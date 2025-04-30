import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static Connection connection;

    
    private DatabaseConnection() {
        // Private constructor: dışarıdan nesne oluşturulamaz.
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                
            	Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/gamemanagementsystem", "root", ""
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: Veritabanına bağlı değilsiniz", "Giriş Başarısız", JOptionPane.ERROR_MESSAGE);
            
        }
        return connection;
    }
}
