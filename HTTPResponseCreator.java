import java.util.*;
import java.nio.charset.StandardCharsets;

class HTTPResponseCreator {
    public static void createResponse(LinkedList<byte[]> outputList, int responseCode){
        if(responseCode == 200){
            outputList.add("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        } else if(responseCode == 400){
            outputList.add("HTTP/1.1 400 Bad Request\r\n".getBytes(StandardCharsets.UTF_8));
        } else {
            outputList.add("HTTP/1.1 404 Service Not Found\r\n".getBytes(StandardCharsets.UTF_8));
        }
        outputList.add("Connection: close\r\n".getBytes(StandardCharsets.UTF_8));
        outputList.add("Server: Rilles hemgjorda\r\n".getBytes(StandardCharsets.UTF_8));
        outputList.add("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public static void addBody(LinkedList<byte[]> outputList, String body){
        outputList.add(body.getBytes(StandardCharsets.UTF_8));
    }
}