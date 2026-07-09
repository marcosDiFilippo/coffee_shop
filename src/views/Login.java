package views;

import constants.Colors;
import controllers.LoginController;
import dtos.LoginDTO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController loginController;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitle = new JLabel("INICIAR SESIÓN");
        lblTitle.setForeground(Colors.WARM_CAPP.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 40, 384, 40);
        contentPane.add(lblTitle);
        
        JLabel lblUsername = new JLabel("Usuario");
        lblUsername.setForeground(Colors.MOCHA_BEAN.getColor());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setBounds(50, 120, 284, 20);
        contentPane.add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBackground(Colors.ESPRESSO_SHOT.getColor());
        txtUsername.setForeground(Colors.WARM_CAPP.getColor());
        txtUsername.setBounds(50, 150, 284, 35);
        contentPane.add(txtUsername);
        
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setForeground(Colors.MOCHA_BEAN.getColor());
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setBounds(50, 210, 284, 20);
        contentPane.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(Colors.ESPRESSO_SHOT.getColor());
        txtPassword.setForeground(Colors.WARM_CAPP.getColor());
        txtPassword.setBounds(50, 240, 284, 35);
        contentPane.add(txtPassword);
        
        loginController = new LoginController(this);

        btnLogin = new JButton("Ingresar");
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                LoginDTO loginDTO = new LoginDTO(username, password);
                loginController.login(loginDTO);
            }
        });
        btnLogin.setBackground(Colors.WARM_CAPP.getColor());
        btnLogin.setForeground(Colors.CREAMY_LATTE.getColor());
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBounds(50, 320, 284, 45);
        btnLogin.setFocusPainted(false);
        contentPane.add(btnLogin);
    }
    
    public JTextField getTxtUsername() {
        return txtUsername;
    }
    
    public JPasswordField getTxtPassword() {
        return txtPassword;
    }
    
    public JButton getBtnLogin() {
        return btnLogin;
    }
}