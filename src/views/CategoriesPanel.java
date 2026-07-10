package views;

import constants.Colors;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

public class CategoriesPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton btnAddCategory;
    private JTable tableCategories;
    private DefaultTableModel tableModel;
    private JButton btnTableEdit;
    private JButton btnTableToggle;

    public CategoriesPanel() {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        btnAddCategory = new JButton("Agregar Categoría");
        btnAddCategory.setBackground(Colors.WARM_CAPP.getColor());
        btnAddCategory.setForeground(Colors.CREAMY_LATTE.getColor());
        btnAddCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddCategory.setFocusPainted(false);
        btnAddCategory.setBounds(30, 30, 180, 40);
        btnAddCategory.setBorder(null);
        add(btnAddCategory);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 90, 970, 530);
        add(scrollPane);

        String[] columnNames = {"ID", "Nombre", "Descripción", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tableCategories = new JTable(tableModel);
        tableCategories.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCategories.setRowHeight(40);
        tableCategories.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableCategories.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        tableCategories.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());
        tableCategories.getTableHeader().setReorderingAllowed(false);
        
        tableCategories.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableCategories.getColumnModel().getColumn(0).setMaxWidth(50);
        tableCategories.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableCategories.getColumnModel().getColumn(4).setPreferredWidth(200);

        tableCategories.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if (value.toString().equals("Activa")) {
                        c.setForeground(Colors.MOCHA_BEAN.getColor());
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });

        tableCategories.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
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
                if (statusValue != null && statusValue.toString().equals("Inactiva")) {
                    bToggle.setText("Habilitar");
                } else {
                    bToggle.setText("Deshabilitar");
                }
                return panel;
            }
        });

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        actionPanel.setBackground(Colors.CREAMY_LATTE.getColor());
        
        btnTableEdit = new JButton("Editar");
        btnTableEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnTableEdit.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableEdit.setFocusPainted(false);
        
        btnTableToggle = new JButton("Deshabilitar");
        btnTableToggle.setBackground(Colors.MOCHA_BEAN.getColor());
        btnTableToggle.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableToggle.setFocusPainted(false);
        
        actionPanel.add(btnTableEdit);
        actionPanel.add(btnTableToggle);

        tableCategories.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (isSelected) actionPanel.setBackground(table.getSelectionBackground());
                else actionPanel.setBackground(table.getBackground());

                Object statusValue = table.getModel().getValueAt(row, 3);
                if (statusValue != null && statusValue.toString().equals("Inactiva")) {
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

        scrollPane.setViewportView(tableCategories);
    }

    public JButton getBtnAddCategory() {
        return btnAddCategory;
    }

    public JTable getTableCategories() {
        return tableCategories;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnTableEdit() {
        return btnTableEdit;
    }

    public JButton getBtnTableToggle() {
        return btnTableToggle;
    }
}
