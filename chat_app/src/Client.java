import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    public static void main(String[] args){
      try(
              Socket socket=new Socket("127.0.0.1",5000);
              BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
              PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
              BufferedReader consoleReader=new BufferedReader(new InputStreamReader(System.in));
      ){
            //Read from the server
          System.out.println(reader.readLine());

          //communication loop
          String in;
          while((in=reader.readLine())!=null){
                 //read from server
                 System.out.println("[Server]: "+in);
                 //write to the server
                 System.out.print("your message: ");
                 writer.println(consoleReader.readLine());
          }

      }
      catch (SocketException e){
          System.out.println("Connection failed");
      }
      catch (Exception e){
            e.printStackTrace();
      }
    }
}
