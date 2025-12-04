package Service;

import java.util.ArrayList;
import java.util.List;

import Model.Course;

public class CourseManager {
    
    private static List<Course> courses = new ArrayList<>();

    public CourseManager() {
        ArrayList<Course> loaded = FileHandling.read("courses");
        if (loaded != null) CourseManager.courses = loaded;
    }

    public boolean addCourse(Course course) {
        if (course == null) return false;
        // ensure code uniqueness
        for (Course c : courses) {
            if (c.getCode().equalsIgnoreCase(course.getCode())) return false;
        }
        courses.add(course);
        // persist
        ArrayList<Course> list = new ArrayList<>(courses);
        return FileHandling.write("courses", list);
    }

    public List<Course> getCourses() {
        return courses;
    }


}
