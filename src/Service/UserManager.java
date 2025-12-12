package Service;

import Model.*;

import java.util.ArrayList;

public class UserManager {

    private ArrayList<User> users;
    private static User currentUser;

    public UserManager(){
        this.users = FileHandling.read("users");
        if (this.users == null) this.users = new ArrayList<>();
    }

    public boolean login(String user, String pass) {
        for (User u : users) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(user) &&
                    u.getPassword() != null && u.getPassword().equals(pass)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password, String role) {
        // Basic validation
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            return false;
        }

        // Check if username exists
        for (User u : users) {
            if (u.getEmail() != null && u.getEmail().equals(username)) {
                return false; // already exists
            }
        }

        // Create new user as correct subclass
        if (role.equalsIgnoreCase("Student")) {
            Student s = new Student(username, password);
            users.add(s);
        } else if (role.equalsIgnoreCase("Professor")) {
            Professor p = new Professor(username, password);
            users.add(p);
        } else {
            // unsupported role
            return false;
        }

        // Save updated list
        FileHandling.write("users", users);

        return true;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    
    // Enroll current logged-in student in a course (updates in-memory and writes to file)
    public boolean enrollCurrentUserInCourse(String courseCode) {
        if (!(currentUser instanceof Student)) return false;
        Student s = (Student) currentUser;
        if (s.getEnrolledCourses().contains(courseCode)) return false;
        s.enrollCourse(courseCode);
        // persist users list (enrollments are stored on Student objects)
        ArrayList<User> all = FileHandling.read("users");
        if (all == null) all = new ArrayList<>();
        // update entry
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            User u = all.get(i);
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(s.getEmail())) {
                all.set(i, s);
                found = true;
                break;
            }
        }
        if (!found) all.add(s);
        FileHandling.write("users", all);
        return true;
    }

    // Logout the current user
    public void logout() {
        currentUser = null;
    }

    

}

