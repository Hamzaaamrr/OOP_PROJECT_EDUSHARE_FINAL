package Model;

import java.io.Serializable;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String name;
    protected String email;
    protected String password;
    protected String role; //student or professor

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Backwards-compatible: default name empty
    public User(String email, String password, String role) {
        this("", email, password, role);
    }

    

    //getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    // Prefer display name; fall back to email
    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) return name.trim();
        return email == null ? "" : email;
    }

    public void displayInfo(){
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Role: "+ role);
    }





}
