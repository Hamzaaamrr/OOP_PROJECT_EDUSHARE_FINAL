package GUI;

import Service.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CoursesPanel extends JPanel {

    private MainFrame parent;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private ArrayList<Course> courses;

    public CoursesPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(8,8));
        JLabel title = new JLabel("Available Courses");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton enrollBtn = new JButton("Enroll");
        JButton backBtn = new JButton("Back");
        buttons.add(enrollBtn);
        buttons.add(backBtn);
        add(buttons, BorderLayout.SOUTH);

        enrollBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx >= 0 && idx < courses.size()) {
                Course c = courses.get(idx);
                boolean ok = parent.getUserManager().enrollCurrentUserInCourse(c.getCode());
                if (ok) {
                    JOptionPane.showMessageDialog(parent, "Enrolled in " + c.getName());
                } else {
                    JOptionPane.showMessageDialog(parent, "Already enrolled or not a student.");
                }
            }
        });

        backBtn.addActionListener(e -> parent.show(MainFrame.STUDENT));
    }

    public void refreshCourses() {
        listModel.clear();
        courses = FileHandling.read("courses");
        for (Course c : courses) {
            listModel.addElement(c.getCode() + " - " + c.getName());
        }
    }
}
