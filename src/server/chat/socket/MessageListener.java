package server.chat.socket;

/**
 *
 * @author tamami
 */
public interface MessageListener {
    
    public void messageReceived(String from, String message);
    
}
