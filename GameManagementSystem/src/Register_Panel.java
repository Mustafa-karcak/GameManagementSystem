import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.regex.Pattern;

public class Register_Panel {

    private JFrame frame;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Register_Panel window = new Register_Panel();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Register_Panel() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Register");
        frame.setBounds(100, 100, 600, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Ortala
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(0, 51, 102));

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(220, 30, 200, 50);
        frame.getContentPane().add(titleLabel);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 51, 102));
        panel.setBounds(100, 100, 400, 300);
        frame.getContentPane().add(panel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        usernameLabel.setBounds(20, 20, 100, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 20, 220, 30);
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        emailLabel.setBounds(20, 70, 100, 30);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(140, 70, 220, 30);
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        emailField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordLabel.setBounds(20, 120, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 120, 220, 30);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        confirmPasswordLabel.setBounds(20, 170, 100, 30);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(140, 170, 220, 30);
        confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(confirmPasswordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        showPasswordCheckBox.setBounds(140, 210, 150, 25);
        showPasswordCheckBox.setBackground(new Color(0, 51, 102));
        showPasswordCheckBox.setForeground(Color.WHITE);
        panel.add(showPasswordCheckBox);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
                confirmPasswordField.setEchoChar('*');
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        registerButton.setBounds(220, 420, 150, 40);
        registerButton.setBackground(new Color(0, 102, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        frame.getContentPane().add(registerButton);

        // Hover efekti:
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(30, 144, 255));
            }

            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(0, 102, 204));
            }
        });

        JButton loginButton = new JButton("Already have an account?");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginButton.setBounds(190, 470, 200, 25);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        frame.getContentPane().add(loginButton);

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(30, 144, 255));
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(0, 102, 204));
            }
        });

        registerButton.addActionListener(e -> registerUser());
        loginButton.addActionListener(e -> {
            frame.dispose();
            Login_Panel.main(new String[]{}); // Login ekranına geç
        });
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields!");
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(null, "Email is not valid!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match!");
            return;
        }

        if (!isPasswordStrong(password)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters and include letters and numbers!");
            return;
        }

        // --- LOADING ANİMASYONU BAŞLAT ---
        JDialog loadingDialog = new JDialog(frame, "Registering...", true);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel("Please wait...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(loadingLabel, BorderLayout.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.SOUTH);
        loadingDialog.getContentPane().add(panel);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(frame);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Connection conn = DatabaseConnection.getConnection();

                    String checkQuery = "SELECT * FROM users WHERE username = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        conn.close();
                        throw new Exception("Username already taken!");
                    }

                    String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, email);
                    insertStmt.setString(3, password);

                    int rowsInserted = insertStmt.executeUpdate();
                    conn.close();

                    if (rowsInserted <= 0) {
                        throw new Exception("Registration failed!");
                    }
                } catch (Exception ex) {
                    throw ex;
                }
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    get(); // hata fırlattı mı kontrol et
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    frame.dispose();
                    Login_Panel.main(new String[]{});
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
        
        
        
        
    }

    private boolean isPasswordStrong(String password) {
        if (password.length() < 8) {
            return false;
        }
        return Pattern.compile("[a-zA-Z]").matcher(password).find() &&
               Pattern.compile("[0-9]").matcher(password).find();
    }
}
