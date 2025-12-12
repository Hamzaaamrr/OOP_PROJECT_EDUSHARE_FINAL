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
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Your Enrolled Courses");
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(31, 41, 55));
        header.add(title, BorderLayout.NORTH);

        
        

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFixedCellHeight(52);
        list.setBackground(Color.white);
        list.setSelectionBackground(new Color(224, 231, 255));
        list.setSelectionForeground(new Color(79, 70, 229));
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                    BorderFactory.createEmptyBorder(12,16,12,16)));
                lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 14f));
                if (!isSelected) {
                    lbl.setForeground(new Color(31, 41, 55));
                }
                return lbl;
            }
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.white);
        center.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(0,0,0,0)));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        buttons.setBackground(new Color(249, 250, 251));
        JButton refreshBtn = new JButton("Refresh");
        JButton viewCoursesBtn = new JButton("View All Courses");
        openBtn = new JButton("Open Course");
        JButton logoutBtn = new JButton("Logout");
        
        refreshBtn.setBackground(Color.white);
        refreshBtn.setForeground(new Color(107, 114, 128));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        viewCoursesBtn.setBackground(new Color(79, 70, 229));
        viewCoursesBtn.setForeground(Color.white);
        viewCoursesBtn.setFocusPainted(false);
        viewCoursesBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        viewCoursesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        openBtn.setBackground(new Color(16, 185, 129));
        openBtn.setForeground(Color.white);
        openBtn.setFocusPainted(false);
        openBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.white);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
            String selected = listModel.getElementAt(idx);
            // Extract course code from "CODE - NAME" format
            String courseCode = selected.contains(" - ") ? selected.split(" - ")[0] : selected;
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
            ArrayList<String> courseCodes = s.getEnrolledCourses();
            ArrayList<Course> allCourses = FileHandling.read("courses");
            for (String code : courseCodes) {
                Course course = allCourses.stream()
                    .filter(c -> c.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
                if (course != null) {
                    listModel.addElement(course.getCode() + " - " + course.getName());
                } else {
                    listModel.addElement(code);
                }
            }
        }
    }
}
