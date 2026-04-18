import java.util.Date;

public class Message {
    String senderId;
    String receiverId;
    String content;
    Date timestamp;

    public Message(String senderId, String receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        // Added timestamp for Bonus requirement
        this.timestamp = new Date();
    }
    
    @Override
    public String toString() {
        return "[" + timestamp.toString() + "] " + senderId + ": " + content;
    }
}
