package GUI;

import Service.FileHandling;
import Model.Material;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CoursePagePanel extends JPanel {

    private MainFrame parent;
    private JLabel titleLabel;
    private JButton addMaterialBtn;
    private JButton backBtn;
    private String currentCourseCode;
    private boolean isOwner = false;

    private DefaultListModel<String> materialListModel;
    private JList<String> materialList;
    private ArrayList<Material> materials;
    private JButton deleteBtn;
    private JButton pinBtn;
    private JButton upvoteBtn;
    private JButton downvoteBtn;
    private JButton commentsBtn;

    public CoursePagePanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(12,12));
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        titleLabel = new JLabel("Course Page");
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(new Color(31, 41, 55));
        add(titleLabel, BorderLayout.NORTH);

        materialListModel = new DefaultListModel<>();
        materialList = new JList<>(materialListModel);
        materialList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        materialList.setFixedCellHeight(56);
        materialList.setBackground(Color.white);
        materialList.setSelectionBackground(new Color(224, 231, 255));
        materialList.setSelectionForeground(new Color(79, 70, 229));

        JScrollPane scroll = new JScrollPane(materialList);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        JPanel centerWrap = new JPanel(new BorderLayout(0,10));
        centerWrap.setOpaque(false);
        centerWrap.add(scroll, BorderLayout.CENTER);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topControls.setOpaque(false);
        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setForeground(new Color(55, 65, 81));
        sortLabel.setFont(sortLabel.getFont().deriveFont(Font.PLAIN, 13f));
        String[] options = new String[] {"Newest First", "Oldest First", "Most Upvoted", "Most Downvoted"};
        JComboBox<String> sortBox = new JComboBox<>(options);
        sortBox.setSelectedIndex(0);
        sortBox.setBackground(Color.white);
        sortBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(4,8,4,8)));
        topControls.add(sortLabel);
        topControls.add(sortBox);
        centerWrap.add(topControls, BorderLayout.NORTH);

        add(centerWrap, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        
        addMaterialBtn = new JButton("Add Material");
        addMaterialBtn.setBackground(new Color(79, 70, 229));
        addMaterialBtn.setForeground(Color.white);
        addMaterialBtn.setFocusPainted(false);
        addMaterialBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addMaterialBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        upvoteBtn = new JButton("Upvote");
        upvoteBtn.setBackground(new Color(16, 185, 129));
        upvoteBtn.setForeground(Color.white);
        upvoteBtn.setFocusPainted(false);
        upvoteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        upvoteBtn.setEnabled(false);
        upvoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        downvoteBtn = new JButton("Downvote");
        downvoteBtn.setBackground(new Color(239, 68, 68));
        downvoteBtn.setForeground(Color.white);
        downvoteBtn.setFocusPainted(false);
        downvoteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        downvoteBtn.setEnabled(false);
        downvoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        commentsBtn = new JButton("Comments");
        commentsBtn.setBackground(new Color(59, 130, 246));
        commentsBtn.setForeground(Color.white);
        commentsBtn.setFocusPainted(false);
        commentsBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        commentsBtn.setEnabled(false);
        commentsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteBtn = new JButton("Delete Material");
        deleteBtn.setVisible(false);
        deleteBtn.setEnabled(false);
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setForeground(Color.white);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pinBtn = new JButton("Pin Material");
        pinBtn.setVisible(false);
        pinBtn.setEnabled(false);
        pinBtn.setBackground(new Color(245, 158, 11));
        pinBtn.setForeground(Color.white);
        pinBtn.setFocusPainted(false);
        pinBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        pinBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backBtn = new JButton("Back");
        backBtn.setBackground(Color.white);
        backBtn.setForeground(new Color(107, 114, 128));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(7, 15, 7, 15)));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttons.add(addMaterialBtn);
        buttons.add(upvoteBtn);
        buttons.add(downvoteBtn);
        buttons.add(commentsBtn);
        buttons.add(deleteBtn);
        buttons.add(pinBtn);
        buttons.add(backBtn);
        add(buttons, BorderLayout.SOUTH);

        // Wire navigation: Back to student or professor page depending on who opened it
        backBtn.addActionListener(e -> {
            if (isOwner) parent.show(MainFrame.PROFESSOR);
            else parent.show(MainFrame.STUDENT);
        });

        // Open Add Material page for current course
        addMaterialBtn.addActionListener(e -> {
            if (currentCourseCode != null && !currentCourseCode.isEmpty()) {
                parent.openAddMaterialPage(currentCourseCode);
            } else {
                parent.openAddMaterialPage("");
            }
        });

        // Double-click to view material content (delegated to MaterialViewer)
        materialList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = materialList.getSelectedIndex();
                    if (idx >= 0 && materials != null && idx < materials.size()) {
                        Material m = materials.get(idx);
                        MaterialViewer.showDialog(parent, m, parent.getCommentManager());
                    }
                }
            }
        });

        // Selection listener: enable/disable delete & pin and update pin label
        materialList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int idx = materialList.getSelectedIndex();
            boolean ok = idx >= 0 && materials != null && idx < materials.size();
            deleteBtn.setEnabled(ok);
            pinBtn.setEnabled(ok);
            upvoteBtn.setEnabled(ok);
            downvoteBtn.setEnabled(ok);
            commentsBtn.setEnabled(ok);
            if (ok) {
                Material sel = materials.get(idx);
                if (sel.isPinned()) {
                    pinBtn.setText("Unpin Material");
                } else {
                    pinBtn.setText("Pin Material");
                }
                // update upvote/downvote text to show score
                upvoteBtn.setText("Upvote (" + sel.getScore() + ")");
                downvoteBtn.setText("Downvote");
            } else {
                pinBtn.setText("Pin Material");
            }
        });

        // Voting actions
        upvoteBtn.addActionListener(e -> {
            int idx = materialList.getSelectedIndex();
            if (idx < 0) return;
            Material m = materials.get(idx);
            Model.User cur = Service.UserManager.getCurrentUser();
            if (cur == null) {
                JOptionPane.showMessageDialog(parent, "You must be logged in to vote.");
                return;
            }
            boolean ok = parent.getMaterialManager().voteMaterial(m, 1, cur.getEmail());
            if (ok) refreshMaterials();
            else JOptionPane.showMessageDialog(parent, "Failed to save vote.");
        });
        downvoteBtn.addActionListener(e -> {
            int idx = materialList.getSelectedIndex();
            if (idx < 0) return;
            Material m = materials.get(idx);
            Model.User cur = Service.UserManager.getCurrentUser();
            if (cur == null) {
                JOptionPane.showMessageDialog(parent, "You must be logged in to vote.");
                return;
            }
            boolean ok = parent.getMaterialManager().voteMaterial(m, -1, cur.getEmail());
            if (ok) refreshMaterials();
            else JOptionPane.showMessageDialog(parent, "Failed to save vote.");
        });

        // Comments action: open dialog listing comments and allow adding one if logged in
        commentsBtn.addActionListener(e -> {
            int idx = materialList.getSelectedIndex();
            if (idx < 0) return;
            Material m = materials.get(idx);
            java.util.ArrayList<Model.Comment> comments = parent.getCommentManager().loadCommentsForMaterial(m.getCourseCode(), m.getTimestamp());

            // Build dialog
            JPanel panel = new JPanel(new BorderLayout(6,6));
            DefaultListModel<String> cmModel = new DefaultListModel<>();
            JList<String> cmList = new JList<>(cmModel);
            for (Model.Comment c : comments) {
                String who = c.getCommenterEmail() == null ? "" : c.getCommenterEmail();
                cmModel.addElement(who + ": " + c.getBody());
            }
            panel.add(new JScrollPane(cmList), BorderLayout.CENTER);

            JPanel addPanel = new JPanel(new BorderLayout(4,4));
            JTextArea input = new JTextArea(4, 40);
            addPanel.add(new JScrollPane(input), BorderLayout.CENTER);
            JButton post = new JButton("Post Comment");
            addPanel.add(post, BorderLayout.SOUTH);
            panel.add(addPanel, BorderLayout.SOUTH);

            java.awt.Frame owner = null;
            java.awt.Window w = SwingUtilities.getWindowAncestor(parent);
            if (w instanceof java.awt.Frame) owner = (java.awt.Frame) w;
            JDialog dialog = new JDialog(owner, "Comments", true);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);

            post.addActionListener(ae -> {
                String text = input.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a comment.");
                    return;
                }
                String who = "";
                if (Service.UserManager.getCurrentUser() != null) who = Service.UserManager.getCurrentUser().getEmail();
                long ts = System.currentTimeMillis();
                Model.Comment nc = new Model.Comment(m.getCourseCode(), m.getTimestamp(), who, text, ts);
                boolean ok = parent.getCommentManager().saveComment(nc);
                if (ok) {
                    cmModel.addElement(who + ": " + text);
                    input.setText("");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save comment.");
                }
            });

            dialog.setVisible(true);
        });

        // Sorting action
        sortBox.addActionListener(e -> {
            int idx = sortBox.getSelectedIndex();
            if (materials != null) {
                if (parent.getMaterialManager() != null) {
                    if (idx == 0) { // Newest First
                        parent.getMaterialManager().sortByTimestamp(materials, false);
                    } else if (idx == 1) { // Oldest First
                        parent.getMaterialManager().sortByTimestamp(materials, true);
                    } else if (idx == 2) { // Most Upvoted
                        parent.getMaterialManager().sortByScore(materials, true);
                    } else if (idx == 3) { // Most Downvoted
                        parent.getMaterialManager().sortByScore(materials, false);
                    }
                }
                materialListModel.clear();
                for (Material m : materials) {
                    String uploader = m.getUploaderEmail() == null ? "" : m.getUploaderEmail();
                    String pinMark = m.isPinned() ? "[PINNED] " : "";
                    materialListModel.addElement(pinMark + m.getTitle() + "  (" + uploader + ")  " + formatTimestamp(m.getTimestamp()) + "  [score:" + m.getScore() + "]");
                }
            }
        });

        // Delete action (professors only)
        deleteBtn.addActionListener(e -> {
            int idx = materialList.getSelectedIndex();
            if (idx < 0 || materials == null || idx >= materials.size()) {
                JOptionPane.showMessageDialog(parent, "Please select a material to delete.");
                return;
            }
            Material m = materials.get(idx);
            int yn = JOptionPane.showConfirmDialog(parent, "Delete selected material?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (yn == JOptionPane.YES_OPTION) {
                boolean ok = parent.getMaterialManager().deleteMaterial(m);
                if (ok) {
                    JOptionPane.showMessageDialog(parent, "Material deleted.");
                    refreshMaterials();
                } else {
                    JOptionPane.showMessageDialog(parent, "Failed to delete material.");
                }
            }
        });

        // Pin action (professors only)
        pinBtn.addActionListener(e -> {
            int idx = materialList.getSelectedIndex();
            if (idx < 0 || materials == null || idx >= materials.size()) {
                JOptionPane.showMessageDialog(parent, "Please select a material to pin.");
                return;
            }
            Material m = materials.get(idx);
            boolean ok = parent.getMaterialManager().pinMaterial(m);
            if (ok) {
                JOptionPane.showMessageDialog(parent, "Material pinned.");
                refreshMaterials();
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to pin material.");
            }
        });
    }

    // Image viewer and material dialog responsibilities moved to helper classes

    public void setCourse(String code, String title) {
        this.currentCourseCode = code;
        titleLabel.setText(title);
        // Determine whether current user is the professor for this course
        boolean isOwner = false;
        try {
            java.util.ArrayList<Model.Course> courses = FileHandling.read("courses");
            for (Model.Course c : courses) {
                if (c.getCode() != null && c.getCode().equalsIgnoreCase(code)) {
                    Model.User u = Service.UserManager.getCurrentUser();
                    if (u != null && "Professor".equalsIgnoreCase(u.getRole()) && u.getEmail() != null && u.getEmail().equalsIgnoreCase(c.getProfessorEmail())) {
                        isOwner = true;
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            // ignore
        }
        // show/hide professor-only controls
        this.isOwner = isOwner;
        deleteBtn.setVisible(isOwner);
        pinBtn.setVisible(isOwner);

        refreshMaterials();
    }

    private void refreshMaterials() {
        materialListModel.clear();
        materials = new ArrayList<>();
        if (currentCourseCode == null || currentCourseCode.isEmpty()) {
            materialListModel.addElement("No materials yet.");
            materialList.setEnabled(false);
            return;
        }

        if (parent.getMaterialManager() != null) {
            materials = parent.getMaterialManager().loadMaterialsForCourse(currentCourseCode);
        } else {
            materials = new ArrayList<>();
            ArrayList<Material> all = FileHandling.read("materials");
            for (Material m : all) {
                if (currentCourseCode.equalsIgnoreCase(m.getCourseCode())) {
                    materials.add(m);
                }
            }
        }

        if (materials.isEmpty()) {
            materialListModel.addElement("No materials yet.");
            materialList.setEnabled(false);
            // ensure professor-only actions are disabled
            deleteBtn.setEnabled(false);
            pinBtn.setEnabled(false);
        } else {
            // Default: place pinned items first, then sort by timestamp (newest first)
            if (parent.getMaterialManager() != null) {
                // sort will put pinned as needed via the manager's comparator
                parent.getMaterialManager().sortByTimestamp(materials, false);
            }
            for (Material m : materials) {
                String uploader = m.getUploaderEmail() == null ? "" : m.getUploaderEmail();
                String pinMark = m.isPinned() ? "[PINNED] " : "";
                materialListModel.addElement(pinMark + m.getTitle() + "  (" + uploader + ")  " + formatTimestamp(m.getTimestamp()) + "  [score:" + m.getScore() + "]");
            }
            materialList.setEnabled(true);
            // clear selection and disable actions until user selects an item
            materialList.clearSelection();
            deleteBtn.setEnabled(false);
            pinBtn.setEnabled(false);
        }
    }

    private String formatTimestamp(long ts) {
        if (ts <= 0) return "";
        try {
            java.time.Instant inst = java.time.Instant.ofEpochMilli(ts);
            java.time.ZonedDateTime zdt = java.time.ZonedDateTime.ofInstant(inst, java.time.ZoneId.systemDefault());
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return zdt.format(fmt);
        } catch (Exception ex) {
            return String.valueOf(ts);
        }
    }
}
