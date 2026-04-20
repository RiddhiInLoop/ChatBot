import java.util.ArrayList;
import java.util.List;

// Class representing a group (Tree node)
public class Group {

    String groupName; // name of the group
    
    // List of child groups (Tree structure)
    List<Group> subGroups;
    
    // List of users in this group
    List<User> members;

    // Constructor
    public Group(String groupName) {
        this.groupName = groupName; // assign name

        // Initialize lists
        subGroups = new ArrayList<>(); // child nodes
        members = new ArrayList<>();   // users
    }

    // Add a subgroup (child node in tree)
    public void addSubGroup(Group group) {
        subGroups.add(group); // add child group
    }

    // Add user to group
    public void addUser(User user) {
        members.add(user); // add user
    }

    // Display hierarchy using recursion
    public void displayHierarchy(String prefix) {

        // Print current group
        System.out.println(prefix + "- Group: " + groupName);

        // Print members of this group
        for (User u : members) {
            System.out.println(prefix + "  * Member: " + u.name);
        }

        // Recursively print all subgroups
        for (Group subGroup : subGroups) {
            subGroup.displayHierarchy(prefix + "  ");
        }
    }

    // Find group by name (recursive search)
    public Group findGroup(String targetName) {

        // If current group matches
        if (this.groupName.equals(targetName)) {
            return this;
        }

        // Search in children
        for (Group subGroup : subGroups) {
            Group found = subGroup.findGroup(targetName);

            if (found != null) {
                return found; // return if found
            }
        }

        return null; // not found
    }
}