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


}
