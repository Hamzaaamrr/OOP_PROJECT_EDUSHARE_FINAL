package GUI;

import javax.swing.*;
import java.awt.*;

public class ImageViewerDialog extends JDialog {

    private JLabel imgLabel;

    public ImageViewerDialog(Window owner) {
        super(owner);
        initUI();
    }

    private void initUI() {
        setModal(true);
        setTitle("Image Viewer");
        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setVerticalAlignment(SwingConstants.CENTER);
        JPanel root = new JPanel(new BorderLayout());
        root.add(new JScrollPane(imgLabel), BorderLayout.CENTER);
        setContentPane(root);
    }

    public void showImage(java.util.List<String> imgs, int index) {
        if (imgs == null || imgs.isEmpty()) return;
        Window owner = getOwner();
        final int[] idx = new int[] { Math.max(0, Math.min(index, imgs.size() - 1)) };

        Runnable showAt = () -> {
            try {
                String p = imgs.get(idx[0]);
                java.io.File f = new java.io.File("Data" + java.io.File.separator + p.replace("/", java.io.File.separator));
                if (f.exists()) {
                    javax.swing.ImageIcon ico = new javax.swing.ImageIcon(f.getAbsolutePath());
                    java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                    int maxW = (int) (screen.width * 0.9);
                    int maxH = (int) (screen.height * 0.85);
                    java.awt.Image img = ico.getImage();
                    int w = img.getWidth(null);
                    int h = img.getHeight(null);
                    if (w <= 0 || h <= 0) {
                        imgLabel.setIcon(ico);
                    } else {
                        double scale = Math.min((double) maxW / w, (double) maxH / h);
                        int nw = (int) (w * scale);
                        int nh = (int) (h * scale);
                        java.awt.Image scaled = img.getScaledInstance(nw, nh, java.awt.Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new javax.swing.ImageIcon(scaled));
                    }
                } else {
                    imgLabel.setIcon(null);
                    imgLabel.setText("Image not found: " + p);
                }
                pack();
                setLocationRelativeTo(owner);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        // controls
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton close = new JButton("Close");
        ctrl.add(prev);
        ctrl.add(next);
        ctrl.add(close);
        getContentPane().add(ctrl, BorderLayout.SOUTH);

        prev.addActionListener(e -> {
            idx[0] = (idx[0] - 1 + imgs.size()) % imgs.size();
            showAt.run();
        });
        next.addActionListener(e -> {
            idx[0] = (idx[0] + 1) % imgs.size();
            showAt.run();
        });
        close.addActionListener(e -> dispose());

        rootKeyBindings(ctrl, prev, next, close);

        showAt.run();
        setVisible(true);
    }

    private void rootKeyBindings(JComponent root, JButton prev, JButton next, JButton close) {
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "prev");
        root.getActionMap().put("prev", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { prev.doClick(); } });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "next");
        root.getActionMap().put("next", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { next.doClick(); } });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        root.getActionMap().put("close", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { close.doClick(); } });
    }
}
