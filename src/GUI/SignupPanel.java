package GUI;


import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {

    private MainFrame parent;
    private JTextField emailField;
    private JPasswordField passField;
    private JComboBox<String> roleBox;

    public SignupPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.white);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(32,32,32,32)));

        JLabel title = new JLabel("Create Account");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(new Color(79, 70, 229));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0,8)));
        
        JLabel subtitle = new JLabel("Join the EDUSHARE community");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(107, 114, 128));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0,18)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Email"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        emailField = new JTextField(22);
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Password"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        passField = new JPasswordField(22);
        form.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Role"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        roleBox = new JComboBox<>(new String[]{"Student","Professor"});
        form.add(roleBox, gbc);

        card.add(form);
        card.add(Box.createRigidArea(new Dimension(0,12)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12,0));
        buttons.setOpaque(false);
        JButton registerBtn = new JButton("Create Account");
        registerBtn.setBackground(new Color(16, 185, 129));
        registerBtn.setForeground(Color.white);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(registerBtn.getFont().deriveFont(Font.BOLD, 14f));
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton backBtn = new JButton("Back to Login");
        backBtn.setBackground(new Color(249, 250, 251));
        backBtn.setForeground(new Color(107, 114, 128));
        backBtn.setFocusPainted(false);
        backBtn.setFont(backBtn.getFont().deriveFont(Font.PLAIN, 13f));
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttons.add(registerBtn);
        buttons.add(backBtn);
        card.add(buttons);

        add(card, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            boolean ok = parent.getUserManager().register(email, pass, role);
            if (ok) {
                JOptionPane.showMessageDialog(parent, "Account created successfully.");
                parent.show(MainFrame.LOGIN);
            } else {
                JOptionPane.showMessageDialog(parent, "Account creation failed (exists or invalid).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> parent.show(MainFrame.LOGIN));
    }
}
