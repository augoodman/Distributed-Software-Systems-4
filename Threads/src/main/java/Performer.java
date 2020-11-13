import java.net.*;
import java.io.*;

class Performer {

    StringList  state;
    Socket      sock;

    public Performer(Socket sock, StringList strings) {
        this.sock = sock;
        this.state = strings;
    }

    public void doPerform() {

        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            out.println("Choose a function (. to disconnect):");

            boolean done = false;
            while (!done) {
                out.println("1. add a string");
                out.println("2. remove a string by index");
                out.println("3. display the list");
                out.println("4. display a count of each string's size");
                out.println("5. reverse a string by index");
                String str = in.readLine();

                if (str == null || str.equals("."))
                    done = true;
                else if (str.equals("1")){
                    out.println("Enter a string to add.");
                    str = in.readLine();
                    state.add(str);
                    out.println("Server state is now: " + state.toString());
                }
                else if (str.equals("2")){
                    out.println("Enter an index to remove.");
                    str = in.readLine();
                    if (state.remove(Integer.parseInt(str))) {
                        out.println("String removed. Server state is now: " + state.toString());
                    }
                    else {
                        out.println((char[]) null);
                        out.println("Index invalid. Server state is: " + state.toString());
                    }
                }
                else if (str.equals("3")){
                    out.println("List Display: " + state.toString());
                }
                else if (str.equals("4")){
                    out.println("List Count: " + state.size());
                }
                else if (str.equals("5")){
                    out.println("Enter an index to reverse.");
                    str = in.readLine();
                    if (state.reverse(Integer.parseInt(str))) {
                        out.println("String reversed. Server state is now: " + state.toString());
                    }
                    else {
                        out.println((char[]) null);
                        out.println("Index invalid. Server state is: " + state.toString());
                    }
                }
                else {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            try {
                in.close();
            } catch (IOException e) {e.printStackTrace();}
            try {
                sock.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}

