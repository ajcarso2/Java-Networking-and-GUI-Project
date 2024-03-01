import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.json.JSONObject;

public class Server {

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getProperty("port", "8080"));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getInetAddress());

                // Read request from client
                byte[] requestBytes = NetworkUtils.Receive(socket.getInputStream());
                JSONObject requestJson = new JSONObject(new String(requestBytes));
                System.out.println("Received request: " + requestJson);

                // Process request
                String responseMessage = processRequest(requestJson);

                // Send response to client
                byte[] responseBytes = responseMessage.getBytes();
                NetworkUtils.Send(socket.getOutputStream(), responseBytes);

                // Close connection
                socket.close();
                System.out.println("Closed connection from " + socket.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(JSONObject requestJson) {
        String action = requestJson.getString("action");
        JSONObject data = requestJson.optJSONObject("data");

        if (action.equals("echo")) {
            String message = data.getString("message");
            return message;
        } else if (action.equals("sum")) {
            int[] numbers = Arrays.stream(data.getJSONArray("numbers").toString().split(","))
                    .mapToInt(Integer::parseInt).toArray();
            int sum = Arrays.stream(numbers).sum();
            return Integer.toString(sum);
        } else {
            return "Invalid action";
        }
    }
}