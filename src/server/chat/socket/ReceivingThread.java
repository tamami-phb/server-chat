package server.chat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author tamami
 */
public class ReceivingThread extends Thread {
    
    private BufferedReader input;
    private MessageListener messageListener;
    private boolean keepListening = true;

    public ReceivingThread(MessageListener listener, Socket clientSocket) {
        super("ReceivingThread: " + clientSocket);
        messageListener = listener;

        try {
            clientSocket.setSoTimeout(5000);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        String message;

        while(keepListening) {
            try {
                message = input.readLine();
            } catch(InterruptedIOException iioe) {
                continue;
            } catch(IOException ioe) {
                ioe.printStackTrace();
                break;
            }

            if(message != null) {
                StringTokenizer tokenizer = new StringTokenizer(message,
                        SocketMessengerConstants.MESSAGE_SEPARATOR);

                if(tokenizer.countTokens() == 2) {
                    messageListener.messageReceived(tokenizer.nextToken(), tokenizer.nextToken());
                } else {
                    if(message.equalsIgnoreCase(
                       SocketMessengerConstants.MESSAGE_SEPARATOR + SocketMessengerConstants.DISCONNECT_STRING))
                        stopListening();
                }
            }
        }

        try {
            input.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void stopListening() {
        keepListening = false;
    }
    
}
