package views;

import constants.Colors;
import controllers.ProductController;
import dtos.ProductDTO;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class ProductsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton btnAddProduct;
    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private ProductController productController;

    public ProductsPanel() {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        btnAddProduct = new JButton("Agregar Producto");
        btnAddProduct.setBackground(Colors.WARM_CAPP.getColor());
        btnAddProduct.setForeground(Colors.CREAMY_LATTE.getColor());
        btnAddProduct.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setBounds(30, 30, 180, 40);
        btnAddProduct.setBorder(null);
        btnAddProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(ProductsPanel.this);
                ProductFormDialog formDialog = new ProductFormDialog(dashboard, productController, null);
                formDialog.setVisible(true);
            }
        });
        add(btnAddProduct);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 90, 970, 530);
        add(scrollPane);

        String[] columnNames = {"ID", "Nombre", "Precio", "Estado", "Categoría", "Descripción", "CatID", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tableProducts = new JTable(tableModel);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableProducts.setRowHeight(60);
        tableProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableProducts.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        tableProducts.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());
        tableProducts.getTableHeader().setReorderingAllowed(false);

        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableProducts.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableProducts.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableProducts.getColumnModel().getColumn(7).setPreferredWidth(200);

        tableProducts.getColumnModel().getColumn(5).setMinWidth(0);
        tableProducts.getColumnModel().getColumn(5).setMaxWidth(0);
        tableProducts.getColumnModel().getColumn(5).setWidth(0);
        
        tableProducts.getColumnModel().getColumn(6).setMinWidth(0);
        tableProducts.getColumnModel().getColumn(6).setMaxWidth(0);
        tableProducts.getColumnModel().getColumn(6).setWidth(0);

        tableProducts.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Colors.WARM_CAPP.getColor());
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                return c;
            }
        });

        tableProducts.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if (value.toString().equals("Disponible")) {
                        c.setForeground(Colors.MOCHA_BEAN.getColor());
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });

        tableProducts.getColumnModel().getColumn(7).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            private JButton bEdit = new JButton("Editar");
            private JButton bToggle = new JButton("Deshabilitar");
            {
                panel.setBackground(Colors.CREAMY_LATTE.getColor());
                bEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
                bEdit.setForeground(Colors.CREAMY_LATTE.getColor());
                bEdit.setFocusPainted(false);
                bToggle.setBackground(Colors.MOCHA_BEAN.getColor());
                bToggle.setForeground(Colors.CREAMY_LATTE.getColor());
                bToggle.setFocusPainted(false);
                panel.add(bEdit);
                panel.add(bToggle);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) panel.setBackground(table.getSelectionBackground());
                else panel.setBackground(table.getBackground());
                
                Object statusValue = table.getModel().getValueAt(row, 3);
                if (statusValue != null && statusValue.toString().equals("No Disponible")) {
                    bToggle.setText("Habilitar");
                } else {
                    bToggle.setText("Deshabilitar");
                }
                return panel;
            }
        });

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        actionPanel.setBackground(Colors.CREAMY_LATTE.getColor());
        
        JButton btnTableEdit = new JButton("Editar");
        btnTableEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnTableEdit.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableEdit.setFocusPainted(false);
        btnTableEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableProducts.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    String name = (String) tableModel.getValueAt(row, 1);
                    BigDecimal price = (BigDecimal) tableModel.getValueAt(row, 2);
                    boolean active = tableModel.getValueAt(row, 3).equals("Disponible");
                    String desc = (String) tableModel.getValueAt(row, 5);
                    Long catId = (Long) tableModel.getValueAt(row, 6);
                    
                    TableCellEditor editor = tableProducts.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    ProductDTO dto = new ProductDTO(id, catId, name, desc, price, active);
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(ProductsPanel.this);
                    ProductFormDialog formDialog = new ProductFormDialog(dashboard, productController, dto);
                    formDialog.setVisible(true);
                }
            }
        });
        
        JButton btnTableToggle = new JButton("Deshabilitar");
        btnTableToggle.setBackground(Colors.MOCHA_BEAN.getColor());
        btnTableToggle.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableToggle.setFocusPainted(false);
        btnTableToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableProducts.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    boolean active = tableModel.getValueAt(row, 3).equals("Disponible");
                    
                    TableCellEditor editor = tableProducts.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    productController.toggleProduct(id, active);
                }
            }
        });
        
        actionPanel.add(btnTableEdit);
        actionPanel.add(btnTableToggle);

        tableProducts.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (isSelected) actionPanel.setBackground(table.getSelectionBackground());
                else actionPanel.setBackground(table.getBackground());

                Object statusValue = table.getModel().getValueAt(row, 3);
                if (statusValue != null && statusValue.toString().equals("No Disponible")) {
                    btnTableToggle.setText("Habilitar");
                } else {
                    btnTableToggle.setText("Deshabilitar");
                }
                return actionPanel;
            }
            @Override
            public Object getCellEditorValue() {
                return null;
            }
        });

        scrollPane.setViewportView(tableProducts);

        productController = new ProductController(this);
    }

    public JTable getTableProducts() {
        return tableProducts;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
