import java.util.ArrayList;
import java.util.List;

public class Group {
    String groupName;
    
    // Using Tree structure: Each group can have multiple subgroups (children)
    // List is used to hold the child nodes in the tree hierarchy
    List<Group> subGroups;
    
    // List to store the members in the specific group node
    List<User> members;

    public Group(String groupName) {
        this.groupName = groupName;
        // avoid using "this" keyword where not absolutely needed
        subGroups = new ArrayList<>();
        members = new ArrayList<>();
    }

    public void addSubGroup(Group group) {
        subGroups.add(group);
    }

    public void addUser(User user) {
        members.add(user);
    }

    // Method to display tree structure (hierarchy) recursively
    public void displayHierarchy(String prefix) {
        System.out.println(prefix + "- Group: " + groupName);

        for (User u : members) {
            System.out.println(prefix + "  * Member: " + u.name);
        }

        // Recursive call to display child nodes in the tree
        for (Group subGroup : subGroups) {
            subGroup.displayHierarchy(prefix + "  ");
        }
    }
}
