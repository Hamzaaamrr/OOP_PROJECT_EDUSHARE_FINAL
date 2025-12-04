package GUI;

import Service.*;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class ProfessorPanel extends JPanel {

    private MainFrame parent;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private java.util.ArrayList<Model.Course> myCourses;

    public ProfessorPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Professor Dashboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8,8));
        center.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel listLabel = new JLabel("Your Courses");
        listLabel.setFont(listLabel.getFont().deriveFont(Font.BOLD, 14f));
        center.add(listLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFixedCellHeight(36);
        center.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.add(new JLabel("Course Name:"));
        JTextField nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Course Code:"));
        JTextField codeField = new JTextField();
        form.add(codeField);

        JButton createBtn = new JButton("Create Course");
        JButton openBtn = new JButton("Open");
        form.add(createBtn);
        form.add(openBtn);

        south.add(form, BorderLayout.CENTER);
        JButton logoutBtn = new JButton("Logout");
        south.add(logoutBtn, BorderLayout.SOUTH);

        center.add(south, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            User u = UserManager.getCurrentUser();
            if (u == null || !"Professor".equalsIgnoreCase(u.getRole())) {
                JOptionPane.showMessageDialog(parent, "You must be logged in as a professor to create courses.");
                return;
            }
            if (name.isEmpty() || code.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please enter both name and code.");
                return;
            }
            CourseManager cm = new CourseManager();
            Course course = new Course(name, code, u.getEmail());
            boolean ok = cm.addCourse(course);
            if (ok) {
                JOptionPane.showMessageDialog(parent, "Course created successfully.");
                nameField.setText(""); codeField.setText("");
                refreshList();
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to create course (duplicate code?).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        openBtn.addActionListener(e -> openSelectedCourse());

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openSelectedCourse();
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            parent.getUserManager().logout();
            parent.show(MainFrame.LOGIN);
        });

        refreshList();
    }

    public void refreshList() {
        listModel.clear();
        myCourses = new java.util.ArrayList<>();
        User u = UserManager.getCurrentUser();
        if (u == null || !(u instanceof Professor)) return;
        java.util.ArrayList<Course> all = FileHandling.read("courses");
        for (Course c : all) {
            if (c.getProfessorEmail() != null && c.getProfessorEmail().equalsIgnoreCase(u.getEmail())) {
                myCourses.add(c);
                listModel.addElement(c.getCode() + " - " + c.getName());
            }
        }
    }

    private void openSelectedCourse() {
        int idx = list.getSelectedIndex();
        if (idx >= 0 && myCourses != null && idx < myCourses.size()) {
            Course c = myCourses.get(idx);
            parent.openCoursePage(c.getCode());
        } else {
            JOptionPane.showMessageDialog(parent, "Please select a course to open.");
        }
    }
}
