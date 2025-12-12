package GUI;

import Service.*;
import Model.Course;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cards;
    private JPanel container;
    private UserManager userManager;
    private LoginPanel loginPanel;
    private SignupPanel signupPanel;
    private StudentPanel studentPanel;
    private CoursesPanel coursesPanel;
    private ProfessorPanel professorPanel;
    private CoursePagePanel coursePagePanel;
    private AddMaterialPanel addMaterialPanel;
    private Service.MaterialManager materialManager;
    private Service.CommentManager commentManager;

    public static final String LOGIN = "login";
    public static final String SIGNUP = "signup";
    public static final String STUDENT = "student";
    public static final String PROFESSOR = "professor";
    public static final String COURSES = "courses";
    public static final String COURSE_PAGE = "course_page";
    public static final String ADD_MATERIAL = "add_material";

    public MainFrame() {
        super("EDUSHARE");

        // Try to set FlatLaf (if available) for a modern appearance, fall back to Nimbus
        try {
            try {
                // Attempt FlatLaf if on classpath
                Class<?> flat = Class.forName("com.formdev.flatlaf.FlatLightLaf");
                flat.getMethod("install").invoke(null);
            } catch (ClassNotFoundException cnf) {
                // FlatLaf not available — try Nimbus
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            // ignore and continue with default
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setMinimumSize(new Dimension(720, 480));
        setLocationRelativeTo(null);

        // Tweak default fonts to improve readability
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("List.font", defaultFont);

        userManager = new UserManager();
        materialManager = new Service.MaterialManager();
        commentManager = new Service.CommentManager();

        // Serialization-only persistence in place; legacy CSV migration removed

        cards = new CardLayout();
        container = new JPanel(cards);
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        container.setBackground(new Color(249, 250, 251));

        loginPanel = new LoginPanel(this);
        signupPanel = new SignupPanel(this);
        studentPanel = new StudentPanel(this);
        coursesPanel = new CoursesPanel(this);
        coursePagePanel = new CoursePagePanel(this);
        addMaterialPanel = new AddMaterialPanel(this);
        professorPanel = new ProfessorPanel(this);

        container.add(loginPanel, LOGIN);
        container.add(signupPanel, SIGNUP);
        container.add(studentPanel, STUDENT);
        container.add(professorPanel, PROFESSOR);
        container.add(coursesPanel, COURSES);
        container.add(coursePagePanel, COURSE_PAGE);
        container.add(addMaterialPanel, ADD_MATERIAL);

        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);
    }

    public void show(String name) {
        cards.show(container, name);
        // refresh views when shown
        if (COURSES.equals(name) && coursesPanel != null) {
            coursesPanel.refreshCourses();
        }
        if (STUDENT.equals(name) && studentPanel != null) {
            studentPanel.refreshList();
        }
        if (PROFESSOR.equals(name) && professorPanel != null) {
            professorPanel.refreshList();
        }
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Service.MaterialManager getMaterialManager() {
        return materialManager;
    }

    public Service.CommentManager getCommentManager() {
        return commentManager;
    }

    public void openCoursePage(String courseCode) {
        // Attempt to find the course by code and set the title on the course page
        try {
            java.util.ArrayList<Course> courses = FileHandling.read("courses");
            for (Course c : courses) {
                if (c.getCode().equalsIgnoreCase(courseCode)) {
                    coursePagePanel.setCourse(c.getCode(), c.getCode() + " - " + c.getName());
                    show(COURSE_PAGE);
                    return;
                }
            }
        } catch (Exception ex) {
            // fallback: show code only
        }
        coursePagePanel.setCourse(courseCode, courseCode);
        show(COURSE_PAGE);
    }

    public void openAddMaterialPage(String courseCode) {
        addMaterialPanel.setCourseCode(courseCode);
        show(ADD_MATERIAL);
    }
}
