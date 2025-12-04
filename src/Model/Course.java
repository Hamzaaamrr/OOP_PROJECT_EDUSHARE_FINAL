package Model;

import java.io.Serializable;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String code;
    private String professorEmail;

    public Course(String name, String code, String professorEmail) {
        this.name = name;
        this.code = code;
        this.professorEmail = professorEmail;
    }  

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public String getProfessorEmail() {
        return professorEmail;
    }

    public void displayInfo(){
        System.out.println("Course Name: " + name);
        System.out.println("Course Code: " + code);
        System.out.println("Professor Email: " + professorEmail);
    }
}
