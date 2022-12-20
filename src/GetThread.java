import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable{
    // 파일 요청이 없을 경우의 기본 파일
    private static final String DEFAULT_FILE_PATH = "index.html";

    // 클라이언트와의 접속 소켓
    private Socket socket;
    private String filePath;



    public GetThread(Socket clientSocket, String filePath){
        this.socket = clientSocket;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        try(DataOutputStream dout = new DataOutputStream(socket.getOutputStream())){
            System.out.println("GET Thread : DataOutputStream Ready");
            
            //파일명 정리
            String[] path_arr = filePath.split("/");
            String[] param_arr = null;
            File file = null;
            int FileLength =0;
            if(path_arr.length==0){ filePath=DEFAULT_FILE_PATH;}
            else {
                filePath = path_arr[path_arr.length-1];
                if(filePath.contains("?")){
                    param_arr = filePath.split("\\?");
                    filePath = param_arr[0];
                }
            }
            file = new File(filePath);
            FileLength = (int)file.length();

            //파일이 존재할 경우 읽기
            if(file.exists()){
                FileInputStream in = new FileInputStream(file);
                byte[] fBytes = new byte[FileLength];
                in.read(fBytes);
                in.close();

                dout.writeBytes("HTTP/1.1 200 OK \r\n");
                dout.writeBytes("Content-Type: text/html;charset=UTF-8\r\n");
                dout.writeBytes("Content-Length: " + FileLength + "\r\n");
                dout.writeBytes("\r\n");
                dout.write(fBytes, 0, FileLength);

                dout.writeBytes("\r\n");
                dout.flush();
                System.out.println("GET Thread : Print Web Page");
            }else{
                System.out.println("GET Thread : RequestFile is not Exist");
            }
            socket.close();
            System.out.printf("Client Closed %s:%d]\n",socket.getInetAddress(), socket.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
