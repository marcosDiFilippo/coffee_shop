package views;

import constants.Colors;
import controllers.OrderHistoryController;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderHistoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable tableOrders;
    private DefaultTableModel tableModel;
    private OrderHistoryController controller;

    public OrderHistoryPanel() {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        JLabel lblTitle = new JLabel("HISTORIAL DE PEDIDOS");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 30, 1030, 40);
        add(lblTitle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 90, 970, 530);
        add(scrollPane);

        String[] columnNames = {"ID Orden", "Fecha", "Cliente", "Total", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tableOrders = new JTable(tableModel);
        tableOrders.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableOrders.setRowHeight(40);
        tableOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableOrders.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        tableOrders.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());
        tableOrders.getTableHeader().setReorderingAllowed(false);
        
        tableOrders.removeColumn(tableOrders.getColumnModel().getColumn(0));
        
        tableOrders.getColumnModel().getColumn(4).setPreferredWidth(120);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridBagLayout());
        actionPanel.setBackground(Colors.CREAMY_LATTE.getColor());
        
        JButton btnView = new JButton("Ver Pedido");
        btnView.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnView.setForeground(Colors.CREAMY_LATTE.getColor());
        btnView.setFocusPainted(false);
        btnView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableOrders.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    TableCellEditor editor = tableOrders.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    controller.openManagementDialog(id);
                }
            }
        });
        JButton btnDelete = new JButton("Eliminar");
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableOrders.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    TableCellEditor editor = tableOrders.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    int confirm = JOptionPane.showConfirmDialog(
                        OrderHistoryPanel.this,
                        "ADVERTENCIA: Se borrará permanentemente la orden. Esta acción no se puede deshacer. Desea continuar?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.deleteOrder(id);
                    }
                }
            }
        });
        actionPanel.add(btnView);
        actionPanel.add(btnDelete);

        tableOrders.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel(new GridBagLayout());
            private JButton button = new JButton("Ver Pedido");
            private JButton delButton = new JButton("Eliminar");
            {
                panel.setBackground(Colors.CREAMY_LATTE.getColor());
                button.setBackground(Colors.CARAMEL_ROAST.getColor());
                button.setForeground(Colors.CREAMY_LATTE.getColor());
                button.setFocusPainted(false);
                delButton.setBackground(Color.RED);
                delButton.setForeground(Color.WHITE);
                delButton.setFocusPainted(false);
                panel.add(button);
                panel.add(delButton);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) panel.setBackground(table.getSelectionBackground());
                else panel.setBackground(table.getBackground());
                return panel;
            }
        });

        tableOrders.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (isSelected) actionPanel.setBackground(table.getSelectionBackground());
                else actionPanel.setBackground(table.getBackground());
                return actionPanel;
            }
            @Override
            public Object getCellEditorValue() {
                return null;
            }
        });

        scrollPane.setViewportView(tableOrders);
        
        controller = new OrderHistoryController(this);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
