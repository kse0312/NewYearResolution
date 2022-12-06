import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PostThread implements Runnable{
    private static final String DEFAULT_FILE_PATH = "index.html";
    private Socket socket;
    private String filePath;
    private User content;
    public PostThread(Socket clientSocket, String filePath, char[] body) throws IOException {
        this.socket = clientSocket;
        this.filePath = filePath;
        
        //URL 형식으로 데이터가 날아 오기 때문에 URL Decoding 해줘야 함 안하면 한글 깨짐
        String decodeData = URLDecoder.decode(String.valueOf(body), StandardCharsets.UTF_8);
        //쪼개기
        String[] Data = decodeData.split("&");
        System.out.println(Arrays.toString(Data));
        for(int i = 0;i<Data.length;i++){
            String[] temp = Data[i].split("=");
            if(temp.length==1) Data[i]= "";
            else Data[i]=temp[1];
        }

        this.content = new User(Data);
    }
    @Override
    public void run() {
        System.out.println("POST Thread : Ready");

        /**content database에 저장해야 돼*/
        System.out.println(content);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
