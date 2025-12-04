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
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.white);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#e0e6ee")),
                BorderFactory.createEmptyBorder(18,18,18,18)));

        JLabel title = new JLabel("Create Account");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setAlignmentX(CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0,10)));

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
        registerBtn.setBackground(Color.decode("#22a06b"));
        registerBtn.setForeground(Color.white);
        JButton backBtn = new JButton("Back to Login");
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
