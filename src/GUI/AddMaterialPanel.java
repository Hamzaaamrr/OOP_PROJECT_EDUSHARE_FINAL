package GUI;

import javax.swing.*;
import Model.Material;
import Service.UserManager;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;

public class AddMaterialPanel extends JPanel {

    private MainFrame parent;
    private JLabel headerLabel;
    private JLabel courseLabel;
    private JTextField titleField;
    private JTextArea bodyArea;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JButton addFilesBtn;
    private JLabel filesLabel;
    private String courseCode;
    private ArrayList<String> stagedFilePaths = new ArrayList<>();

    public AddMaterialPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(12,12));
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        headerLabel = new JLabel("Add Material");
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 24f));
        headerLabel.setForeground(new Color(31, 41, 55));
        add(headerLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new BorderLayout(10,10));
        form.setOpaque(false);

        courseLabel = new JLabel("Course: ");
        courseLabel.setFont(courseLabel.getFont().deriveFont(Font.PLAIN, 14f));
        courseLabel.setForeground(new Color(107, 114, 128));
        courseLabel.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        form.add(courseLabel, BorderLayout.NORTH);

        JPanel fields = new JPanel(new BorderLayout(0,12));
        fields.setOpaque(false);
        
        titleField = new JTextField();
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                "Title",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                null,
                new Color(55, 65, 81)),
            BorderFactory.createEmptyBorder(6,8,6,8)));
        
        bodyArea = new JTextArea(12, 40);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                "Content",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                null,
                new Color(55, 65, 81)),
            BorderFactory.createEmptyBorder(4,4,4,4)));
        
        fields.add(titleField, BorderLayout.NORTH);
        fields.add(bodyScroll, BorderLayout.CENTER);
        
        JPanel fileRow = new JPanel(new BorderLayout(8,8));
        fileRow.setOpaque(false);
        addFilesBtn = new JButton("Add Files");
        addFilesBtn.setBackground(new Color(99, 102, 241));
        addFilesBtn.setForeground(Color.white);
        addFilesBtn.setFocusPainted(false);
        addFilesBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addFilesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filesLabel = new JLabel("No files selected");
        filesLabel.setForeground(new Color(107, 114, 128));
        fileRow.add(addFilesBtn, BorderLayout.WEST);
        fileRow.add(filesLabel, BorderLayout.CENTER);
        fields.add(fileRow, BorderLayout.SOUTH);

        form.add(fields, BorderLayout.CENTER);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        saveBtn = new JButton("Save Material");
        saveBtn.setBackground(new Color(16, 185, 129));
        saveBtn.setForeground(Color.white);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.white);
        cancelBtn.setForeground(new Color(107, 114, 128));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttons.add(saveBtn);
        buttons.add(cancelBtn);
        add(buttons, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String body = bodyArea.getText().trim();
            if (title.isEmpty() || body.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please provide both title and content.");
                return;
            }

            String uploader = "";
            if (UserManager.getCurrentUser() != null && UserManager.getCurrentUser().getEmail() != null) {
                uploader = UserManager.getCurrentUser().getEmail();
            }

                long ts = System.currentTimeMillis();
                Material m = new Material(courseCode, uploader, title, body, ts);
                // attach staged file paths (these are relative paths under Data/media)
                if (!stagedFilePaths.isEmpty()) m.setFilePaths(new ArrayList<>(stagedFilePaths));
                boolean ok = parent.getMaterialManager() != null && parent.getMaterialManager().saveMaterial(m);
            if (ok) {
                JOptionPane.showMessageDialog(parent, "Material saved.");
                // clear fields
                titleField.setText("");
                bodyArea.setText("");
                stagedFilePaths.clear();
                filesLabel.setText("No files selected");
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to save material. See console for details.");
            }
            parent.openCoursePage(courseCode);
        });

        addFilesBtn.addActionListener(ae -> {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            // Accept common image and document formats
            fc.setFileFilter(new FileNameExtensionFilter("Supported files (images, pdf, docs, sheets, slides)", "jpg", "jpeg", "png", "gif", "bmp", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"));
            int res = fc.showOpenDialog(parent);
            if (res == JFileChooser.APPROVE_OPTION) {
                File[] sel = fc.getSelectedFiles();
                File mediaDir = new File("Data" + File.separator + "media");
                if (!mediaDir.exists()) mediaDir.mkdirs();
                for (File f : sel) {
                    try {
                        String uniq = System.currentTimeMillis() + "_" + f.getName();
                        File dest = new File(mediaDir, uniq);
                        Files.copy(f.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        // store relative path
                        String rel = "media/" + uniq;
                        stagedFilePaths.add(rel);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                filesLabel.setText(stagedFilePaths.size() + " file(s) selected");
            }
        });

        cancelBtn.addActionListener(e -> parent.openCoursePage(courseCode));
    }

    public void setCourseCode(String code) {
        this.courseCode = code;
        courseLabel.setText("Course: " + code);
    }
}
