package views;

import constants.Colors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Frame;

public class CategoryFormDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JButton btnSave;
    private JButton btnCancel;

    public CategoryFormDialog(Frame parent) {
        super(parent, true);
        setTitle("Formulario de Categoría");
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("DATOS DE LA CATEGORÍA");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 414, 30);
        contentPane.add(lblTitle);

        JLabel lblName = new JLabel("Nombre:");
        lblName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBounds(30, 70, 384, 20);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBounds(30, 95, 374, 30);
        contentPane.add(txtName);

        JLabel lblDescription = new JLabel("Descripción:");
        lblDescription.setForeground(Colors.MOCHA_BEAN.getColor());
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescription.setBounds(30, 135, 384, 20);
        contentPane.add(lblDescription);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtDescription);
        scrollPane.setBounds(30, 160, 374, 70);
        contentPane.add(scrollPane);

        btnSave = new JButton("Guardar");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBounds(30, 250, 120, 40);
        btnSave.setBorder(null);
        contentPane.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(284, 250, 120, 40);
        btnCancel.setBorder(null);
        contentPane.add(btnCancel);
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextArea getTxtDescription() {
        return txtDescription;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
