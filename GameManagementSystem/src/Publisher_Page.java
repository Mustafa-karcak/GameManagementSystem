import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Publisher_Page {
    private JFrame frame;
    private JTextField txtgname;
    private JTextField txtprice;
    private JTextField txtgenre;
    private JTable table;
    private JTextField txtgid;

    // Database connection variables
    private Connection con;
    private PreparedStatement pst;    
    private ResultSet rs;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Publisher_Page window = new Publisher_Page();
                window.frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error starting application: " + e.getMessage(), 
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public Publisher_Page() {
        initialize();
        connect();
        table_load();
    }

    private void connect() {
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "Database connection failed: " + e.getMessage(), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void table_load() {
        try {
            pst = con.prepareStatement("SELECT id, name, price, genre FROM game ORDER BY name");
            rs = pst.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
            // Customize table appearance
            table.setRowHeight(25);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        } catch (SQLException e) {
            showSqlError(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        setupMainFrame();
        setupHeaderPanel();
        setupInputPanel();
        setupButtonPanel();
        setupTablePanel();
        setupGameIdPanel();
        setupActionButtons();
    }

    private void setupMainFrame() {
        frame = new JFrame();
        frame.setTitle("SeaOfGames - Publisher Dashboard");
        frame.setBounds(100, 100, 1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 240, 240));
        frame.getContentPane().setLayout(new BorderLayout(10, 10));
    }

    private void setupHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("PUBLISHER ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        JButton backButton = new JButton("← Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.addActionListener(e -> {
            frame.dispose();
            Login_Panel.main(new String[]{});
        });
        headerPanel.add(backButton);
        
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
    }

    private void setupInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 15));
        inputPanel.setBorder(new CompoundBorder(
            new TitledBorder("Game Information"),
            new EmptyBorder(15, 15, 15, 15)
        ));
        inputPanel.setBackground(Color.WHITE);

        // Game Name
        JLabel nameLabel = createInputLabel("Game Name:");
        txtgname = createInputField();
        inputPanel.add(nameLabel);
        inputPanel.add(txtgname);

        // Price
        JLabel priceLabel = createInputLabel("Price (€):");
        txtprice = createInputField();
        txtprice.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == '.')) {
                    e.consume();
                    JOptionPane.showMessageDialog(frame, "Please enter numbers only for price", 
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        inputPanel.add(priceLabel);
        inputPanel.add(txtprice);

        // Genre
        JLabel genreLabel = createInputLabel("Genre:");
        txtgenre = createInputField();
        inputPanel.add(genreLabel);
        inputPanel.add(txtgenre);

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        container.add(inputPanel, BorderLayout.CENTER);
        frame.getContentPane().add(container, BorderLayout.WEST);
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Save Button
        JButton saveButton = createActionButton("SAVE", new Color(46, 204, 113));
        saveButton.addActionListener(e -> saveGame());
        buttonPanel.add(saveButton);

        // Clear Button
        JButton clearButton = createActionButton("CLEAR", new Color(52, 152, 219));
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);

        // Exit Button
        JButton exitButton = createActionButton("EXIT", new Color(231, 76, 60));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        return button;
    }

    private void setupTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new CompoundBorder(
            new EmptyBorder(10, 0, 10, 10),
            new TitledBorder("Game Inventory")
        ));
        tablePanel.setBackground(Color.WHITE);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtgid.setText(table.getValueAt(row, 0).toString());
                    txtgname.setText(table.getValueAt(row, 1).toString());
                    txtprice.setText(table.getValueAt(row, 2).toString());
                    txtgenre.setText(table.getValueAt(row, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
    }

    private void setupGameIdPanel() {
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        idPanel.setBackground(Color.WHITE);
        idPanel.setBorder(new EmptyBorder(0, 20, 20, 0));

        JLabel idLabel = new JLabel("Search by ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idPanel.add(idLabel);

        txtgid = new JTextField(10);
        txtgid.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtgid.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String id = txtgid.getText().trim();
                if (!id.isEmpty()) {
                    try {
                        pst = con.prepareStatement("SELECT name, price, genre FROM game WHERE id = ?");
                        pst.setString(1, id);
                        rs = pst.executeQuery();

                        if (rs.next()) {
                            txtgname.setText(rs.getString(1));
                            txtprice.setText(rs.getString(2));
                            txtgenre.setText(rs.getString(3));
                        } else {
                            clearFields();
                            JOptionPane.showMessageDialog(frame, "No game found with ID: " + id, 
                                "Not Found", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        showSqlError(ex);
                    } finally {
                        try {
                            if (rs != null) rs.close();
                            if (pst != null) pst.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        idPanel.add(txtgid);

        frame.getContentPane().add(idPanel, BorderLayout.NORTH);
    }

    private void setupActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionPanel.setBackground(new Color(240, 240, 240));

        // Update Button
        JButton updateButton = createActionButton("UPDATE", new Color(241, 196, 15));
        updateButton.addActionListener(e -> updateGame());
        actionPanel.add(updateButton);

        // Delete Button
        JButton deleteButton = createActionButton("DELETE", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteGame());
        actionPanel.add(deleteButton);

        frame.getContentPane().add(actionPanel, BorderLayout.EAST);
    }

    private void saveGame() {
        String gameName = txtgname.getText().trim();
        String genre = txtgenre.getText().trim();
        String priceText = txtprice.getText().trim();

        if (gameName.isEmpty() || genre.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate and format price
            double price = Double.parseDouble(priceText);
            DecimalFormat df = new DecimalFormat("#.##");
            price = Double.parseDouble(df.format(price));

            pst = con.prepareStatement("INSERT INTO game(name, price, genre) VALUES(?, ?, ?)");
            pst.setString(1, gameName);
            pst.setDouble(2, price);
            pst.setString(3, genre);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Game added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                table_load();
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid price (e.g., 19.99)", 
                "Invalid Price", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            showSqlError(e);
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        String gameId = txtgid.getText().trim();
        if (gameId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a game to update (search by ID first)", 
                "No Game Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gameName = txtgname.getText().trim();
        String genre = txtgenre.getText().trim();
        String priceText = txtprice.getText().trim();

        if (gameName.isEmpty() || genre.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate and format price
            double price = Double.parseDouble(priceText);
            DecimalFormat df = new DecimalFormat("#.##");
            price = Double.parseDouble(df.format(price));

            pst = con.prepareStatement("UPDATE game SET name = ?, price = ?, genre = ? WHERE id = ?");
            pst.setString(1, gameName);
            pst.setDouble(2, price);
            pst.setString(3, genre);
            pst.setString(4, gameId);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Game updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                table_load();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "No game found with ID: " + gameId, 
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid price (e.g., 19.99)", 
                "Invalid Price", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            showSqlError(e);
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteGame() {
        String gameId = txtgid.getText().trim();
        if (gameId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a game to delete (search by ID first)", 
                "No Game Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to delete this game?", "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            pst = con.prepareStatement("DELETE FROM game WHERE id = ?");
            pst.setString(1, gameId);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Game deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                table_load();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "No game found with ID: " + gameId, 
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            showSqlError(e);
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtgid.setText("");
        txtgname.setText("");
        txtprice.setText("");
        txtgenre.setText("");
        table.clearSelection();
        txtgname.requestFocus();
    }

    private void showSqlError(SQLException e) {
        JOptionPane.showMessageDialog(frame, 
            "Database error: " + e.getMessage(), 
            "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}