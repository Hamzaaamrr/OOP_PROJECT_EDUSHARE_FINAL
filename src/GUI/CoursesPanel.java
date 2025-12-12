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
        setLayout(new BorderLayout(12,12));
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        JLabel title = new JLabel("Available Courses");
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(31, 41, 55));
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFixedCellHeight(52);
        list.setBackground(Color.white);
        list.setSelectionBackground(new Color(224, 231, 255));
        list.setSelectionForeground(new Color(79, 70, 229));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton enrollBtn = new JButton("Enroll in Course");
        enrollBtn.setBackground(new Color(79, 70, 229));
        enrollBtn.setForeground(Color.white);
        enrollBtn.setFocusPainted(false);
        enrollBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enrollBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.white);
        backBtn.setForeground(new Color(107, 114, 128));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
            String profName = Service.UserManager.getDisplayNameForEmail(c.getProfessorEmail());
            listModel.addElement(c.getCode() + " - " + c.getName() + " (" + profName + ")");
        }
    }
}
