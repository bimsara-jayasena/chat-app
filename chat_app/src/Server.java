import com.sun.security.jgss.GSSUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    //creating list for store multiple clients
    private static List<ClientHandler> clients= Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args){
        try( ServerSocket serverSocket=new ServerSocket(5000)){

            System.out.println("server is started");
            while (true) {
              Socket socket=serverSocket.accept();
              System.out.println("new client is connected");

              ClientHandler client=new ClientHandler(socket);
              //add client to the list
              clients.add(client);
              Thread thread=new Thread(client);
              thread.start();
            }
        }catch (IOException e){}
    }
    private static class ClientHandler implements Runnable{
        private static Socket socket;
        private static PrintWriter writer;
        private static BufferedReader reader;
        private static BufferedReader consoleReader;
        public   ClientHandler(Socket socket){
            this.socket=socket;
        }
        @Override
        public void run() {
                try {
                    writer=new PrintWriter(socket.getOutputStream(),true);
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    consoleReader=new BufferedReader(new InputStreamReader(System.in));

                    //send message to client
                    writer.println("Hello from server");

                    //read from the client
                    //System.out.println(reader.readLine());

                    //reply to client
                    writer.println("welcome to the chat");

                    //communication loop
                    String in;
                    while((in=reader.readLine())!=null) {
                        System.out.println("[Client]: " + in);
                        broadCast(in,this);
                        System.out.print("your message: ");
                        writer.println(consoleReader.readLine());


                    }
                    //close the resources;
                    writer.close();
                    reader.close();
                    consoleReader.close();


                }
                catch (SocketException e){
                    System.out.println("Client disconnected");
                }
                catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

        }
        static void sendMessage(String message){
                try{
                    writer=new PrintWriter(socket.getOutputStream(),true);
                    writer.println(message);
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
        public  void broadCast(String message,ClientHandler sender){

            for(ClientHandler client:clients){
                if(client!=sender){
                    sendMessage(message);
                }
            }
        }
    }

}
