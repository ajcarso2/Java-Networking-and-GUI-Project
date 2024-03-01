import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class CharactersClass{
    public enum Characters{
        Captain_America, Darth_Vader, Homer_Simpson, Jack_Sparrow, Joker, Tony_Stark, Wolverine
    }
}


public class ServerLogic {

    public static JSONArray scoreBoard = new JSONArray();


    static String[] Characters = {"Captain America", "Darth Vader", "Homer Simpson", "Jack Sparrow", "Joker", "Tony Stark", "Wolverine"};

    //images of the characters
    static String[] Captain_America_img = {"img/Captain_America/quote1.png", "img/Captain_America/quote2.png", "img/Captain_America/quote3.png", "img/Captain_America/quote4.png"};
    static String[] Darth_Vader_img = {"img/Darth_Vader/quote1.png", "img/Darth_Vader/quote2.png", "img/Darth_Vader/quote3.png", "img/Darth_Vader/quote4.png"};
    static String[] Homer_Simpson_img = {"img/Homer_Simpson/quote1.png", "img/Homer_Simpson/quote2.png", "img/Homer_Simpson/quote3.png", "img/Homer_Simpson/quote4.png"};
    static String[] Jack_Sparrow_img = {"img/Jack_Sparrow/quote1.png", "img/Jack_Sparrow/quote2.png", "img/Jack_Sparrow/quote3.png", "img/Jack_Sparrow/quote4.png"};
    static String[] Joker_img = {"img/Joker/quote1.png", "img/Joker/quote2.png", "img/Joker/quote3.png", "img/Joker/quote4.png"};
    static String[] Tony_Stark_img = {"img/Tony_Stark/quote1.png", "img/Tony_Stark/quote2.png", "img/Tony_Stark/quote3.png", "img/Tony_Stark/quote4.png"};
    static String[] Wolverine_img = {"img/Wolverine/quote1.png", "img/Wolverine/quote2.png", "img/Wolverine/quote3.png", "img/Wolverine/quote4.png"};


    public static int numScoreBoard = 0;

    public static boolean timeUp = false;

    public static void main(String[] args) {
        // set up the client side
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
        }

        int moreUse = 0;
        int points = 0;
        String name = "";


        CharactersClass charactersClass = new CharactersClass();



        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            Timer timer = null;

            CharactersClass.Characters current = CharactersClass.Characters.Joker;
            int currentQuote = 0;

            Socket socket = serverSocket.accept();

            while (true) {

                System.out.println("Accepted connection from " + socket.getInetAddress());

                // Read request from client
                //byte[] requestBytes = NetworkUtils.Receive(socket.getInputStream());
                //JSONObject requestJson = new JSONObject(new String(requestBytes));
                byte[] requestBytes = NetworkUtils.Receive(socket.getInputStream());



                System.out.println("Received request: " + new String(requestBytes) + "breh");

                if(!(new String(requestBytes).equals(""))){

                    JSONObject requestJson = JsonUtils.fromByteArray(requestBytes);

                    JSONObject responseJson = new JSONObject();

                    responseJson.put("gameEnded", false);

                    System.out.println("Received request: " + requestJson);

                    // Process request
                    switch (requestJson.getString("action")) {
                        case "hello":
                            //
                            responseJson.put("response", "Hello, What is your name?");
                            break;
                        case "welcome":

                            System.out.println("Welcome, " + requestJson.getString("name") + "!\n type start to begin");

                            responseJson.put("response", "Welcome, " + requestJson.getString("name") + "!\nPlease type \"start\" to start the game, or \"leaderboard\" to see the leaderboard");
                            name = requestJson.getString("name");
                            break;
                        case "start":
                            responseJson.put("response", "Who is this quote from?");
                            timer = new Timer();
                            timer.schedule(new TimerTask(){
                                @Override
                                public void run() {
                                    timeUp = true;
                                    System.out.println("Time's up!");
                                }
                            }, 60000);


                            int random = (int) (Math.random() * CharactersClass.Characters.values().length);
                            current = CharactersClass.Characters.values()[random];

                            System.out.println("Character: " + current);

                            //chooses a random quote from the character
                            currentQuote = (int) (Math.random() * 4);

                            //create code here that sends the image of the quote to the client
                            //adds the image to the responseJson
                            switch (current){
                                case Captain_America:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Captain_America_img[0]);
                                        case 1 -> addImageToJson(responseJson, Captain_America_img[1]);
                                        case 2 -> addImageToJson(responseJson, Captain_America_img[2]);
                                        case 3 -> addImageToJson(responseJson, Captain_America_img[3]);
                                    }
                                    break;
                                case Darth_Vader:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Darth_Vader_img[0]);
                                        case 1 -> addImageToJson(responseJson, Darth_Vader_img[1]);
                                        case 2 -> addImageToJson(responseJson, Darth_Vader_img[2]);
                                        case 3 -> addImageToJson(responseJson, Darth_Vader_img[3]);
                                    }
                                    break;
                                case Homer_Simpson:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Homer_Simpson_img[0]);
                                        case 1 -> addImageToJson(responseJson, Homer_Simpson_img[1]);
                                        case 2 -> addImageToJson(responseJson, Homer_Simpson_img[2]);
                                        case 3 -> addImageToJson(responseJson, Homer_Simpson_img[3]);
                                    }
                                    break;
                                case Jack_Sparrow:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Jack_Sparrow_img[0]);
                                        case 1 -> addImageToJson(responseJson, Jack_Sparrow_img[1]);
                                        case 2 -> addImageToJson(responseJson, Jack_Sparrow_img[2]);
                                        case 3 -> addImageToJson(responseJson, Jack_Sparrow_img[3]);
                                    }
                                    break;
                                case Joker:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Joker_img[0]);
                                        case 1 -> addImageToJson(responseJson, Joker_img[1]);
                                        case 2 -> addImageToJson(responseJson, Joker_img[2]);
                                        case 3 -> addImageToJson(responseJson, Joker_img[3]);
                                    }
                                    break;
                                case Tony_Stark:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Tony_Stark_img[0]);
                                        case 1 -> addImageToJson(responseJson, Tony_Stark_img[1]);
                                        case 2 -> addImageToJson(responseJson, Tony_Stark_img[2]);
                                        case 3 -> addImageToJson(responseJson, Tony_Stark_img[3]);
                                    }
                                    break;
                                case Wolverine:
                                    switch (currentQuote) {
                                        case 0 -> addImageToJson(responseJson, Wolverine_img[0]);
                                        case 1 -> addImageToJson(responseJson, Wolverine_img[1]);
                                        case 2 -> addImageToJson(responseJson, Wolverine_img[2]);
                                        case 3 -> addImageToJson(responseJson, Wolverine_img[3]);
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid character");
                                    break;
                            }



                            break;
                        case "guess":
                            if (requestJson.getString("guess").equalsIgnoreCase(Characters[current.ordinal()])) {
                                responseJson.put("response", "Correct!\nWho is this quote from?");

                                if(timeUp) {
                                    timeUp = false;
                                    timer.cancel();
                                    timer.purge();
                                    moreUse = 0;

                                    JSONObject scoreBoardSingle = new JSONObject();
                                    scoreBoardSingle.put("name", name);
                                    scoreBoardSingle.put("points", points);

                                    scoreBoard.put(scoreBoardSingle);


                                    name = null;
                                    points = 0;
                                    responseJson.put("response", "Time's up!");
                                    responseJson.put("gameEnded", true);
                                }else {

                                    //calculates the points
                                    if (moreUse > 1) {
                                        points += 2;
                                    } else if (moreUse == 1) {
                                        points += 3;
                                    } else {
                                        points += 5;
                                    }
                                    moreUse = 0;

                                    int random2 = (int) (Math.random() * CharactersClass.Characters.values().length);

                                    //chooses a random quote from the character
                                    currentQuote = (int) (Math.random() * 4);

                                    //ensure that the same character is not chosen twice in a row
                                    while (random2 == current.ordinal()) {
                                        random2 = (int) (Math.random() * CharactersClass.Characters.values().length);
                                    }
                                    current = CharactersClass.Characters.values()[random2];

                                    switch (current) {
                                        case Captain_America:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Captain_America_img[0]);
                                                case 1 -> addImageToJson(responseJson, Captain_America_img[1]);
                                                case 2 -> addImageToJson(responseJson, Captain_America_img[2]);
                                                case 3 -> addImageToJson(responseJson, Captain_America_img[3]);
                                            }
                                            break;
                                        case Darth_Vader:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Darth_Vader_img[0]);
                                                case 1 -> addImageToJson(responseJson, Darth_Vader_img[1]);
                                                case 2 -> addImageToJson(responseJson, Darth_Vader_img[2]);
                                                case 3 -> addImageToJson(responseJson, Darth_Vader_img[3]);
                                            }
                                            break;
                                        case Homer_Simpson:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Homer_Simpson_img[0]);
                                                case 1 -> addImageToJson(responseJson, Homer_Simpson_img[1]);
                                                case 2 -> addImageToJson(responseJson, Homer_Simpson_img[2]);
                                                case 3 -> addImageToJson(responseJson, Homer_Simpson_img[3]);
                                            }
                                            break;
                                        case Jack_Sparrow:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Jack_Sparrow_img[0]);
                                                case 1 -> addImageToJson(responseJson, Jack_Sparrow_img[1]);
                                                case 2 -> addImageToJson(responseJson, Jack_Sparrow_img[2]);
                                                case 3 -> addImageToJson(responseJson, Jack_Sparrow_img[3]);
                                            }
                                            break;
                                        case Joker:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Joker_img[0]);
                                                case 1 -> addImageToJson(responseJson, Joker_img[1]);
                                                case 2 -> addImageToJson(responseJson, Joker_img[2]);
                                                case 3 -> addImageToJson(responseJson, Joker_img[3]);
                                            }
                                            break;
                                        case Tony_Stark:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Tony_Stark_img[0]);
                                                case 1 -> addImageToJson(responseJson, Tony_Stark_img[1]);
                                                case 2 -> addImageToJson(responseJson, Tony_Stark_img[2]);
                                                case 3 -> addImageToJson(responseJson, Tony_Stark_img[3]);
                                            }
                                            break;
                                        case Wolverine:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Wolverine_img[0]);
                                                case 1 -> addImageToJson(responseJson, Wolverine_img[1]);
                                                case 2 -> addImageToJson(responseJson, Wolverine_img[2]);
                                                case 3 -> addImageToJson(responseJson, Wolverine_img[3]);
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid character");
                                            break;
                                    }
                                }

                            } else if(requestJson.getString("guess").equalsIgnoreCase("next")){
                                if(timeUp) {
                                    responseJson.put("response", "Time's up!\nWho is this quote from?");
                                    timeUp = false;
                                    timer.cancel();
                                    timer.purge();
                                    moreUse = 0;

                                    JSONObject scoreBoardSingle = new JSONObject();
                                    scoreBoardSingle.put("name", name);
                                    scoreBoardSingle.put("points", points);

                                    scoreBoard.put(scoreBoardSingle);

                                    name = null;
                                    points = 0;
                                    responseJson.put("response", "Time's up!");
                                    responseJson.put("gameEnded", true);
                                }else {

                                    responseJson.put("response", "Skipping!\nWho is this quote from?");
                                    int random2 = (int) (Math.random() * CharactersClass.Characters.values().length);

                                    moreUse = 0;

                                    points -= 2;
                                    //chooses a random quote from the character
                                    currentQuote = (int) (Math.random() * 4);

                                    //ensure that the same character is not chosen twice in a row
                                    while (random2 == current.ordinal()) {
                                        random2 = (int) (Math.random() * CharactersClass.Characters.values().length);
                                    }
                                    current = CharactersClass.Characters.values()[random2];

                                    switch (current) {
                                        case Captain_America:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Captain_America_img[0]);
                                                case 1 -> addImageToJson(responseJson, Captain_America_img[1]);
                                                case 2 -> addImageToJson(responseJson, Captain_America_img[2]);
                                                case 3 -> addImageToJson(responseJson, Captain_America_img[3]);
                                            }
                                            break;
                                        case Darth_Vader:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Darth_Vader_img[0]);
                                                case 1 -> addImageToJson(responseJson, Darth_Vader_img[1]);
                                                case 2 -> addImageToJson(responseJson, Darth_Vader_img[2]);
                                                case 3 -> addImageToJson(responseJson, Darth_Vader_img[3]);
                                            }
                                            break;
                                        case Homer_Simpson:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Homer_Simpson_img[0]);
                                                case 1 -> addImageToJson(responseJson, Homer_Simpson_img[1]);
                                                case 2 -> addImageToJson(responseJson, Homer_Simpson_img[2]);
                                                case 3 -> addImageToJson(responseJson, Homer_Simpson_img[3]);
                                            }
                                            break;
                                        case Jack_Sparrow:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Jack_Sparrow_img[0]);
                                                case 1 -> addImageToJson(responseJson, Jack_Sparrow_img[1]);
                                                case 2 -> addImageToJson(responseJson, Jack_Sparrow_img[2]);
                                                case 3 -> addImageToJson(responseJson, Jack_Sparrow_img[3]);
                                            }
                                            break;
                                        case Joker:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Joker_img[0]);
                                                case 1 -> addImageToJson(responseJson, Joker_img[1]);
                                                case 2 -> addImageToJson(responseJson, Joker_img[2]);
                                                case 3 -> addImageToJson(responseJson, Joker_img[3]);
                                            }
                                            break;
                                        case Tony_Stark:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Tony_Stark_img[0]);
                                                case 1 -> addImageToJson(responseJson, Tony_Stark_img[1]);
                                                case 2 -> addImageToJson(responseJson, Tony_Stark_img[2]);
                                                case 3 -> addImageToJson(responseJson, Tony_Stark_img[3]);
                                            }
                                            break;
                                        case Wolverine:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Wolverine_img[0]);
                                                case 1 -> addImageToJson(responseJson, Wolverine_img[1]);
                                                case 2 -> addImageToJson(responseJson, Wolverine_img[2]);
                                                case 3 -> addImageToJson(responseJson, Wolverine_img[3]);
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid character");
                                            break;
                                    }
                                }
                            } else if(requestJson.getString("guess").equalsIgnoreCase("more")){
                                if(timeUp) {
                                    responseJson.put("response", "Time's up!\nWho is this quote from?");
                                    timeUp = false;
                                    timer.cancel();
                                    timer.purge();
                                    moreUse = 0;

                                    JSONObject scoreBoardSingle = new JSONObject();
                                    scoreBoardSingle.put("name", name);
                                    scoreBoardSingle.put("points", points);

                                    scoreBoard.put(scoreBoardSingle);

                                    name = null;
                                    points = 0;
                                    responseJson.put("response", "Time's up!");
                                    responseJson.put("gameEnded", true);
                                }else {

                                    responseJson.put("response", "Alright, Here's a new one!\nWho is this quote from?");

                                    moreUse += 1;
                                    System.out.println("current quote: " + currentQuote);

                                    //chooses a random quote from the character
                                    int newQuote = (int) (Math.random() * 4);
                                    while (newQuote == currentQuote) {
                                        System.out.println("current quote: " + newQuote);
                                        newQuote = (int) (Math.random() * 4);
                                    }

                                    currentQuote = newQuote;

                                    System.out.println("current quote: " + currentQuote);

                                    switch (current) {
                                        case Captain_America:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Captain_America_img[0]);
                                                case 1 -> addImageToJson(responseJson, Captain_America_img[1]);
                                                case 2 -> addImageToJson(responseJson, Captain_America_img[2]);
                                                case 3 -> addImageToJson(responseJson, Captain_America_img[3]);
                                            }
                                            break;
                                        case Darth_Vader:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Darth_Vader_img[0]);
                                                case 1 -> addImageToJson(responseJson, Darth_Vader_img[1]);
                                                case 2 -> addImageToJson(responseJson, Darth_Vader_img[2]);
                                                case 3 -> addImageToJson(responseJson, Darth_Vader_img[3]);
                                            }
                                            break;
                                        case Homer_Simpson:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Homer_Simpson_img[0]);
                                                case 1 -> addImageToJson(responseJson, Homer_Simpson_img[1]);
                                                case 2 -> addImageToJson(responseJson, Homer_Simpson_img[2]);
                                                case 3 -> addImageToJson(responseJson, Homer_Simpson_img[3]);
                                            }
                                            break;
                                        case Jack_Sparrow:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Jack_Sparrow_img[0]);
                                                case 1 -> addImageToJson(responseJson, Jack_Sparrow_img[1]);
                                                case 2 -> addImageToJson(responseJson, Jack_Sparrow_img[2]);
                                                case 3 -> addImageToJson(responseJson, Jack_Sparrow_img[3]);
                                            }
                                            break;
                                        case Joker:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Joker_img[0]);
                                                case 1 -> addImageToJson(responseJson, Joker_img[1]);
                                                case 2 -> addImageToJson(responseJson, Joker_img[2]);
                                                case 3 -> addImageToJson(responseJson, Joker_img[3]);
                                            }
                                            break;
                                        case Tony_Stark:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Tony_Stark_img[0]);
                                                case 1 -> addImageToJson(responseJson, Tony_Stark_img[1]);
                                                case 2 -> addImageToJson(responseJson, Tony_Stark_img[2]);
                                                case 3 -> addImageToJson(responseJson, Tony_Stark_img[3]);
                                            }
                                            break;
                                        case Wolverine:
                                            switch (currentQuote) {
                                                case 0 -> addImageToJson(responseJson, Wolverine_img[0]);
                                                case 1 -> addImageToJson(responseJson, Wolverine_img[1]);
                                                case 2 -> addImageToJson(responseJson, Wolverine_img[2]);
                                                case 3 -> addImageToJson(responseJson, Wolverine_img[3]);
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid character");
                                            break;
                                    }
                                }
                            } else {
                                if(timeUp) {
                                    responseJson.put("response", "Time's up!\nWho is this quote from?");
                                    timeUp = false;
                                    timer.cancel();
                                    timer.purge();
                                    moreUse = 0;

                                    JSONObject scoreBoardSingle = new JSONObject();
                                    scoreBoardSingle.put("name", name);
                                    scoreBoardSingle.put("points", points);

                                    scoreBoard.put(scoreBoardSingle);

                                    name = null;
                                    points = 0;
                                    responseJson.put("response", "Time's up!");
                                    responseJson.put("gameEnded", true);
                                }else{
                                    responseJson.put("response", "Incorrect!");
                                }

                            }
                            break;

                        case "leaderboard":
                            responseJson.put("leaderboard", scoreBoard);
                            responseJson.put("response", "Here's the scoreboard!");
                            break;


                        default:
                            System.out.println("Invalid action");
                            break;


                    }

                    responseJson.put("points", points);

                    // Send response to client
                    byte[] responseBytes = responseJson.toString().getBytes();
                    NetworkUtils.Send(socket.getOutputStream(), responseBytes);
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addImageToJson(JSONObject json, String image) throws IOException {
        File file = new File(image);
        if (!file.exists()) {
            System.err.println("Cannot find file: " + file.getAbsolutePath());
            System.exit(-1);
        }
        // Read in image
        BufferedImage img = ImageIO.read(file);
        byte[] bytes = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            bytes = out.toByteArray();
        }
        if (bytes != null) {
            Base64.Encoder encoder = Base64.getEncoder();
            json.put("image", encoder.encodeToString(bytes));
        }
    }


}
