import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
public class Main {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8000)){
            System.out.println("[Server Start] Waiting......................");
            GetThread getThread;
            PostThread postThread;

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
                        getThread = new GetThread(clientSocket, request[1]);
                        getThread.run();
                    }
                    case "POST" -> {
                        //Body 받기
                        //URL 형식으로 데이터가 날아 오기 때문에 한글은 URL Decoding 해줘야 함
                        int leng = Integer.parseInt(header.get("Content-Length"));
                        char[] body = new char[leng];
                        reader.read(body,0,leng);
                        String decodeData = URLDecoder.decode(String.valueOf(body), StandardCharsets.UTF_8);
                        System.out.println("Post Content : "+decodeData);
                        postThread = new PostThread(clientSocket, request[1], decodeData);
                        postThread.run();
                    }
                }
                System.out.printf("Client Closed %s:%d]\n",clientSocket.getInetAddress(), clientSocket.getPort());
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}