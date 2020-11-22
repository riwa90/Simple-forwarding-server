import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    public static void main( String[] args) throws IOException {
        int socketNr = 0;
        ServerSocket serverSocket = null;
        if(args.length > 0){
            socketNr = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("You need to specifiy what port to host the server. Proper way to call is: java HTTPEcho port");
            System.out.println("Terminating program.");
            System.exit(0);
        }
        try {
            serverSocket = new ServerSocket(socketNr);
        }
        catch (IllegalArgumentException e){
            System.err.println(e);
            System.out.println("Terminating program.");
            System.exit(0);
        }


        while(true){
            Socket socket = serverSocket.accept();
            Runnable serverSession = new ServerProcess(socket);
            Thread newSession = new Thread(serverSession);
            newSession.start();
        }

    }
}

