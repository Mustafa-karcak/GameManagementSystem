import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

public class Market_Panel {

    private JFrame frame;
    private JPanel gamesPanel;
    private JPanel cartPanel;
    private Map<Integer, Game> cartItems;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private int currentUserId;
    private Library_Panel1 libraryPanel;
    
    

    public Market_Panel(int userId) {
        this.currentUserId = userId;
        this.cartItems = new HashMap<>();
        initialize();
        loadGamesFromDatabase();
        updateCartUI();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Global Game Store");
        frame.setBounds(100, 100, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(32, 42, 68));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("GAME STORE");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Library Button
       
        // Header Panel'in içine bir küçük panel oluştur
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false); // Arka plan görünmesin (header ile aynı olsun)

        // Backto Login Button
        JButton loginButton = new JButton("<Back");
        loginButton.setPreferredSize(new Dimension(100, 40)); // Aynı boyutta olsun
        styleButton(loginButton);
        loginButton.addActionListener(e -> backToLogin());
        buttonPanel.add(loginButton);
        
        // Publish Button
        JButton publishButton = new JButton("Publish Game");
        publishButton.setPreferredSize(new Dimension(140, 40)); // Küçük ve sabit boyut
        styleButton(publishButton);
        publishButton.addActionListener(e -> publishGame()); // Burayı sonra değiştirebilirsin
        buttonPanel.add(publishButton);

        // Library Button
        JButton libraryButton = new JButton("MY LIBRARY");
        libraryButton.setPreferredSize(new Dimension(140, 40)); // Aynı boyutta olsun
        styleButton(libraryButton);
        libraryButton.addActionListener(e -> openLibrary());
        buttonPanel.add(libraryButton);
        
      


        // Header Panel'in EAST kısmına ekliyoruz
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Header Paneli Frame'e ekliyoruz
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);

        
  
        // Main Content with Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setDividerSize(5);

        // Games Panel (Left)
        gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        gamesPanel.setBackground(new Color(36, 39, 48));
        JScrollPane gamesScroll = new JScrollPane(gamesPanel);
        splitPane.setLeftComponent(gamesScroll);

        // Cart Panel (Right)
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(new Color(36, 39, 48));
        
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.add(new JScrollPane(cartPanel), BorderLayout.CENTER);

        // Cart Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(new Color(46, 49, 58));
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE);
        
        checkoutButton = new JButton("CHECKOUT");
        styleButton(checkoutButton);
        checkoutButton.addActionListener(e -> checkout());
             

        
        summaryPanel.add(totalLabel);
        summaryPanel.add(checkoutButton);
        cartContainer.add(summaryPanel, BorderLayout.SOUTH);
        
        splitPane.setRightComponent(cartContainer);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    private void openLibrary() {
        if (libraryPanel == null) {
            libraryPanel = new Library_Panel1(currentUserId, frame);
        }
        libraryPanel.show();
        libraryPanel.refreshLibrary();
    }
    
    private void backToLogin() {
    	frame.dispose();
        Login_Panel.main(new String[] {});

    }
    
    
    private void publishGame() {    	
      
        int result = JOptionPane.showConfirmDialog(
        	    null,
        	    "You cannot return here from the Publisher Dashboard Page. You will need to log in again. Are you sure?",
        	    "You can't go back from here",
        	    JOptionPane.YES_NO_OPTION,
        	    JOptionPane.WARNING_MESSAGE
        	);

        	if (result == JOptionPane.YES_OPTION) {
        	    // Evet seçildi, giriş ekranına yönlendir
                Publisher_Page.main(new String[]{}); // publisher ekranına geç
                frame.dispose();
        	} else {
        	    // Hayır seçildi, bir şey yapma veya farklı işlem yap
        	    System.out.println("Kullanıcı publisher dashboard'a giriş yapmak istemedi.");
        	    
        	}

        
    }

    private void refreshLibraryPanel() {
        if (libraryPanel != null) {
            libraryPanel.refreshLibrary();
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void loadGamesFromDatabase() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT id, name, price, genre FROM game";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int gameId = rs.getInt("id");
                String gameName = rs.getString("name");
                double price = rs.getDouble("price");
                String genre = rs.getString("genre");
                addGameToPanel(gameId, gameName, price, genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading games: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addGameToPanel(int gameId, String gameName, double price, String genre) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 70, 70), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(46, 49, 58));

        // Game Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(gameName);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);

        JLabel priceLabel = new JLabel("$" + String.format("%.2f", price));
        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        priceLabel.setForeground(new Color(180, 180, 180));

        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        genreLabel.setForeground(new Color(200, 200, 200));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(genreLabel);

        // Add to Cart Button
        JButton addToCartButton = new JButton("ADD TO CART");
        styleButton(addToCartButton);
        addToCartButton.addActionListener(e -> addToCart(gameId, gameName, price));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addToCartButton, BorderLayout.EAST);

        gamesPanel.add(card);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        gamesPanel.revalidate();
    }

    private void addToCart(int gameId, String gameName, double price) {
     
        if (cartItems.containsKey(gameId)) {
            JOptionPane.showMessageDialog(frame, 
                "You already have this game in your cart!\n" +
                "You can't purchase the same game multiple times.",
                "Already in Cart",
                JOptionPane.WARNING_MESSAGE);
        } else {
            cartItems.put(gameId, new Game(gameId, gameName, price));
            JOptionPane.showMessageDialog(frame, gameName + " added to cart!");
            updateCartUI();
        }
    }

    private void updateCartUI() { 
    	cartPanel.removeAll();
    
    if (cartItems.isEmpty()) {
        JLabel emptyLabel = new JLabel("Your cart is empty");
        emptyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        emptyLabel.setForeground(new Color(180, 180, 180));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartPanel.add(emptyLabel);
        checkoutButton.setEnabled(false);
    } else {
        double total = 0.0;
        
        for (Game game : cartItems.values()) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            itemPanel.setBackground(new Color(56, 59, 68));
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            // Game Info
            JLabel gameLabel = new JLabel(
                String.format("%s - $%.2f", game.getName(), game.getPrice())
            );
            gameLabel.setForeground(Color.WHITE);
            
            // Remove Button
            JButton removeButton = new JButton("Remove");
            removeButton.setForeground(new Color(255, 100, 100));
            removeButton.setContentAreaFilled(false);
            removeButton.setBorder(null);
            removeButton.addActionListener(e -> {
                cartItems.remove(game.getId());
                updateCartUI();
            });
            
            itemPanel.add(gameLabel, BorderLayout.WEST);
            itemPanel.add(removeButton, BorderLayout.EAST);
            
            cartPanel.add(itemPanel);
            cartPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            
            total += game.getPrice();
        }
        
        checkoutButton.setEnabled(true);
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }
    
    cartPanel.revalidate();
    cartPanel.repaint();
    }


    private double calculateTotal() {
        return cartItems.values().stream()
                .mapToDouble(game -> game.getPrice())
                .sum();
    }

    private void checkout() {if (cartItems.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Your cart is empty!");
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(
        frame, 
        "Proceed to checkout with total: $" + String.format("%.2f", calculateTotal()) + "?",
        "Confirm Purchase", 
        JOptionPane.YES_NO_OPTION
    );
    
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try (Connection con = DatabaseConnection.getConnection()) {
        con.setAutoCommit(false);
        String sql = "INSERT INTO purchases (user_id, game_id) VALUES (?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Game game : cartItems.values()) {
                // Aynı oyun zaten kütüphanede var mı kontrolü
                String checkQuery = "SELECT 1 FROM purchases WHERE user_id = ? AND game_id = ?";
                try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, currentUserId);
                    checkPs.setInt(2, game.getId());
                    ResultSet rs = checkPs.executeQuery();
                    
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(frame,
                            "You already own '" + game.getName() + "' in your library!\n" +
                            "Please remove it from your cart.",
                            "Already Owned",
                            JOptionPane.WARNING_MESSAGE);
                        con.rollback();
                        return;
                    }
                }
                
                ps.setInt(1, currentUserId);
                ps.setInt(2, game.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
            
            JOptionPane.showMessageDialog(frame, 
                "Purchase successful! Games added to your library.");
            cartItems.clear();
            updateCartUI();
            refreshLibraryPanel();
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(frame,
            "Error during purchase: " + e.getMessage(),
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }

    private static class Game {
        private final int id;
        private final String name;
        private final double price;

        public Game(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
           
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
    }
}

class Library_Panel1 {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final JPanel gamesPanel;
    private final int userId;

    public Library_Panel1(int userId, JFrame parentFrame) {
        this.userId = userId;
        this.frame = new JFrame("My Library");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(1000, 700);
        this.frame.setLocationRelativeTo(parentFrame);
        
        this.mainPanel = new JPanel(new BorderLayout());
        this.gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        gamesPanel.setBackground(new Color(36, 39, 48));
        
        initializeUI();
        refreshLibrary();
    }

    private void initializeUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(32, 42, 68));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("MY LIBRARY");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Main Content
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(gamesPanel), BorderLayout.CENTER);
        frame.setContentPane(mainPanel);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void refreshLibrary() {
        gamesPanel.removeAll();
        
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT g.id, g.name, g.genre FROM game g " +
                         "JOIN purchases p ON g.id = p.game_id " +
                         "WHERE p.user_id = ? ORDER BY p.purchase_date DESC";
            
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    addGameToPanel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre")
                    );
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                "Error loading library: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        if (gamesPanel.getComponentCount() == 0) {
            JLabel emptyLabel = new JLabel("Your library is empty");
            emptyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(180, 180, 180));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            gamesPanel.add(emptyLabel);
        }
        
        gamesPanel.revalidate();
        gamesPanel.repaint();
    }

    private void addGameToPanel(int gameId, String gameName, String genre) {
        JPanel gameCard = new JPanel(new BorderLayout());
        gameCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(70, 70, 70)),
            new EmptyBorder(10, 15, 10, 15)
        ));
        gameCard.setBackground(new Color(46, 49, 58));
        gameCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Game Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(gameName);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        genreLabel.setForeground(new Color(180, 180, 180));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(genreLabel);

        // Play Button
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        playButton.setBackground(new Color(0, 180, 0));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.addActionListener(e -> launchGame(gameId));

        gameCard.add(infoPanel, BorderLayout.CENTER);
        gameCard.add(playButton, BorderLayout.EAST);
        
        gamesPanel.add(gameCard);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void launchGame(int gameId) {
        JOptionPane.showMessageDialog(frame, 
            "Launching game: " + gameId + "\n" +
            "(This would normally start the game)", 
            "Game Launch", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}