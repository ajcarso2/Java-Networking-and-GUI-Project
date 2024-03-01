
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The ClientGui class is a GUI frontend that displays an image grid, an input text box,
 * a button, and a text area for status. 
 * 
 * Methods of Interest
 * ----------------------
 * show(boolean modal) - Shows the GUI frame with the current state
 *     -> modal means that it opens the GUI and suspends background processes. Processing still happens 
 *        in the GUI. If it is desired to continue processing in the background, set modal to false.
 * newGame(int dimension) - Start a new game with a grid of dimension x dimension size
 * insertImage(String filename, int row, int col) - Inserts an image into the grid
 * appendOutput(String message) - Appends text to the output panel
 * submitClicked() - Button handler for the submit button in the output panel
 * 
 * Notes
 * -----------
 * > Does not show when created. show() must be called to show he GUI.
 * 
 */
public class ClientGui implements OutputPanel.EventHandlers {
  JDialog frame;
  PicturePanel picturePanel;
  OutputPanel outputPanel;


  /**
   * Construct dialog
   */
  public ClientGui() {
    frame = new JDialog();
    frame.setLayout(new GridBagLayout());
    frame.setMinimumSize(new Dimension(500, 500));
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    // setup the top picture frame
    picturePanel = new PicturePanel();
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.weighty = 0.25;
    frame.add(picturePanel, c);

    // setup the input, button, and output area
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 1;
    c.weighty = 0.75;
    c.weightx = 1;
    c.fill = GridBagConstraints.BOTH;
    outputPanel = new OutputPanel();
    outputPanel.addEventHandlers(this);
    frame.add(outputPanel, c);
  }

  /**
   * Shows the current state in the GUI
   * @param makeModal - true to make a modal window, false disables modal behavior
   */
  public void show(boolean makeModal) {
    frame.pack();
    frame.setModal(makeModal);
    frame.setVisible(true);
  }

  /**
   * Creates a new game and set the size of the grid 
   * @param dimension - the size of the grid will be dimension x dimension
   */
  public void newGame(int dimension) {
    picturePanel.newGame(dimension);
    outputPanel.appendOutput("Started new game with a " + dimension + "x" + dimension + " board.");
  }

  /**
   * Insert an image into the grid at position (col, row)
   * 
   * @param filename - filename relative to the root directory
   * @param row - the row to insert into
   * @param col - the column to insert into
   * @return true if successful, false if an invalid coordinate was provided
   * @throws IOException An error occured with your image file
   */
  public boolean insertImage(String filename, int row, int col) throws IOException {
    String error = "";
    try {
      // insert the image
      if (picturePanel.insertImage(filename, row, col)) {
      // put status in output
        outputPanel.appendOutput("Inserting " + filename + " in position (" + row + ", " + col + ")");
        return true;
      }
      error = "File(\"" + filename + "\") not found.";
    } catch(PicturePanel.InvalidCoordinateException e) {
      // put error in output
      error = e.toString();
    }
    outputPanel.appendOutput(error);
    return false;
  }

  /**
   * Submit button handling
   * 
   * Change this to whatever you need
   */

// The current state of the game
  // 0 = Just Opened
  // 1 = Name Given
  // 2 = Game Started
  // 3 = In Game
  // 4 = Leaderboard

  int status = 0;

  @Override
  public void submitClicked() throws IOException {
    System.out.println("Submit clicked");

    String input = outputPanel.getInputText();
    if (status == 0){
      // send the server the hello message
      JSONObject hello = new JSONObject();
      hello.put("action", "hello");
      byte[] responseBytes = hello.toString().getBytes();
      System.out.println("Sending: " + hello);
      NetworkUtils.Send(out, responseBytes);

      //convert received bytes to JSONObject
      JSONObject response = JsonUtils.fromByteArray(NetworkUtils.Receive(in));
      outputPanel.appendOutput(response.getString("response"));

      status = 1;

    }else if(status == 1){
      JSONObject name = new JSONObject();
      name.put("action", "welcome");

      name.put("name", input);

      byte[] responseBytes = name.toString().getBytes();
      System.out.println("Sending: " + name);
      NetworkUtils.Send(out, responseBytes);

      //convert received bytes to JSONObject
      JSONObject response = JsonUtils.fromByteArray(NetworkUtils.Receive(in));
      outputPanel.appendOutput(response.getString("response"));

      status = 2;

    }else if(status == 2){

      if(input.equals("start")) {
        JSONObject guess = new JSONObject();
        guess.put("action", "start");

        byte[] responseBytes = guess.toString().getBytes();
        System.out.println("Sending: " + responseBytes);
        NetworkUtils.Send(out, responseBytes);

        //convert received bytes to JSONObject
        JSONObject response = JsonUtils.fromByteArray(NetworkUtils.Receive(in));
        outputPanel.appendOutput(response.getString("response"));

        System.out.println("Your image");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(response.getString("image"));
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
          picturePanel.insertImage(bais, 0, 0);
        } catch (PicturePanel.InvalidCoordinateException e) {
          e.printStackTrace();
        }

        status = 3;

      }else if(input.equals("leaderboard")) {
        JSONObject guess = new JSONObject();
        guess.put("action", "leaderboard");

        byte[] responseBytes = guess.toString().getBytes();
        System.out.println("Sending: " + responseBytes);
        NetworkUtils.Send(out, responseBytes);

        //convert received bytes to JSONObject
        JSONObject response = JsonUtils.fromByteArray(NetworkUtils.Receive(in));
        JSONArray leaderBoard = response.getJSONArray("leaderboard");
        for(int i = 0; i < leaderBoard.length(); i++){
          JSONObject player = leaderBoard.getJSONObject(i);
          outputPanel.appendOutput(player.getString("name") + ": " + player.getInt("points"));
        }


        status = 2;
        outputPanel.appendOutput("Please type \"start\" to start the game, or \"leaderboard\" to see the leaderboard");
      }else{
        outputPanel.appendOutput("Please type \"start\" to start the game, or \"leaderboard\" to see the leaderboard");

      }

    }else if(status == 3) {
      JSONObject guess = new JSONObject();
      guess.put("action", "guess");

      guess.put("guess", input);

      byte[] responseBytes = guess.toString().getBytes();
      System.out.println("Sending: " + responseBytes);
      NetworkUtils.Send(out, responseBytes);

      //convert received bytes to JSONObject
      JSONObject response = JsonUtils.fromByteArray(NetworkUtils.Receive(in));
      outputPanel.appendOutput(response.getString("response"));

      System.out.println("game over: " + response.getBoolean("gameEnded"));
      if(!response.getBoolean("gameEnded")){
        System.out.println("Your image");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(response.getString("image"));
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
          picturePanel.insertImage(bais, 0, 0);
        } catch (PicturePanel.InvalidCoordinateException e) {
          e.printStackTrace();
        }

        outputPanel.setPoints(response.getInt("points"));



      }else{
        System.out.println("Times Up!");
        outputPanel.appendOutput("Please Press Enter to Continue");
        status = 0;

      }
    }
  }
  
  /**
   * Key listener for the input text box
   * 
   * Change the behavior to whatever you need
   */
  @Override
  public void inputUpdated(String input) {
    if (input.equals("surprise")) {
      outputPanel.appendOutput("You found me!");
    }
  }

    /**
     * Request a guess from the server
     */

    public static OutputStream out;
    public static InputStream in;

  public static void main(String[] args) throws IOException {
    // create the frame
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
    Socket socket;

    try {
      System.out.println("Connected to server: " + host + ":" + port);

      socket = new Socket(host, port);
      out = socket.getOutputStream();
      in = socket.getInputStream();



      //figure out some godforsaken way to send the output to the output panel

    } catch (IOException e) {
      System.err.println("Failed to connect to server: " + host + ":" + port);
      e.printStackTrace();
    }

    ClientGui main = new ClientGui();



    // setup the UI to display on image
    main.newGame(1);

    // add images to the grid
    main.insertImage("img/hi.png", 0, 0);




    // show the GUI dialog as modal
    main.show(true); // you should not have your logic after this. You main logic should happen whenever "submit" is clicked





  }

  public void sendToOutput(String message){
    outputPanel.appendOutput(message);
  }
}
