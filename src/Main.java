import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.*;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient( new MongoClientURI(DBinfo.getDB_URI()));
        // DB 변수 (몽고DB 접속시 데이터베이스 이름 설정.), 여기서 이름 바꾸면 새로운 DB 생성됨.
        MongoDatabase mongoDB = mongoClient.getDatabase(DBinfo.getDB());
        // 위 데이터베이스 내부의 콜렉션 생성 구문. 중복 시 기존 것 수정함(수정..안 하는 것 같은데 에러나는데)
        //mongoDB.createCollection(DB_C);
        MongoCollection<Document> books = mongoDB.getCollection(DBinfo.getDB_C());

        Document doc = new Document()
                .append("passwd","Password")
                .append("name", "Nickname")         // 닉네임
                .append("subject","Subject")
                .append("count", "Count")           // 개수
                .append("target","Target")          // 목표
                .append("backc","#FFF")
                .append("fontc","#000")
                .append("bordc","")
                .append("linec","");
        books.insertOne(doc);

        try(ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("[Server Start] Waiting......................");
            Thread getThread;
            Thread postThread;
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("Client Accept %s:%d\n",clientSocket.getInetAddress(), clientSocket.getPort());

                BufferedReader reader  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
                //Start Line 받기
                String[] request = reader.readLine().split(" ");
                System.out.println("Request "+ Arrays.toString(request));

                //Header 받기
                HashMap<String, String> header = new HashMap<>();
                String line;
                int temp;
                while((line = reader.readLine()).length() !=0) {
                    temp = line.indexOf(":");
                    header.put(line.substring(0, temp), line.substring(temp+2));
                }

                switch (request[0]) {
                    case "GET" -> {
                        getThread = new Thread(new GetThread(clientSocket, request[1]));
                        getThread.start();
                    }
                    case "POST" -> {
                        //Body 받기
                        int leng = Integer.parseInt(header.get("Content-Length"));
                        char[] body = new char[leng];
                        reader.read(body,0,leng);
                        postThread = new Thread(new PostThread(clientSocket, request[1], body));
                        postThread.start();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}