package views.products;

import constants.Colors;
import controllers.ProductController;
import dtos.ProductDTO;
import models.Category;
import services.CategoryService;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.util.List;

public class ProductFormDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtBasePrice;
    private JComboBox<CategoryItem> cbCategories;
    private JButton btnSave;
    private JButton btnCancel;

    public ProductFormDialog(Frame parent, ProductController controller, ProductDTO dto) {
        super(parent, true);
        setTitle("Formulario de Producto");
        setBounds(100, 100, 480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("DATOS DEL PRODUCTO");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 444, 30);
        contentPane.add(lblTitle);

        JLabel lblName = new JLabel("Nombre:");
        lblName.setForeground(Colors.MOCHA_BEAN.getColor());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBounds(30, 70, 404, 20);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBounds(30, 95, 404, 30);
        contentPane.add(txtName);

        JLabel lblDescription = new JLabel("Descripción:");
        lblDescription.setForeground(Colors.MOCHA_BEAN.getColor());
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescription.setBounds(30, 135, 404, 20);
        contentPane.add(lblDescription);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtDescription);
        scrollPane.setBounds(30, 160, 404, 50);
        contentPane.add(scrollPane);
        
        JLabel lblPrice = new JLabel("Precio Base:");
        lblPrice.setForeground(Colors.MOCHA_BEAN.getColor());
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrice.setBounds(30, 220, 190, 20);
        contentPane.add(lblPrice);
        
        txtBasePrice = new JTextField();
        txtBasePrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBasePrice.setBounds(30, 245, 190, 30);
        contentPane.add(txtBasePrice);
        
        JLabel lblCategory = new JLabel("Categoría:");
        lblCategory.setForeground(Colors.MOCHA_BEAN.getColor());
        lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCategory.setBounds(244, 220, 190, 20);
        contentPane.add(lblCategory);
        
        cbCategories = new JComboBox<>();
        cbCategories.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategories.setBounds(244, 245, 190, 30);
        contentPane.add(cbCategories);
        
        CategoryService catService = new CategoryService();
        List<Category> categories = catService.getAllCategories();
        for (Category cat : categories) {
            if (cat.isActive()) {
                cbCategories.addItem(new CategoryItem(cat.getId(), cat.getName()));
            }
        }
        
        if (dto != null) {
            txtName.setText(dto.getName());
            txtDescription.setText(dto.getDescription());
            txtBasePrice.setText(dto.getBasePrice().toString());
            
            for (int i = 0; i < cbCategories.getItemCount(); i++) {
                CategoryItem item = cbCategories.getItemAt(i);
                if (item.getId().equals(dto.getCategoryId())) {
                    cbCategories.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        btnSave = new JButton("Guardar");
        btnSave.setBackground(Colors.WARM_CAPP.getColor());
        btnSave.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBounds(30, 310, 120, 40);
        btnSave.setBorder(null);
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = txtName.getText().trim();
                String description = txtDescription.getText().trim();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(ProductFormDialog.this, "El nombre del producto es obligatorio.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (name.length() > 100) {
                    JOptionPane.showMessageDialog(ProductFormDialog.this, "El nombre del producto no puede exceder los 100 caracteres.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (description.length() > 255) {
                    JOptionPane.showMessageDialog(ProductFormDialog.this, "La descripcion no puede exceder los 255 caracteres.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Double price;
                try {
                    price = new Double(txtBasePrice.getText().trim());
                    if (price <= 0) {
                        JOptionPane.showMessageDialog(ProductFormDialog.this, "El precio base debe ser mayor a 0.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductFormDialog.this, "El precio base debe ser un numero valido.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                CategoryItem selectedCat = (CategoryItem) cbCategories.getSelectedItem();
                if (selectedCat == null) {
                    JOptionPane.showMessageDialog(ProductFormDialog.this, "Debe seleccionar una categoria.", "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Long catId = selectedCat.getId();
                
                ProductDTO newDto = new ProductDTO(dto != null ? dto.getId() : null, catId, name, description, price, dto != null ? dto.isAvailable() : true);
                controller.saveProduct(newDto, ProductFormDialog.this);
            }
        });
        contentPane.add(btnSave);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Colors.MOCHA_BEAN.getColor());
        btnCancel.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.setBounds(314, 310, 120, 40);
        btnCancel.setBorder(null);
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        contentPane.add(btnCancel);
    }
    
    private class CategoryItem {
        private Long id;
        private String name;
        
        public CategoryItem(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public Long getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}
