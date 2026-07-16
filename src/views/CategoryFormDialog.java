package views;

import constants.Colors;
import controllers.CategoryController;
import dtos.CategoryDTO;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryFormDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JCheckBox chkRequiresSize;
    private JButton btnSave;
    private JButton btnCancel;

    public CategoryFormDialog(Frame parent, CategoryController controller, CategoryDTO dto) {
        super(parent, true);
        setTitle("Formulario de Categoría");
        setBounds(100, 100, 450, 400);
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
        
        if (dto != null) {
            txtName.setText(dto.getName());
            txtDescription.setText(dto.getDescription());
        }

        chkRequiresSize = new JCheckBox("¿Tienen distintos tamaños los productos de esta categoría?");
        chkRequiresSize.setBackground(Colors.CREAMY_LATTE.getColor());
        chkRequiresSize.setForeground(Colors.MOCHA_BEAN.getColor());
        chkRequiresSize.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkRequiresSize.setBounds(30, 240, 384, 20);
        if (dto != null) {
            chkRequiresSize.setSelected(dto.isRequiresSize());
        }
        contentPane.add(chkRequiresSize);

        JLabel lblSizeDesc = new JLabel("Activa esto para Bebidas. El sistema pedirá elegir tamaño al vender.");
        lblSizeDesc.setForeground(Colors.WARM_CAPP.getColor());
        lblSizeDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSizeDesc.setBounds(30, 260, 384, 20);
        contentPane.add(lblSizeDesc);

        btnSave = new JButton("Guardar");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBounds(30, 300, 120, 40);
        btnSave.setBorder(null);
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                boolean requiresSize = chkRequiresSize.isSelected();
                CategoryDTO newDto = new CategoryDTO(dto != null ? dto.getId() : null, name, description, true, requiresSize);
                controller.saveCategory(newDto, CategoryFormDialog.this);
            }
        });
        contentPane.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(284, 300, 120, 40);
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
