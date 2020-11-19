import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

/**
 * This is the main class for the peer2peer program.
 * It starts a client with a username and port. Next the peer can decide who to listen to.
 * So this peer2peer application is basically a subscriber model, we can "blurt" out to anyone who wants to listen and
 * we can decide who to listen to. We cannot limit in here who can listen to us. So we talk publicly but listen to only the other peers
 * we are interested in.
 *
 */

public class Peer {
    private static String username;
    private BufferedReader bufferedReader;
    public static ServerThread serverThread;
    private boolean hostFlag = true;
    private int playerCount = 1;
    private static JSONArray ja;
    private static List<JSONObject> joList = new ArrayList<JSONObject>();
    public static boolean isHost = true;
    private static boolean isClient = false;
    public static String answer = " ";
    public static String guess = null;
    private static int points = 0;
    private static boolean firstGame = true;
    public static boolean gameOver = false;
    public static boolean winner = false;

    public Peer(BufferedReader bufReader, String username, ServerThread serverThread){
        this.username = username;
        this.bufferedReader = bufReader;
        this.serverThread = serverThread;
    }
    /**
     * Main method saying hi and also starting the Server thread where other peers can subscribe to listen
     *
     */
    public static void main (String[] args) throws Exception {
        FileInputStream in = new FileInputStream("src/main/resources/data.json");
        ja = new JSONArray(new JSONTokener(in));
        for(int i = 0; i < 15; i++){
            joList.add(ja.getJSONObject(i));
            //System.out.println(joList.get(i));
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String username = args[0];
        System.out.println("Hello " + username + " and welcome! Your port will be " + args[1]);

        // starting the Server Thread, which waits for other peers to want to connect
        ServerThread serverThread = new ServerThread(args[1]);
        serverThread.start();
        Peer peer = new Peer(bufferedReader, args[0], serverThread);
        peer.updateListenToPeers();
    }

    /**
     * User is asked to define who they want to subscribe/listen to
     * Per default we listen to no one
     *
     */
    public void updateListenToPeers() throws Exception {
        System.out.println("> Who do you want to listen to? Enter host:port");
        String input = bufferedReader.readLine();
        String[] setupValue = input.split(" ");
        playerCount += setupValue.length;
        System.out.println("Number of players: " + playerCount);
        for (int i = 0; i < setupValue.length; i++) {
            String[] address = setupValue[i].split(":");
            Socket socket = null;
            try {
                socket = new Socket(address[0], Integer.valueOf(address[1]));
                new ClientThread(socket).start();
            } catch (Exception c) {
                if (socket != null) {
                    socket.close();
                } else {
                    System.out.println("Cannot connect, wrong input");
                    System.out.println("Exiting: I know really user friendly");
                    System.exit(0);
                }
            }
        }

        askForInput();
    }

    /**
     * Client waits for user to input their message or quit
     *
     */
    public void askForInput() throws Exception {
        try {
            if(firstGame) System.out.println("> You can now start chatting (exit to exit). If you type 'start' your game will start with your peers:");
            else System.out.println("Back to main loop: chat or type 'start' to start a game");
            //firstGame = false;
            while(true) {
                String message = bufferedReader.readLine();
                if (message.equals("exit")) {
                    System.out.println("bye, see you next time");
                    break;
                }
                else if(message.equalsIgnoreCase("start")){
                    if(!firstGame && winner){
                        winner = false;
                        runHost();
                    }
                    else if(!firstGame && !winner) runClient();
                    System.out.println("You are almost ready to start....");
                    System.out.println("Type 1 if you are the agreed upon host or 0 if you are a pawn");
                    message = bufferedReader.readLine();
                    while(!message.equals("0") && !message.equals("1")){
                        System.out.println("Invalid input. Type 1 if you are the agreed upon host or 0 if you are a pawn");
                    }
                    if(message.equals("1")) runHost();
                    else runClient();
                }
                else {
                    // we are sending the message to our server thread. this one is then responsible for sending it to listening peers
                    serverThread.sendMessage("{'username': '"+ username +"','message':'" + message + "'}");
                }
            }
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Logic for implementing host logic
     *
     */
    private void runHost() throws Exception {
        try {

            System.out.println("\n\n\n################");
            System.out.println("NEW ROUND");
            System.out.println("################\n\n\n");
            System.out.println("   #");
            System.out.println("  ###");
            System.out.println(" #####");
            System.out.println("#######");
            System.out.println("You are Host");
            System.out.println("Starting our question round:");
            System.out.println("Type 'yes' if you are ready to send your question:");
            while(guess == null && !gameOver) {
                String message = bufferedReader.readLine();
                if (message.equalsIgnoreCase("yes")) {
                    JSONObject jo = joList.remove((int) (Math.random() * 15));
                    String question = jo.getString("question");
                    answer = jo.getString("answer");
                    System.out.println("[ question ]: " + question);
                    serverThread.sendMessage("{'username': '" + username + "','question': '" + question + "','answer':'" + answer + "'}");
                } else if (message.equalsIgnoreCase("exit")) {
                    System.out.println("bye, see you next time");
                    System.exit(0);
                }
            }
            guess = null;
            if(gameOver){
                gameOver = false;
                firstGame = false;
                askForInput();
            }
            runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logic for implementing client logic
     *
     */
    public void runClient() throws Exception {
        //try {
            System.out.println("\n\n\n################");
            System.out.println("NEW ROUND");
            System.out.println("################\n\n\n");
            System.out.println("################");
            System.out.println("You are a Pawn");
            System.out.println("Starting our question round:");
            System.out.println("Wait, your host is selecting a question...");
        while(true) {
            String guess = bufferedReader.readLine();
            if (!guess.equalsIgnoreCase("exit")) {
                //String guess = bufferedReader.readLine();
                serverThread.sendMessage("{'username': '"+ username +"','guess':'" + guess + "'}");
                if(guess.equalsIgnoreCase(answer)){
                    System.out.println("Yeah correct");
                    points++;
                    if(points == 3){
                        System.out.println("I WON I WON I WON I WON");
                        serverThread.sendMessage("{'username': '"+ username +"','message':'I WON I WON I WON I WON'}");
                        firstGame = false;
                        winner = true;
                        askForInput();
                    }
                    break;
                }
                else System.out.println("Nope");
            }
            else {
                System.out.println("bye, see you next time");
                System.exit(0);
            }
        }
        runHost();
    }
}
