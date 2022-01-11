package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private static String serverResponse ;
    private Client client;

    //Class responsible for handling all responses from the server
    public ServerConnection(Client client)throws IOException{
        this.client = client;
        this.socket = client.getSocket();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {

            try {
                while (true) {
                    //just listening to what server say and print in
                    serverResponse = in.readLine();
                    System.out.println("From serv Conn : "+serverResponse);
//                    if (serverResponse.startsWith("INFO"))
//                        System.out.println("From ServConn : "+serverResponse);
//
//                    if (serverResponse.startsWith("nb")){
//
//                    }
                    if (serverResponse==null) break;

                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public static String getServerResponse() {
        return serverResponse;
    }
}
