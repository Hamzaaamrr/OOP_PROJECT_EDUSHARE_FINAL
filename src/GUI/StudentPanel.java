package GUI;

import Service.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentPanel extends JPanel {

    private MainFrame parent;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JButton openBtn;

    public StudentPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Your Enrolled Courses");
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFixedCellHeight(36);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(BorderFactory.createEmptyBorder(6,8,6,8));
                return lbl;
            }
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createLineBorder(Color.decode("#e6eef6")));
        center.add(new JScrollPane(list), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton refreshBtn = new JButton("Refresh");
        JButton viewCoursesBtn = new JButton("View All Courses");
        openBtn = new JButton("Open");
        JButton logoutBtn = new JButton("Logout");
        refreshBtn.setBackground(Color.decode("#f3f4f6"));
        viewCoursesBtn.setBackground(Color.decode("#2b7cff"));
        viewCoursesBtn.setForeground(Color.white);
        logoutBtn.setBackground(Color.decode("#ff6b6b"));
        logoutBtn.setForeground(Color.white);
        buttons.add(refreshBtn);
        buttons.add(viewCoursesBtn);
        buttons.add(openBtn);
        buttons.add(logoutBtn);
        add(buttons, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshList());
        viewCoursesBtn.addActionListener(e -> parent.show(MainFrame.COURSES));
        openBtn.addActionListener(e -> openSelectedCourse());
        logoutBtn.addActionListener(e -> {
            parent.getUserManager().logout();
            parent.show(MainFrame.LOGIN);
        });

        // Double-click to open selected course
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openSelectedCourse();
                }
            }
        });
    }

    private void openSelectedCourse() {
        int idx = list.getSelectedIndex();
        if (idx >= 0) {
            String courseCode = listModel.getElementAt(idx);
            parent.openCoursePage(courseCode);
        } else {
            JOptionPane.showMessageDialog(parent, "Please select a course to open.");
        }
    }

    public void refreshList() {
        listModel.clear();
        User u = UserManager.getCurrentUser();
        if (u instanceof Student) {
            Student s = (Student) u;
            ArrayList<String> courses = s.getEnrolledCourses();
            for (String c : courses) listModel.addElement(c);
        }
    }
}
