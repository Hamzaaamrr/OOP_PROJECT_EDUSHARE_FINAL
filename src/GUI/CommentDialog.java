package GUI;

import Model.Comment;
import Service.CommentManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CommentDialog extends JDialog {

    public CommentDialog(Window owner, ArrayList<Comment> comments, CommentManager manager, String courseCode, long materialTs) {
        super(owner, "Comments", ModalityType.APPLICATION_MODAL);
        init(comments, manager, courseCode, materialTs);
    }

    private void init(ArrayList<Comment> comments, CommentManager manager, String courseCode, long materialTs) {
        JPanel panel = new JPanel(new BorderLayout(6,6));
        DefaultListModel<String> cmModel = new DefaultListModel<>();
        JList<String> cmList = new JList<>(cmModel);
        for (Comment c : comments) {
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

        post.addActionListener(ae -> {
            String text = input.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a comment.");
                return;
            }
            String who = "";
            if (Service.UserManager.getCurrentUser() != null) who = Service.UserManager.getCurrentUser().getEmail();
            long ts = System.currentTimeMillis();
            Comment nc = new Comment(courseCode, materialTs, who, text, ts);
            boolean ok = manager.saveComment(nc);
            if (ok) {
                cmModel.addElement(who + ": " + text);
                input.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save comment.");
            }
        });

        setContentPane(panel);
        pack();
        setLocationRelativeTo(getOwner());
    }
}
