package views;

import constants.Colors;
import controllers.OrderController;
import dtos.UserDTO;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;

    public CustomerDialog(Frame parent, OrderController controller) {
        super(parent, true);
        setTitle("Datos del Cliente");
        setBounds(100, 100, 400, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("DATOS DEL CLIENTE");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 400, 30);
        contentPane.add(lblTitle);

        JLabel lblFirstName = new JLabel("Nombre:");
        lblFirstName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblFirstName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFirstName.setBounds(30, 70, 340, 20);
        contentPane.add(lblFirstName);

        txtFirstName = new JTextField();
        txtFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFirstName.setBounds(30, 95, 320, 30);
        contentPane.add(txtFirstName);

        JLabel lblLastName = new JLabel("Apellido:");
        lblLastName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblLastName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLastName.setBounds(30, 135, 340, 20);
        contentPane.add(lblLastName);

        txtLastName = new JTextField();
        txtLastName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLastName.setBounds(30, 160, 320, 30);
        contentPane.add(txtLastName);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Colors.MOCHA_BEAN.getColor());
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setBounds(30, 200, 340, 20);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(30, 225, 320, 30);
        contentPane.add(txtEmail);

        JLabel lblPhone = new JLabel("Teléfono:");
        lblPhone.setForeground(Colors.MOCHA_BEAN.getColor());
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPhone.setBounds(30, 265, 340, 20);
        contentPane.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone.setBounds(30, 290, 320, 30);
        contentPane.add(txtPhone);

        JButton btnSave = new JButton("Finalizar Compra");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBounds(30, 350, 150, 40);
        btnSave.setBorder(null);
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String fname = txtFirstName.getText().trim();
                String lname = txtLastName.getText().trim();
                String email = txtEmail.getText().trim();
                String phone = txtPhone.getText().trim();

                if (fname.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(CustomerDialog.this, "El Nombre es obligatorio.", "Error de Validación", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (lname.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(CustomerDialog.this, "El Apellido es obligatorio.", "Error de Validación", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    javax.swing.JOptionPane.showMessageDialog(CustomerDialog.this, "El Email es obligatorio y debe tener un formato válido.", "Error de Validación", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(CustomerDialog.this, "El Teléfono es obligatorio.", "Error de Validación", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserDTO dto = new UserDTO(fname, lname, email, phone);
                controller.processCheckout(dto, CustomerDialog.this);
            }
        });
        contentPane.add(btnSave);

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(200, 350, 150, 40);
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
