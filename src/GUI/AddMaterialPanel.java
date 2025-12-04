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
        setLayout(new BorderLayout(8,8));

        headerLabel = new JLabel("Add Material");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(headerLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new BorderLayout(6,6));

        courseLabel = new JLabel("Course: ");
        form.add(courseLabel, BorderLayout.NORTH);

        JPanel fields = new JPanel(new BorderLayout(6,6));
        titleField = new JTextField();
        titleField.setBorder(BorderFactory.createTitledBorder("Title"));
        bodyArea = new JTextArea(10, 40);
        bodyArea.setBorder(BorderFactory.createTitledBorder("Content"));
        fields.add(titleField, BorderLayout.NORTH);
        fields.add(new JScrollPane(bodyArea), BorderLayout.CENTER);
        JPanel fileRow = new JPanel(new BorderLayout(4,4));
        addFilesBtn = new JButton("Add Files");
        filesLabel = new JLabel("No files selected");
        fileRow.add(addFilesBtn, BorderLayout.WEST);
        fileRow.add(filesLabel, BorderLayout.CENTER);
        fields.add(fileRow, BorderLayout.SOUTH);

        form.add(fields, BorderLayout.CENTER);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
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
