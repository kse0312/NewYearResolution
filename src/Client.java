import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class Client implements Runnable{
    private final Socket clientsocket;

    public Client(Socket socket) {
        this.clientsocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader  = new BufferedReader(new InputStreamReader(clientsocket.getInputStream(), StandardCharsets.UTF_8));
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
                    Thread getThread;
                    getThread = new Thread(new GetThread(clientsocket, request[1]));
                    getThread.start();
                }
                case "POST" -> {
                    //Body 받기
                    int leng = Integer.parseInt(header.get("Content-Length"));
                    char[] body = new char[leng];
                    reader.read(body,0,leng);
                    Thread postThread;
                    postThread = new Thread(new PostThread(clientsocket, request[1], body));
                    postThread.start();
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
