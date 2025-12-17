package Model;

import java.util.ArrayList;

public class Student extends User {

    private ArrayList<String> enrolledCourses = new ArrayList<>();

    public Student(String name, String email, String password) {
        super(name, email, password, "Student");
    }


    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollCourse(String courseName){
        if(!enrolledCourses.contains(courseName)){
            enrolledCourses.add(courseName);
        }
    }
}