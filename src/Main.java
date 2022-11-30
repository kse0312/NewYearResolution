import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8000)){
            System.out.println("[Server Start] Waiting......................");
            GetThread getThread;
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("[Client Accept %s:%d]\n",clientSocket.getInetAddress(), clientSocket.getPort());

                BufferedReader reader  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String[] request = reader.readLine().split(" ");

                System.out.println("[Request "+ Arrays.toString(request) +"]");

                if(request[0].equals("GET")){
                    getThread = new GetThread(clientSocket, request[1]);
                    getThread.run();
                }

                System.out.printf("[Client Closed %s:%d]\n",clientSocket.getInetAddress(), clientSocket.getPort());
                clientSocket.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}