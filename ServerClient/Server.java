import java.io.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Iterator;

public class Server implements HttpHandler {
    private List<JSONObject> history = new ArrayList<JSONObject>();
    private MessageExchange messageExchange = new MessageExchange();
    private JSONParser jsonParser = new JSONParser();
    private PrintWriter out;
    private Date d;

    public Server() throws IOException{
        out = new PrintWriter(new BufferedWriter(new FileWriter(new File("serverlog.txt"))));
    }

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        d = new Date();
        out.flush();

        String response = "";

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        }
        else if("DELETE".equals(httpExchange.getRequestMethod())){
            doDelete(httpExchange);
        }
        else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
            d = new Date();
            out.println(d.toLocaleString() + " " + response);
            out.flush();
        }

        sendResponse(httpExchange, response);
        d = new Date();
        out.flush();
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");

            if (token != null && !"".equals(token)) {
                d = new Date();
                out.println(d.toLocaleString() + " request method: GET");
                out.println(d.toLocaleString() + " request parameters: token: " + token);
                out.flush();

                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(history.subList(index, history.size()));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        d = new Date();
        out.println(d.toLocaleString() + " Absent query in url");
        out.flush();
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            JSONObject msg = messageExchange.getClientMessage(httpExchange.getRequestBody());

            d = new Date();
            out.println(d.toLocaleString() + " request method: POST");
            out.println(d.toLocaleString() + " request parameters: " + msg);
            out.flush();

            System.out.println("Get Message from User: " + msg);
            history.add(msg);
        } catch (ParseException e) {
            d = new Date();
            out.println(d.toLocaleString() + " Invalid user message");
            out.flush();
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doDelete(HttpExchange httpExchange) {
        try{
            JSONObject message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            for(JSONObject object: history)
                if(object.get("id").equals(message.get("id"))){
                    object.put("message", "DELETED");
                    object.put("deleted","true");
                    out.println(d.toLocaleString() + " request method: DELETE");
                }
        }
        catch(ParseException e){
            System.err.println("Invalid request message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }


    private void sendResponse(HttpExchange httpExchange, String response){
        try {
            d = new Date();
            try{
                JSONObject resp = (JSONObject) jsonParser.parse(response.trim());
                out.println(d.toLocaleString() + " server response");
                out.println(d.toLocaleString() + "response parameters: token: " + resp.get("token") + " messages: " + resp.get("messages"));
                out.flush();
            }
            catch (ParseException p){}

            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin","*");
            httpExchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = httpExchange.getResponseBody();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}