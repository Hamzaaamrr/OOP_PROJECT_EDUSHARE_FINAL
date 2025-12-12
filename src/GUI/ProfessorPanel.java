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
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Professor Dashboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(31, 41, 55));
        header.add(title, BorderLayout.NORTH);

        
        JPanel center = new JPanel(new BorderLayout(10,10));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));

        JLabel listLabel = new JLabel("Your Courses");
        listLabel.setFont(listLabel.getFont().deriveFont(Font.BOLD, 16f));
        listLabel.setForeground(new Color(31, 41, 55));
        center.add(listLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFixedCellHeight(48);
        list.setBackground(Color.white);
        list.setSelectionBackground(new Color(224, 231, 255));
        list.setSelectionForeground(new Color(79, 70, 229));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        center.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 12));
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));
        
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.white);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(16,16,16,16)));
        
        JPanel form = new JPanel(new GridLayout(3,2,12,12));
        form.setOpaque(false);
        JLabel nameLabel = new JLabel("Course Name:");
        nameLabel.setForeground(new Color(55, 65, 81));
        form.add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(6,8,6,8)));
        form.add(nameField);

        JLabel codeLabel = new JLabel("Course Code:");
        codeLabel.setForeground(new Color(55, 65, 81));
        form.add(codeLabel);
        JTextField codeField = new JTextField();
        codeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(6,8,6,8)));
        form.add(codeField);

        JButton createBtn = new JButton("Create Course");
        createBtn.setBackground(new Color(79, 70, 229));
        createBtn.setForeground(Color.white);
        createBtn.setFocusPainted(false);
        createBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton openBtn = new JButton("Open Course");
        openBtn.setBackground(new Color(16, 185, 129));
        openBtn.setForeground(Color.white);
        openBtn.setFocusPainted(false);
        openBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        form.add(createBtn);
        form.add(openBtn);

        formWrapper.add(form, BorderLayout.CENTER);
        south.add(formWrapper, BorderLayout.CENTER);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.white);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
