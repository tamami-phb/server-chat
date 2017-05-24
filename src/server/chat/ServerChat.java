package server.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.chat.socket.MessageListener;
import server.chat.socket.MulticastSendingThread;
import server.chat.socket.ReceivingThread;
import server.chat.socket.SocketMessengerConstants;

/**
 *
 * @author tamami
 */
public class ServerChat implements MessageListener {
    
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(SocketMessengerConstants.SERVER_PORT, 100);
            System.out.println("Server menunggu di port " +
                SocketMessengerConstants.SERVER_PORT + " ...");

            while(true) {
                Socket clientSocket = serverSocket.accept();
                new ReceivingThread(this, clientSocket).start();
                System.out.println("Connection received from: " + clientSocket.getInetAddress());
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void messageReceived(String from, String message) {
        String completeMessage = from + SocketMessengerConstants.MESSAGE_SEPARATOR + message;
        new MulticastSendingThread(completeMessage.getBytes()).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ServerChat().startServer();
    }
    
}
