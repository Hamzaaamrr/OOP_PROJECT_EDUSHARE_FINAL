package GUI;

import Model.Material;
import Service.CommentManager;
import Service.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MaterialViewer {

    public static void showDialog(Component parent, Material m, CommentManager commentManager) {
        JPanel content = new JPanel(new BorderLayout(6,6));

        // attachments
        ArrayList<String> files = m.getFilePaths();
        ArrayList<String> imgPaths = new ArrayList<>();
        ArrayList<String> otherFiles = new ArrayList<>();
        if (files != null) {
            for (String p : files) {
                String low = p == null ? "" : p.toLowerCase();
                if (low.endsWith(".jpg") || low.endsWith(".jpeg") || low.endsWith(".png") || low.endsWith(".gif") || low.endsWith(".bmp")) {
                    imgPaths.add(p);
                } else {
                    otherFiles.add(p);
                }
            }
        }

        if (!imgPaths.isEmpty()) {
            JPanel thumbs = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (int ti = 0; ti < imgPaths.size(); ti++) {
                String p = imgPaths.get(ti);
                final int index = ti;
                try {
                    File f = new File("Data" + File.separator + p.replace("/", File.separator));
                    if (f.exists()) {
                        ImageIcon ico = new ImageIcon(f.getAbsolutePath());
                        Image scaled = ico.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                        JLabel imgLbl = new JLabel(new ImageIcon(scaled));
                        imgLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        imgLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        imgLbl.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                ImageViewerDialog iv = new ImageViewerDialog(SwingUtilities.getWindowAncestor(parent));
                                iv.showImage(imgPaths, index);
                            }
                        });
                        thumbs.add(imgLbl);
                    }
                } catch (Exception ex) {
                    // ignore
                }
            }
            content.add(new JScrollPane(thumbs), BorderLayout.NORTH);
        }

        if (!otherFiles.isEmpty()) {
            JPanel filesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (String p : otherFiles) {
                try {
                    File f = new File("Data" + File.separator + p.replace("/", File.separator));
                    String name = f.getName();
                    JButton openBtn = new JButton("Open: " + name);
                    openBtn.addActionListener(ae -> {
                        try {
                            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
                            else JOptionPane.showMessageDialog(parent, "Cannot open files on this platform.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parent, "Failed to open file: " + ex.getMessage());
                        }
                    });
                    filesPanel.add(openBtn);
                } catch (Exception ex) {}
            }
            content.add(filesPanel, BorderLayout.SOUTH);
        }

        JTextArea area = new JTextArea(m.getBody());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(480, 300));
        content.add(sp, BorderLayout.CENTER);

        String header = m.getTitle() + "\n(Uploaded by: " + UserManager.getDisplayNameForEmail(m.getUploaderEmail()) + ")";
        JOptionPane.showMessageDialog(parent, content, header, JOptionPane.INFORMATION_MESSAGE);
    }
}
