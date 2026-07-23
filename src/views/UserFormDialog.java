package views;

import constants.Colors;
import controllers.UserController;
import dtos.UserDTO;
import enums.UserRole;
import models.User;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<UserRole> cmbRol;
    private JButton btnSave;
    private JButton btnCancel;

    public UserFormDialog(Frame parent, UserController controller, User dto) {
        super(parent, true);
        setTitle("Formulario de Usuario");
        setBounds(100, 100, 450, 600);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("DATOS DEL USUARIO");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 414, 30);
        contentPane.add(lblTitle);

        JLabel lblFirstName = new JLabel("Nombre:");
        lblFirstName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblFirstName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFirstName.setBounds(30, 70, 384, 20);
        contentPane.add(lblFirstName);

        txtFirstName = new JTextField();
        txtFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFirstName.setBounds(30, 95, 374, 30);
        contentPane.add(txtFirstName);

        JLabel lblLastName = new JLabel("Apellido:");
        lblLastName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblLastName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLastName.setBounds(30, 135, 384, 20);
        contentPane.add(lblLastName);

        txtLastName = new JTextField();
        txtLastName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLastName.setBounds(30, 160, 374, 30);
        contentPane.add(txtLastName);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Colors.MOCHA_BEAN.getColor());
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setBounds(30, 200, 384, 20);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(30, 225, 374, 30);
        contentPane.add(txtEmail);

        JLabel lblPhone = new JLabel("Teléfono:");
        lblPhone.setForeground(Colors.MOCHA_BEAN.getColor());
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPhone.setBounds(30, 265, 384, 20);
        contentPane.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone.setBounds(30, 290, 374, 30);
        contentPane.add(txtPhone);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setForeground(Colors.MOCHA_BEAN.getColor());
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRol.setBounds(30, 330, 180, 20);
        contentPane.add(lblRol);

        cmbRol = new JComboBox<>(new UserRole[]{UserRole.EMPLOYEE, UserRole.MANAGER});
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRol.setBounds(30, 355, 180, 30);
        contentPane.add(cmbRol);

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setForeground(Colors.MOCHA_BEAN.getColor());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setBounds(224, 330, 180, 20);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(224, 355, 180, 30);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(Colors.MOCHA_BEAN.getColor());
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setBounds(30, 395, 384, 20);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(30, 420, 374, 30);
        contentPane.add(txtPassword);

        JLabel lblPassDesc = new JLabel(dto == null ? "Requerido para nuevos usuarios." : "Dejar en blanco para no cambiar.");
        lblPassDesc.setForeground(Colors.WARM_CAPP.getColor());
        lblPassDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPassDesc.setBounds(30, 450, 384, 20);
        contentPane.add(lblPassDesc);

        if (dto != null) {
            txtFirstName.setText(dto.getFirstName());
            txtLastName.setText(dto.getLastName());
            txtEmail.setText(dto.getEmail());
            txtPhone.setText(dto.getPhone());
            cmbRol.setSelectedItem(dto.getRol());
        }

        btnSave = new JButton("Guardar");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBounds(30, 490, 120, 40);
        btnSave.setBorder(null);
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String phone = txtPhone.getText();
                UserRole rol = (UserRole) cmbRol.getSelectedItem();
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                
                UserDTO newDto = new UserDTO(
                    dto != null ? dto.getId() : null,
                    firstName, lastName, email, phone, true, rol, username, password
                );
                controller.saveUser(newDto, UserFormDialog.this);
            }
        });
        contentPane.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(284, 490, 120, 40);
        btnCancel.setBorder(null);
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        contentPane.add(btnCancel);
    }
}
