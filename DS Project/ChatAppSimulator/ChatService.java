import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

// Backend logic class
public class ChatService {

    // HashMap → stores users (fast lookup O(1))
    HashMap<String, User> users;

    // Queue → stores messages temporarily (FIFO)
    Queue<Message> messageQueue;

    // HashMap + LinkedList → stores chat history
    HashMap<String, LinkedList<Message>> chatHistories;

    // Root of group tree
    Group rootGroup;

    // Constructor
    public ChatService() {
        users = new HashMap<>();           // initialize users
        messageQueue = new LinkedList<>(); // queue using linked list
        chatHistories = new HashMap<>();   // history map
        rootGroup = new Group("Global");   // root group
    }

    // Add user
    public void addUser(String userId, String name) {

        User newUser = new User(userId, name); // create user

        users.put(userId, newUser); // store in HashMap

        System.out.println("User added: " + name + " (ID: " + userId + ")");
    }

    // Send message → goes to queue
    public void sendMessage(String senderId, String receiverId, String content) {

        // Check sender exists
        if (!users.containsKey(senderId)) {
            System.out.println("Error: Sender not found.");
            return;
        }

        // Check receiver exists
        if (!users.containsKey(receiverId)) {
            System.out.println("Error: Receiver not found.");
            return;
        }

        // Create message
        Message newMessage = new Message(senderId, receiverId, content);

        // Add to queue
        messageQueue.add(newMessage);

        System.out.println("Message queued from " + senderId + " to " + receiverId);
    }

    // Deliver messages from queue
    public void deliverMessages() {

        System.out.println("\n--- Delivering Messages ---");

        // If queue empty
        if (messageQueue.isEmpty()) {
            System.out.println("No messages to deliver.");
            return;
        }

        // Process all messages
        while (!messageQueue.isEmpty()) {

            Message msg = messageQueue.poll(); // remove first message

            String key = generateConversationKey(msg.senderId, msg.receiverId);

            // If no history exists
            if (!chatHistories.containsKey(key)) {
                chatHistories.put(key, new LinkedList<>());
            }

            // Store in LinkedList history
            chatHistories.get(key).add(msg);

            System.out.println("Delivered: " + msg.content);
        }
    }

    // View chat history
    public void viewChatHistory(String u1, String u2) {

        String key = generateConversationKey(u1, u2);

        if (!chatHistories.containsKey(key)) {
            System.out.println("No chat history found.");
            return;
        }

        for (Message m : chatHistories.get(key)) {
            System.out.println(m);
        }
    }

    // Generate unique key for conversation
    private String generateConversationKey(String u1, String u2) {

        if (u1.compareTo(u2) < 0) {
            return u1 + "_" + u2;
        }

        return u2 + "_" + u1;
    }

    // Create group
    public void createGroup(String parentName, String newGroupName) {

    // If parent is empty → use Global
    if (parentName == null || parentName.trim().isEmpty()) {
        parentName = "Global";
    }

    // Find parent group
    Group parentGroup = rootGroup.findGroup(parentName);

    if (parentGroup == null) {
        System.out.println("Error: Parent group '" + parentName + "' not found.");
        return;
    }

    // Check duplicate group
    if (rootGroup.findGroup(newGroupName) != null) {
        System.out.println("Error: Group already exists.");
        return;
    }

    // Create new group
    Group newGroup = new Group(newGroupName);

    // Add under parent
    parentGroup.addSubGroup(newGroup);

    System.out.println("Group created: " + newGroupName + " (Under " + parentName + ")");
   }
    // Add user to group
    public void addUserToGroup(String userId, String groupName) {

        if (!users.containsKey(userId)) {
            System.out.println("User not found");
            return;
        }

        Group group = rootGroup.findGroup(groupName);

        if (group == null) {
            System.out.println("Group not found");
            return;
        }

        group.addUser(users.get(userId));
    }

    // Print tree
    public void printGroupTree() {
        rootGroup.displayHierarchy("");
    }
}