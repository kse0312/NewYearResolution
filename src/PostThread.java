import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class PostThread implements Runnable{
    private static final String DEFAULT_FILE_PATH = "index.html";
    private Socket socket;
    private String filePath;
    private String content;
    public PostThread(Socket clientSocket, String filePath,String content){
        this.socket = clientSocket;
        this.filePath = filePath;
        this.content = content;
    }
    @Override
    public void run() {
        System.out.println("POST Thread : Ready");
        System.out.println(content);
        try(DataOutputStream dout = new DataOutputStream(socket.getOutputStream())){
            System.out.println("POST Thread : DataOutputStream Ready");

            File file = null;
            if(filePath.length()>1){ file = new File(filePath.substring(1)); }
            else{ file = new File(DEFAULT_FILE_PATH);}

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
