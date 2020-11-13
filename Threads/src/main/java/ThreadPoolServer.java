import java.io.*;
import java.net.*;

class ThreadPoolServer extends Thread{
    private static int id = 0;
    private static volatile int pool;
    private Socket sock;
    StringList strings = new StringList();

    public ThreadPoolServer(Socket sock, int id){
        this.sock = sock;
        this.id = id;
    }

    public void run(){
        int thisID = id;
        Performer performer = new Performer(sock, strings);
        performer.doPerform();
        pool++;
        System.out.println("Client: " + thisID + " disconnected.");
    }

    public static void main(String args[]) throws Exception {
        Socket sock = null;
        try {
            if (args.length != 2) {
                System.out.println("Usage: ThreadPoolServer <port> <thread>");
                System.exit(1);
            }
            int port = Integer.parseInt(args[0]);
            pool = Integer.parseInt(args[1]);
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server Started...");
            if(pool < 0){
                try {
                    StringList strings = new StringList();
                    System.out.println("Accepting Unthreaded Request...");
                    sock = server.accept();
                    Performer performer = new Performer(sock, strings);
                    performer.doPerform();
                    System.out.println("Done!");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    sock.close();
                    System.exit(0);
                }
            }
            while (pool > 0) {
                System.out.println("Accepting a Request...");
                sock = server.accept();
                System.out.println("Threaded server connected to client: " + id);
                id++;
                ThreadPoolServer ts = new ThreadPoolServer(sock, id);
                ts.start();
                pool--;
                if(pool == 0) System.out.println("Thread pool depleted. Waiting for open thread...");
                while (pool == 0) {
                    Thread.onSpinWait();
                }
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



