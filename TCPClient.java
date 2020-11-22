import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {

    private static int BUFFER_SIZE = 4096;
    private static int END_OF_INPUT = -1;

    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
        String returnString = TCPClient.contactServer(hostname, port, ToServer);
        return returnString;
    }

    public static String askServer(String hostname, int port) throws  IOException {
        String returnString = TCPClient.contactServer(hostname, port, null);
        return returnString;
    }

    private static String contactServer(String hostname, int port, String ToServer) throws  IOException {

        String returnString = "";
        byte[] serverAnswer = new byte[BUFFER_SIZE];
        Socket clientSocket = new Socket(hostname, port);
        OutputStream outToServer = clientSocket.getOutputStream();
        InputStream inFromServer = clientSocket.getInputStream();
        clientSocket.setSoTimeout(2000);

        if(ToServer != null) {
            ToServer = ToServer + '\n';
            byte[] outputData = ToServer.getBytes(StandardCharsets.UTF_8);
            outToServer.write(outputData);
        }

        while(inFromServer.read(serverAnswer) != END_OF_INPUT && inFromServer.available() > 0){
            if(inFromServer.read(serverAnswer) >= BUFFER_SIZE - 1){
                returnString = returnString + new String(serverAnswer, StandardCharsets.UTF_8).trim();
                serverAnswer = new byte[BUFFER_SIZE];
            }
        }

        returnString = returnString + new String(serverAnswer, StandardCharsets.UTF_8).trim();

        clientSocket.close();
        outToServer.close();
        inFromServer.close();

        return returnString;
    }
}

