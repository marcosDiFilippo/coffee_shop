package views.sizes;

import constants.Colors;
import controllers.DrinkSizeController;
import models.Size;
import views.Dashboard;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class DrinkSizePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnNewSize;
    private DrinkSizeController controller;

    public DrinkSizePanel(Dashboard dashboard) {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setBounds(0, 0, 1030, 660);
        setLayout(null);

        JLabel lblTitle = new JLabel("GESTIÓN DE TAMAÑOS DE BEBIDAS");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(30, 20, 500, 40);
        add(lblTitle);

        btnNewSize = new JButton("Nuevo Tamaño");
        btnNewSize.setBackground(Colors.WARM_CAPP.getColor());
        btnNewSize.setForeground(Colors.CREAMY_LATTE.getColor());
        btnNewSize.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNewSize.setBounds(30, 80, 150, 40);
        btnNewSize.setFocusPainted(false);
        btnNewSize.setBorder(null);
        add(btnNewSize);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 140, 970, 480);
        add(scrollPane);

        String[] columnNames = {"ID", "Nombre", "Multiplicador de Precio", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        table.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());
        table.getTableHeader().setReorderingAllowed(false);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(0).setWidth(0);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if (value.toString().equals("Activo")) {
                        c.setForeground(Colors.MOCHA_BEAN.getColor());
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel(new GridBagLayout());
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
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) panel.setBackground(tbl.getSelectionBackground());
                else panel.setBackground(tbl.getBackground());
                
                Object statusValue = tbl.getModel().getValueAt(row, 3);
                if (statusValue != null && statusValue.toString().equals("Inactivo")) {
                    bToggle.setText("Habilitar");
                } else {
                    bToggle.setText("Deshabilitar");
                }
                return panel;
            }
        });

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridBagLayout());
        actionPanel.setBackground(Colors.CREAMY_LATTE.getColor());

        JButton btnTableEdit = new JButton("Editar");
        btnTableEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnTableEdit.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableEdit.setFocusPainted(false);
        btnTableEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    String name = (String) tableModel.getValueAt(row, 1);
                    BigDecimal multiplier = (BigDecimal) tableModel.getValueAt(row, 2);
                    
                    TableCellEditor editor = table.getCellEditor();
                    if (editor != null) editor.stopCellEditing();

                    Size sizeToEdit = new Size();
                    sizeToEdit.setId(id);
                    sizeToEdit.setName(name);
                    sizeToEdit.setPriceMultiplier(multiplier);
                    controller.openForm(sizeToEdit);
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
                int row = table.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    boolean active = tableModel.getValueAt(row, 3).equals("Activo");
                    
                    TableCellEditor editor = table.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    controller.toggleSize(id, active);
                }
            }
        });

        actionPanel.add(btnTableEdit);
        actionPanel.add(btnTableToggle);

        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
                if (isSelected) actionPanel.setBackground(tbl.getSelectionBackground());
                else actionPanel.setBackground(tbl.getBackground());
                
                Object statusValue = tbl.getModel().getValueAt(row, 3);
                if (statusValue != null && statusValue.toString().equals("Inactivo")) {
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

        scrollPane.setViewportView(table);

        controller = new DrinkSizeController(this, dashboard);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnNewSize() {
        return btnNewSize;
    }
}
