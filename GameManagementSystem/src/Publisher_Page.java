import java.awt.EventQueue;	
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Window.Type;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Publisher_Page {

	private JFrame frame;
	private JTextField txtgname;
	private JTextField txtprice;
	private JTextField txtgenre;
	private JTable table;
	private JTextField txtgid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Publisher_Page window = new Publisher_Page();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Publisher_Page() {
		initialize();
		Connect(); // when initilazes it will connect to mysql
		table_load();
	}

	//variables for connection
	Connection con;
	PreparedStatement pst;	
	ResultSet rs;
	
	//to connect with mysql server
	public void Connect() {
		
		con = DatabaseConnection.getConnection();
		
	}
	
	void table_load() {
		try {
			pst = con.prepareStatement("select * from game");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(0, 64, 128));
		frame.setTitle("SeaOfGames");
		frame.setBounds(100, 100, 1001, 565);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(28, 82, 416, 202);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Game Name");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblNewLabel.setBounds(24, 36, 122, 30);
		panel.add(lblNewLabel);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblPrice.setBounds(24, 101, 122, 30);
		panel.add(lblPrice);
		
		JLabel lblCreater = new JLabel("Genre");
		lblCreater.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblCreater.setBounds(24, 164, 122, 30);
		panel.add(lblCreater);
		
		txtgname = new JTextField();
		txtgname.setBounds(192, 36, 203, 30);
		panel.add(txtgname);
		txtgname.setColumns(10);
		
		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(192, 101, 203, 30);
		panel.add(txtprice);
		
		txtgenre = new JTextField();
		txtgenre.setColumns(10);
		txtgenre.setBounds(192, 164, 203, 30);
		panel.add(txtgenre);
		
		JLabel lblNewLabel_1 = new JLabel("Publisher Account");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel_1.setBounds(330, 10, 327, 62);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String gameName,genre,price;
				
				gameName = txtgname.getText();
				genre = txtgenre.getText();
				price = txtprice.getText();
				
				//in this section sql will be ran
				try {
					pst = con.prepareStatement("insert into game(name,price,genre)values(?,?,?)");
					pst.setString(1,gameName);
					pst.setString(2, price);
					pst.setNString(3, genre);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null,"Game Added");// message shows
					table_load();  //when game is added then table will be updated
					
					txtgname.setText("");
					txtgenre.setText("");
					txtprice.setText("");
					txtgname.requestFocus();
				}
				
				catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.setBounds(28, 327, 123, 49);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_1.setBounds(161, 327, 123, 49);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Clear");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtgname.setText("");
				txtgenre.setText("");
				txtprice.setText("");
				txtgname.requestFocus();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_2.setBounds(295, 327, 123, 49);
		frame.getContentPane().add(btnNewButton_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(454, 99, 463, 286);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(28, 405, 416, 56);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblGameid = new JLabel("GameID");
		lblGameid.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblGameid.setBounds(10, 10, 122, 30);
		panel_1.add(lblGameid);
		
		txtgid = new JTextField();
		txtgid.addKeyListener(new KeyAdapter() {
			
			//when we put the id of game it will be shown
			@Override
			public void keyReleased(KeyEvent e) {
				
				try {
					String id = txtgid.getText();
					
					pst = con.prepareStatement("select name,price,genre from game where id = ?");
					pst.setString(1, id);
					ResultSet rs = pst.executeQuery();
										
					
					if(rs.next() == true) {
						String name = rs.getString(1);
						String price = rs.getString(2);
						String genre = rs.getString(3);
						
						txtgname.setText(name);
						txtprice.setText(price);
						txtgenre.setText(genre);
						
					}
					
					else {
						txtgname.setText("");
						txtprice.setText("");
						txtgenre.setText("");
					}
				}
				
				catch(SQLException ex) {
					ex.printStackTrace();
				}
				
			}
		});
		txtgid.setColumns(10);
		txtgid.setBounds(178, 10, 203, 30);
		panel_1.add(txtgid);
		
		JButton btnNewButton_2_1 = new JButton("Update");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String gameName,genre,price,gameid;
				
				gameName = txtgname.getText();
				genre = txtgenre.getText();
				price = txtprice.getText();
				gameid = txtgid.getText();
				
				
				//in this section sql will be ran
				try {
					pst = con.prepareStatement("update game set name = ?,price = ?,genre = ? where id = ?");
					pst.setString(1,gameName);
					pst.setString(2, price);
					pst.setNString(3, genre);
					pst.setString(4, gameid);
					
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null,"Game Table Updated!");// message shows
					table_load();  //when game is added then table will be updated
					
					txtgname.setText("");
					txtgenre.setText("");
					txtprice.setText("");
					txtgname.requestFocus();
				}
				
				catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
				
				
			
		});
		btnNewButton_2_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_2_1.setBounds(534, 395, 123, 49);
		frame.getContentPane().add(btnNewButton_2_1);
		
		JButton btnNewButton_2_2 = new JButton("Delete");
		btnNewButton_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String gameid;
				
				
				gameid = txtgid.getText();
				
				
				//in this section sql will be ran
				try {
					pst = con.prepareStatement("delete from game where id = ?");
					
					pst.setString(1, gameid);
					
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null,"Game Delete!");// message shows
					table_load();  //when game is added then table will be updated
					
					txtgname.setText("");
					txtgenre.setText("");
					txtprice.setText("");
					txtgname.requestFocus();
				}
				
				catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
				
			
		});
		btnNewButton_2_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_2_2.setBounds(688, 395, 123, 49);
		frame.getContentPane().add(btnNewButton_2_2);
		
		JButton btnNewButton_3 = new JButton("< Login Panel");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Login_Panel.main(new String[]{}); // Login ekranına geç
			};
				
			
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_3.setBounds(28, 33, 165, 29);
		frame.getContentPane().add(btnNewButton_3);
	}
}
