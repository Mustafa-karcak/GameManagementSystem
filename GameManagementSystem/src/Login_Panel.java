import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login_Panel {

    private JFrame frmLoginPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    private int userId;

    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login_Panel window = new Login_Panel();
                window.frmLoginPanel.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login_Panel() {
        initialize();
        connect();
    }
    
    public  int getUserId() {
    	return userId;
    }

    public void connect() {
       
           // Class.forName("com.mysql.cj.jdbc.Driver");
            con = DatabaseConnection.getConnection();
      
    }

    private void initialize() {
        frmLoginPanel = new JFrame();
        frmLoginPanel.setTitle("Login Panel");
        frmLoginPanel.setBounds(100, 100, 700, 500);
        frmLoginPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmLoginPanel.setResizable(false);
        frmLoginPanel.getContentPane().setBackground(new Color(245, 245, 245));
        frmLoginPanel.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Giriş Yap");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(200, 30, 300, 50);
        frmLoginPanel.getContentPane().add(lblTitle);

        JLabel lblUsername = new JLabel("Kullanıcı Adı:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsername.setBounds(150, 120, 150, 30);
        frmLoginPanel.getContentPane().add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtUsername.setBounds(300, 120, 250, 30);
        txtUsername.setColumns(10);
        frmLoginPanel.getContentPane().add(txtUsername);

        JLabel lblPassword = new JLabel("Şifre:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPassword.setBounds(150, 180, 150, 30);
        frmLoginPanel.getContentPane().add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtPassword.setBounds(300, 180, 250, 30);
        frmLoginPanel.getContentPane().add(txtPassword);

        JButton btnLogin = new JButton("Giriş Yap");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnLogin.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(250, 260, 200, 50);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        frmLoginPanel.getContentPane().add(btnLogin);

        // Hover efekti login butonuna
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(65, 105, 225)); // Koyu mavi
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(100, 149, 237)); // Normal mavi
            }
        });

        JButton btnRegister = new JButton("Kayıt Ol");
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnRegister.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBounds(250, 330, 200, 45);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        frmLoginPanel.getContentPane().add(btnRegister);

        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(new Color(46, 139, 87)); // Daha koyu yeşil
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(new Color(60, 179, 113)); // Normal yeşil
            }
        });

        
        // Login işlemi
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            try {
                pst = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                pst.setString(1, username);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                	userId = rs.getInt("id");
                	frmLoginPanel.dispose(); // giriş ekranını kapat
                	
                	System.out.println(getUserId());
                	
                	// Market panelini aç ve kullanıcı ID'sini ilet
                    SwingUtilities.invokeLater(() -> {
                        Market_Panel marketPanel = new Market_Panel(userId);
                        marketPanel.show();
                    });
                	
                  
                 //   Market_Panel.main(new String[] {});   // Market ekranını aç
                } else {
                    JOptionPane.showMessageDialog(null, "Hatalı kullanıcı adı veya şifre!", "Giriş Başarısız", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), 
                        "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Kayıt ekranına geçiş
        btnRegister.addActionListener(e -> {
            frmLoginPanel.dispose();
            Register_Panel.main(new String[] {});
        });
    }
}
