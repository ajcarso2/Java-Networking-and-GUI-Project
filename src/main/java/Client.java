import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import org.json.JSONObject;

public class Client {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        String host = "localhost";
        int port = 8080;


        // Check if host and port have been provided as arguments
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-h") && i < args.length - 1) {
                    host = args[i + 1];
                } else if (args[i].equals("-p") && i < args.length - 1) {
                    port = Integer.parseInt(args[i + 1]);
                }
            }
        }else {
            try (InputStream in = Client.class.getClassLoader().getResourceAsStream("gradle.properties")) {
                props.load(in);
            }
        }

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server: " + host + ":" + port);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // Create a JSON request object with action and data fields
            JSONObject request = new JSONObject();
            request.put("action", "echo");
            JSONObject data = new JSONObject();
            data.put("message", "Hello, server!");
            request.put("data", data);

            NetworkUtils.Send(out, request.toString().getBytes());

            byte[] responseBytes = NetworkUtils.Receive(in);
            if (responseBytes.length > 0) {
                JSONObject response = new JSONObject(new String(responseBytes));
                System.out.println("Received response from server: " + response);
            } else {
                System.out.println("Received empty response from server.");
            }
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + host + ":" + port);
            e.printStackTrace();
        }
    }
}