import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChatService chatService = new ChatService();
        
        // Setting up a root group to demonstrate the tree structure dynamically
        Group rootGroup = new Group("Organization Root");

        System.out.println("===============================================");
        System.out.println("  Welcome to the Chat Application Simulator    ");
        System.out.println("===============================================\n");

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add a new user");
            System.out.println("2. Queue a message to send");
            System.out.println("3. Deliver queued messages");
            System.out.println("4. View chat history between two users");
            System.out.println("5. Build a group hierarchy test (Hardcoded demo)");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter new User ID (e.g., u1): ");
                    String id = scanner.nextLine();
                    System.out.print("Enter User Name (e.g., Alice): ");
                    String name = scanner.nextLine();
                    chatService.addUser(id, name);
                    break;
                case "2":
                    System.out.print("Enter Sender ID: ");
                    String sender = scanner.nextLine();
                    System.out.print("Enter Receiver ID: ");
                    String receiver = scanner.nextLine();
                    System.out.print("Enter Message Content: ");
                    String content = scanner.nextLine();
                    chatService.sendMessage(sender, receiver, content);
                    break;
                case "3":
                    chatService.deliverMessages();
                    break;
                case "4":
                    System.out.print("Enter User ID 1: ");
                    String u1 = scanner.nextLine();
                    System.out.print("Enter User ID 2: ");
                    String u2 = scanner.nextLine();
                    chatService.viewChatHistory(u1, u2);
                    break;
                case "5":
                    System.out.println("\n--- Building Organization Groups (Tree Structure) ---");
                    Group engineering = new Group("Engineering (Child)");
                    Group backendTeam = new Group("Backend (Grandchild)");
                    
                    rootGroup.addSubGroup(engineering);
                    engineering.addSubGroup(backendTeam);
                    
                    rootGroup.displayHierarchy("");
                    System.out.println("-----------------------------------------------------");
                    break;
                case "6":
                    System.out.println("Exiting Simulator. Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}
