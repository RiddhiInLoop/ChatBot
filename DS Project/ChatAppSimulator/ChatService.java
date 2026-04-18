import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class ChatService {

    // Using HashMap for O(1) user lookup by userId
    HashMap<String, User> users;

    // Using Queue (LinkedList implementation) for message delivery (FIFO)
    // Ensures messages are delivered in the exact order they were sent
    Queue<Message> messageQueue;

    // Using HashMap to map a unique conversation key to its chat history
    // Using LinkedList for storing chat history as per requirement
    HashMap<String, LinkedList<Message>> chatHistories;

    public ChatService() {
        users = new HashMap<>();
        messageQueue = new LinkedList<>();
        chatHistories = new HashMap<>();
    }

    public void addUser(String userId, String name) {
        User newUser = new User(userId, name);
        // Storing in HashMap
        users.put(userId, newUser);
        System.out.println("User added: " + name + " (ID: " + userId + ")");
    }

    public void sendMessage(String senderId, String receiverId, String content) {
        // Checking if users exist using HashMap lookup
        if (!users.containsKey(senderId)) {
            System.out.println("Error: Sender " + senderId + " not found.");
            return;
        }
        if (!users.containsKey(receiverId)) {
            System.out.println("Error: Receiver " + receiverId + " not found.");
            return;
        }

        Message newMessage = new Message(senderId, receiverId, content);
        // Adding message to Queue for later delivery
        messageQueue.add(newMessage);
        System.out.println("Message queued from " + senderId + " to " + receiverId);
    }

    public void deliverMessages() {
        System.out.println("\n--- Delivering Messages ---");
        // Handle empty queue safely (Bonus requirement)
        if (messageQueue.isEmpty()) {
            System.out.println("No messages in queue to deliver.");
            System.out.println("---------------------------\n");
            return;
        }

        // Processing Queue (FIFO pattern)
        while (!messageQueue.isEmpty()) {
            Message currentMessage = messageQueue.poll();
            String senderId = currentMessage.senderId;
            String receiverId = currentMessage.receiverId;

            // Generate unique key for the pair to store in HashMap
            String conversationKey = generateConversationKey(senderId, receiverId);

            // If history doesn't exist for this pair, initialize a new LinkedList
            if (!chatHistories.containsKey(conversationKey)) {
                chatHistories.put(conversationKey, new LinkedList<>());
            }

            // Using LinkedList to store chat history sequences
            chatHistories.get(conversationKey).add(currentMessage);
            System.out.println("Delivered: " + currentMessage.content);
        }
        System.out.println("---------------------------\n");
    }

    public void viewChatHistory(String userId1, String userId2) {
        System.out.println("--- Chat History between " + userId1 + " and " + userId2 + " ---");
        String conversationKey = generateConversationKey(userId1, userId2);

        // Fetching history from HashMap
        if (!chatHistories.containsKey(conversationKey) || chatHistories.get(conversationKey).isEmpty()) {
            System.out.println("No chat history found.");
            System.out.println("----------------------------------------------\n");
            return;
        }

        // Processing chat history stored in LinkedList
        LinkedList<Message> history = chatHistories.get(conversationKey);
        for (Message msg : history) {
            System.out.println(msg.toString());
        }
        System.out.println("----------------------------------------------\n");
    }

    // Helper method to ensure user1_user2 and user2_user1 create the same key
    private String generateConversationKey(String u1, String u2) {
        if (u1.compareTo(u2) < 0) {
            return u1 + "_" + u2;
        }
        return u2 + "_" + u1;
    }
}
