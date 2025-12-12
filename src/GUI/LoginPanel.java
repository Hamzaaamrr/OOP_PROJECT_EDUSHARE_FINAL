package GUI;

import Service.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
// no direct action imports required (using lambdas)

public class LoginPanel extends JPanel {

    private MainFrame parent;
    private JTextField emailField;
    private JPasswordField passField;

    public LoginPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.white);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(32,32,32,32)));

        JLabel title = new JLabel("Welcome to EDUSHARE");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(new Color(79, 70, 229));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0,8)));
        
        JLabel subtitle = new JLabel("Share knowledge, grow together");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(107, 114, 128));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Email"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        emailField = new JTextField(24);
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Password"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        passField = new JPasswordField(24);
        form.add(passField, gbc);

        card.add(form);
        card.add(Box.createRigidArea(new Dimension(0,12)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(79, 70, 229));
        loginBtn.setForeground(Color.white);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(loginBtn.getFont().deriveFont(Font.BOLD, 14f));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton signupBtn = new JButton("Sign up");
        signupBtn.setBackground(new Color(249, 250, 251));
        signupBtn.setForeground(new Color(79, 70, 229));
        signupBtn.setFocusPainted(false);
        signupBtn.setFont(signupBtn.getFont().deriveFont(Font.BOLD, 14f));
        signupBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(79, 70, 229), 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)));
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttons.add(loginBtn);
        buttons.add(signupBtn);

        card.add(buttons);

        add(card, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());
            boolean ok = parent.getUserManager().login(email, pass);
                if (ok) {
                    User u = UserManager.getCurrentUser();
                    if (u != null) {
                        if ("Student".equalsIgnoreCase(u.getRole())) {
                            parent.show(MainFrame.STUDENT);
                        } else if ("Professor".equalsIgnoreCase(u.getRole())) {
                            parent.show(MainFrame.PROFESSOR);
                        } else {
                            JOptionPane.showMessageDialog(parent, "Logged in as: " + u.getEmail() + " (" + u.getRole() + ")");
                        }
                    }
            } else {
                JOptionPane.showMessageDialog(parent, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        signupBtn.addActionListener(e -> parent.show(MainFrame.SIGNUP));
    }
}
