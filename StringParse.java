import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

class StringParse{

    private static int PROPER_INDEX_OF_QUESTION_MARK = 4;   // Query starts with /ask?....

    public static HashMap<String, String> parseQueries(String HTTPString){
        int indexOfQuestionMark;
        String[] queryTemp = new String[2];
        HashMap queries = new HashMap<String, String>(10);
        String parseTemp;
        String[] parseTempArray;

        parseTempArray = HTTPString.split(" ");

        if(isSupportedRequest(parseTempArray))
            throw new IllegalArgumentException("Unsupported request");

        parseTemp = parseTempArray[1];
        indexOfQuestionMark = parseTemp.indexOf("?");
        if(indexOfQuestionMark != PROPER_INDEX_OF_QUESTION_MARK)
            throw new IllegalArgumentException("No argument found");

        parseTemp = parseTemp.substring(indexOfQuestionMark + 1);
        parseTempArray = parseTemp.split("&");
        for(String parameter : parseTempArray) {
            queryTemp = parameter.split("=");
            queries.put(queryTemp[0], queryTemp[1]);
        }

        return queries;
    }

    public static String dataToRequestString(byte[] data){
        String entireHTTPMsg;
        String request;

        entireHTTPMsg = new String(data, StandardCharsets.UTF_8).trim();
        request = entireHTTPMsg.split("\r")[0];
        return request;
    }

    public static HashMap<String, String> dataToQueries(byte[] data){
        String requestString = dataToRequestString(data);
        return parseQueries(requestString);
    }

    private static boolean isSupportedRequest(String[] request){
        return !request[0].equals("GET") || !request[2].equals("HTTP/1.1") || !request[1].substring(0, 4).equals("/ask");
    }

    /*
    Only for testing the static functions.
     */
    public static void main(String[] args) throws Exception {
        String testCall = "GET /ask?hostname=time.nist.gov&port=13&string=Hej HTTP/1.1\r\n";
        byte[] testBuffer = new byte[4096];
        byte[] testData =   (testCall + "HTTP/1.1 200 OK\r\n" + "Host: Blaj.com" + "Connection: close\r\n" + "Server: Rilles hemgjorda\r\n" + "\r\n")
                            .getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i < testData.length; i++)
            testBuffer[i] = testData[i];
        testCall = dataToRequestString(testBuffer);

        System.out.println(testCall);

        HashMap result = parseQueries(testCall);

        for(Object key: result.keySet()){
            System.out.println(key.toString() + ": " + result.get(key));
        }
    }
}