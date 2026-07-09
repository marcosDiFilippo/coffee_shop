package views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import constants.Colors;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final Color creamyLatte = Colors.CREAMY_LATTE.getColor();
	private final Color mochaBean = Colors.MOCHA_BEAN.getColor();
	private final Color warmCapp = Colors.WARM_CAPP.getColor();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Home() {
		setTitle("Sistema de Gestión de Cafetería");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(creamyLatte);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(mochaBean);
		headerPanel.setBounds(0, 0, 1008, 80);
		headerPanel.setLayout(null);
		contentPane.add(headerPanel);
		
		JLabel lblShopName = new JLabel("LOS TOSTADORES DE CAFÉ");
		lblShopName.setForeground(creamyLatte);
		lblShopName.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblShopName.setBounds(30, 20, 400, 40);
		headerPanel.add(lblShopName);
		
		JButton btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setForeground(creamyLatte);
		btnLogin.setBackground(warmCapp);
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnLogin.setBounds(850, 20, 120, 40);
		btnLogin.setFocusPainted(false);
		btnLogin.setBorder(new LineBorder(warmCapp, 2, true));
		headerPanel.add(btnLogin);
		
		JLabel lblMainTitle = new JLabel("DISFRUTA EL CAFÉ PERFECTO");
		lblMainTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainTitle.setForeground(warmCapp);
		lblMainTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
		lblMainTitle.setBounds(0, 250, 1008, 60);
		contentPane.add(lblMainTitle);
		
		JLabel lblSlogan = new JLabel("Despierta tus sentidos con nuestro café artesanal");
		lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlogan.setForeground(mochaBean);
		lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 24));
		lblSlogan.setBounds(0, 330, 1008, 40);
		contentPane.add(lblSlogan);
		
		JLabel lblDesc1 = new JLabel("Nuestros granos cuidadosamente seleccionados se tuestan a la perfección cada día,");
		lblDesc1.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesc1.setForeground(mochaBean);
		lblDesc1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblDesc1.setBounds(0, 400, 1008, 30);
		contentPane.add(lblDesc1);
		
		JLabel lblDesc2 = new JLabel("brindando un sabor rico y suave en cada taza.");
		lblDesc2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesc2.setForeground(mochaBean);
		lblDesc2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblDesc2.setBounds(0, 430, 1008, 30);
		contentPane.add(lblDesc2);
		
		JLabel lblDesc3 = new JLabel("Visítanos y prueba la diferencia de la verdadera artesanía.");
		lblDesc3.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesc3.setForeground(mochaBean);
		lblDesc3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblDesc3.setBounds(0, 460, 1008, 30);
		contentPane.add(lblDesc3);
		
		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(mochaBean);
		footerPanel.setBounds(0, 689, 1008, 40);
		footerPanel.setLayout(null);
		contentPane.add(footerPanel);
		
		JLabel lblFooterText = new JLabel("© 2026 Los Tostadores de Café. Todos los derechos reservados.");
		lblFooterText.setHorizontalAlignment(SwingConstants.CENTER);
		lblFooterText.setForeground(creamyLatte);
		lblFooterText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblFooterText.setBounds(0, 10, 1008, 20);
		footerPanel.add(lblFooterText);
	}
}
