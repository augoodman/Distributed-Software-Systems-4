import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Iterator;


/**
 * Client
 * This is the Client thread class, there is a client thread for each peer we are listening to.
 * We are constantly listening and if we get a message we print it.
 */

public class ClientThread extends Thread {
    private BufferedReader bufferedReader;
    static String username = null;
    static String message = null;
    static String question = null;
    static String answer = null;
    static String guess = null;
    static String system = null;
    public ClientThread(Socket socket) throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public void run() {
        while (true) {
            try {
                JSONObject json = new JSONObject(bufferedReader.readLine());
                Iterator<?> keys = json.keys();
                //add read in json
                while(keys.hasNext()){
                    String key = (String)keys.next();
                    if(key.equalsIgnoreCase("username")) username = json.getString(key);
                    else if(key.equalsIgnoreCase("message")) message = json.getString(key);
                    else if(key.equalsIgnoreCase("question")) question = json.getString(key);
                    else if(key.equalsIgnoreCase("answer")) answer = json.getString(key);
                    else if(key.equalsIgnoreCase("guess")) guess = json.getString(key);
                    else if(key.equalsIgnoreCase("system")) system = json.getString(key);
                }
                if(username != null && message != null){
                    System.out.println("[" + username + "]: " + message);
                    if(message.equalsIgnoreCase("I WON I WON I WON I WON")){
                        Peer.gameOver = true;
                        System.out.println("Press enter to continue.");
                    }
                    username = null;
                    message = null;
                    question = null;
                    answer = null;
                    guess = null;
                    system = null;
                }
                else if(question != null && answer != null){
                    Peer.answer = answer;
                    System.out.println("[ question ]: " + question);
                    username = null;
                    message = null;
                    question = null;
                    answer = null;
                    guess = null;
                    system = null;
                }
                else if(username != null && guess != null){
                    System.out.println("[" + username + "]: " + guess);
                    Peer.guess = guess;
                    Peer.isHost = false;
                    if(guess.equalsIgnoreCase(Peer.answer)){
                        System.out.println("Yeah correct. There is a new host! Press 'enter' to continue.");
                        username = null;
                        message = null;
                        question = null;
                        answer = null;
                        guess = null;
                        system = null;
                        //Peer.isHost = false;
                    }
                    else{
                        System.out.println("Nope");
                    }
                }
                else if(system != null) System.out.println("Game is finished");
                } catch (Exception e) {
                interrupt();
                break;
            }
        }
    }

}
