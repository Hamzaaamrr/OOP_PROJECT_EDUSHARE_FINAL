package Model;

public class Professor extends User {

    public Professor(String name, String email, String password) {
        super(name, email, password, "Professor");
    }

    // Backwards-compatible constructor
    public Professor(String email, String password) {
        this("", email, password);
    }
}
