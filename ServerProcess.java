import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

class ServerProcess implements Runnable{
    private int BUFFER_SIZE = 4096;
    private int END_OF_INPUT = -1;
    private int HTTP_CODE_OK = 200;
    private int HTTP_CODE_BAD_REQ = 400;
    private int HTTP_CODE_NOT_FOUND = 404;

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private int inputIndex;
    private byte[] inputBuffer = new byte[BUFFER_SIZE];
    private String serverResponse;
    private LinkedList<byte[]> response = new LinkedList<byte[]>();
    private HashMap<String, String> userQuery;

    public ServerProcess(Socket socket){
        this.socket = socket;
    }

    private void sendResponse() throws IOException {
        for(byte[] line : response){
            out.write(line);
        }
    }

    private void recieveInput() throws IOException{
        inputIndex = 0;
        while(inputIndex != END_OF_INPUT) {
            inputIndex = in.read(inputBuffer);
            if(in.available() == 0){
                break;
            }
        }
    }

    private void createAndSendFailureResponse(int failCode) throws IOException {
        if(failCode == HTTP_CODE_BAD_REQ){
            HTTPResponseCreator.createResponse(response, HTTP_CODE_BAD_REQ);
            HTTPResponseCreator.addBody(response, HTTP_CODE_BAD_REQ + " Bad request");
        } else if(failCode == HTTP_CODE_NOT_FOUND){
            HTTPResponseCreator.createResponse(response, HTTP_CODE_NOT_FOUND);
            HTTPResponseCreator.addBody(response, HTTP_CODE_NOT_FOUND + " Site not found");
        }
        this.sendResponse();
        socket.close();
    }

    public void run() {
        try {
            in = this.socket.getInputStream();
            out = this.socket.getOutputStream();

            this.recieveInput();

            try {
                userQuery = StringParse.dataToQueries(inputBuffer);
            } catch (IllegalArgumentException e) {
                createAndSendFailureResponse(HTTP_CODE_BAD_REQ);
                return;
            }

            if (!userQuery.containsKey("port") || !userQuery.containsKey("hostname")) {
                createAndSendFailureResponse(HTTP_CODE_BAD_REQ);
                return;
            }

            try {
                if (userQuery.containsKey("string")) {
                    serverResponse = TCPClient.askServer(
                            userQuery.get("hostname").toString(),
                            Integer.parseInt(userQuery.get("port").toString()),
                            userQuery.get("string").toString()
                    );
                } else {
                    serverResponse = TCPClient.askServer(
                            userQuery.get("hostname").toString(),
                            Integer.parseInt(userQuery.get("port").toString())
                    );
                }
            } catch (Exception e) {
                createAndSendFailureResponse(HTTP_CODE_NOT_FOUND);
                return;
            }

            HTTPResponseCreator.createResponse(response, HTTP_CODE_OK);
            HTTPResponseCreator.addBody(response, serverResponse);
            this.sendResponse();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}