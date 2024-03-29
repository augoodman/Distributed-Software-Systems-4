import java.io.*;
import java.net.*;

class ThreadedServer extends Thread{
    private static int id = 0;
    private Socket sock;
    StringList strings = new StringList();

    public ThreadedServer(Socket sock, int id){
        this.sock = sock;
        this.id = id;
    }

    public void run(){
        Performer performer = new Performer(sock, strings);
        performer.doPerform();
    }

    public static void main(String args[]) throws Exception {
        Socket sock = null;
        try {
            if (args.length != 1) {
                System.out.println("Usage: ThreadedServer <port>");
                System.exit(1);
            }
            int port = Integer.parseInt(args[0]);
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server Started...");
            while (true) {
                System.out.println("Accepting a Request...");
                sock = server.accept();
                System.out.println("Threaded server connected to client: " + id);
                id++;
                ThreadPoolServer ts = new ThreadPoolServer(sock, id);
                ts.start();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sock !=null) sock.close();
        }
    }
}



