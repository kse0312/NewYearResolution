import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostThread implements Runnable{
    private static final String DEFAULT_FILE_PATH = "index.html";
    private final Socket socket;
    private final String filePath;
    private final Map<String, String> content;
    private final MongoClient mongoClient = new MongoClient( new MongoClientURI(DBinfo.getDB_URI()));
    private final MongoDatabase mongoDB = mongoClient.getDatabase(DBinfo.getDB());
    private final MongoCollection<Document> books = mongoDB.getCollection(DBinfo.getDB_C());
    public PostThread(Socket clientSocket, String filePath, char[] body) throws IOException {
        this.socket = clientSocket;
        this.filePath = filePath;
        this.content = new LinkedHashMap<String, String>();

        //URL 형식으로 데이터가 날아 오기 때문에 URL Decoding 해줘야 함 안하면 한글 깨짐
        String decodeData = URLDecoder.decode(String.valueOf(body), StandardCharsets.UTF_8);
        //쪼개기
        String[] Data = decodeData.split("&");
        for (String datum : Data) {
            String[] temp = datum.split("=");
            if (temp.length == 1) content.put(temp[0], "");
            else content.put(temp[0], temp[1]);

        }
    }

    @Override
    public void run() {
        System.out.println("POST Thread : Ready");

        Document doc = new Document();
        // 방법1)
        for ( String key : content.keySet() ) {
            doc.append(key, content.get(key));
            System.out.println("방법1) key : " + key +" / value : " + content.get(key));
        }
        System.out.println("=======================");

        books.insertOne(doc);

        System.out.println("map: "+content);
        try(DataOutputStream dout = new DataOutputStream(socket.getOutputStream())){
            System.out.println("POST Thread : DataOutputStream Ready");

            File file = file = new File(DEFAULT_FILE_PATH);
            //if(filePath.length()>1){ file = new File(filePath.substring(1)); }
            //else{ file = new File(DEFAULT_FILE_PATH);}

            int FileLength = (int)file.length();
            //파일이 존재할 경우 읽기
            if(file.exists()){
                FileInputStream in = new FileInputStream(file);
                byte[] fBytes = new byte[FileLength];
                in.read(fBytes);
                in.close();

                dout.writeBytes("HTTP/1.1 200 OK \r\n");
                dout.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
                dout.writeBytes("Content-Length: " + FileLength + "\r\n");
                dout.writeBytes("\r\n");
                dout.write(fBytes, 0, FileLength);

                dout.writeBytes("\r\n");
                dout.flush();
                System.out.println("POST Thread : Print Web Page");
            }else{
                System.out.println("POST Thread : RequestFile is not Exist");
            }
            socket.close();
            System.out.printf("Client Closed %s:%d]\n",socket.getInetAddress(), socket.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
