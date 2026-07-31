package views;

import constants.Colors;
import models.Size;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Frame;

public class DrinkSizeForm extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextField txtMultiplier;
    private JButton btnSave;
    private JButton btnCancel;
    private Long currentSizeId;

    public DrinkSizeForm(Frame parent, Size sizeModel) {
        super(parent, true);

        if (sizeModel != null) {
            setTitle("Modificar Tamaño");
            currentSizeId = sizeModel.getId();
        } else {
            setTitle("Nuevo Tamaño");
            currentSizeId = null;
        }

        setBounds(100, 100, 400, 340);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel(sizeModel == null ? "NUEVO TAMAÑO" : "MODIFICAR TAMAÑO");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 15, 364, 30);
        contentPane.add(lblTitle);

        JLabel lblName = new JLabel("Nombre:");
        lblName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBounds(40, 70, 300, 20);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBounds(40, 95, 304, 35);
        if (sizeModel != null && sizeModel.getName() != null) {
            txtName.setText(sizeModel.getName());
        }
        contentPane.add(txtName);

        JLabel lblMultiplier = new JLabel("Multiplicador de Precio (ej. 1.5):");
        lblMultiplier.setForeground(Colors.MOCHA_BEAN.getColor());
        lblMultiplier.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMultiplier.setBounds(40, 140, 300, 20);
        contentPane.add(lblMultiplier);

        txtMultiplier = new JTextField();
        txtMultiplier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMultiplier.setBounds(40, 165, 304, 35);
        if (sizeModel != null && sizeModel.getPriceMultiplier() != null) {
            txtMultiplier.setText(sizeModel.getPriceMultiplier().toString());
        }
        contentPane.add(txtMultiplier);

        JLabel lblMultiplierHelp = new JLabel("<html>Ej: Si el producto base cuesta $1000 y el multiplicador es 1.5, el precio final será $1500.</html>");
        lblMultiplierHelp.setForeground(Colors.MOCHA_BEAN.getColor());
        lblMultiplierHelp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMultiplierHelp.setBounds(40, 200, 304, 40);
        contentPane.add(lblMultiplierHelp);

        btnSave = new JButton("Guardar");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBounds(40, 250, 145, 40);
        btnSave.setFocusPainted(false);
        btnSave.setBorder(null);
        contentPane.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBounds(199, 250, 145, 40);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(null);
        contentPane.add(btnCancel);
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextField getTxtMultiplier() {
        return txtMultiplier;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public Long getCurrentSizeId() {
        return currentSizeId;
    }
}
