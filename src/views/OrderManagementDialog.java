package views;

import constants.Colors;
import controllers.OrderHistoryController;
import models.Order;
import models.OrderItem;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrderManagementDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> cmbStatus;

    public OrderManagementDialog(Frame parent, OrderHistoryController controller, Long orderId) {
        super(parent, true);
        setTitle("Gestión de Pedido #" + orderId);
        setBounds(100, 100, 600, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Order order = null;
        for (Order o : controller.getService().getAllOrders()) {
            if (o.getId().equals(orderId)) {
                order = o;
                break;
            }
        }
        
        if (order == null) return;

        JLabel lblTitle = new JLabel("DETALLES DEL PEDIDO");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 564, 30);
        contentPane.add(lblTitle);

        JLabel lblCustomer = new JLabel("Cliente: " + order.getCustomerName());
        lblCustomer.setForeground(Colors.MOCHA_BEAN.getColor());
        lblCustomer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCustomer.setBounds(30, 60, 300, 20);
        contentPane.add(lblCustomer);

        JLabel lblTotal = new JLabel("Total: $" + order.getTotal().toString());
        lblTotal.setForeground(Colors.WARM_CAPP.getColor());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBounds(344, 60, 200, 20);
        contentPane.add(lblTotal);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 100, 524, 200);
        contentPane.add(scrollPane);

        String[] columnNames = {"Producto", "Tamaño", "Cant", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableItems = new JTable(model);
        tableItems.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableItems.setRowHeight(30);
        tableItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableItems.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        tableItems.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());

        List<OrderItem> items = controller.getService().getOrderItems(orderId);
        for (OrderItem item : items) {
            String size = item.getSizeName() != null ? item.getSizeName() : "-";
            model.addRow(new Object[]{
                item.getProductName(),
                size,
                item.getQuantity(),
                "$" + item.getSubtotal().toString()
            });
        }
        scrollPane.setViewportView(tableItems);

        JLabel lblStatus = new JLabel("Estado actual: " + order.getStatus());
        lblStatus.setForeground(Colors.MOCHA_BEAN.getColor());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setBounds(30, 320, 300, 20);
        contentPane.add(lblStatus);

        cmbStatus = new JComboBox<>(new String[]{"PENDING", "PREPARING", "READY", "DELIVERED", "CANCELLED"});
        cmbStatus.setSelectedItem(order.getStatus());
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbStatus.setBounds(30, 350, 200, 40);
        contentPane.add(cmbStatus);

        JButton btnUpdate = new JButton("Actualizar Estado");
        btnUpdate.setBackground(Colors.WARM_CAPP.getColor());
        btnUpdate.setForeground(Colors.CREAMY_LATTE.getColor());
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBounds(354, 350, 200, 40);
        btnUpdate.setBorder(null);
        btnUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newStatus = (String) cmbStatus.getSelectedItem();
                try {
                    boolean success = controller.getService().updateOrderStatus(orderId, newStatus);
                    if (success) {
                        JOptionPane.showMessageDialog(OrderManagementDialog.this, "Estado actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        controller.loadOrders();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(OrderManagementDialog.this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(OrderManagementDialog.this, ex.getMessage(), "Transición Inválida", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(btnUpdate);
        
        JButton btnClose = new JButton("Cerrar");
        btnClose.setBackground(Colors.MOCHA_BEAN.getColor());
        btnClose.setForeground(Colors.CREAMY_LATTE.getColor());
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setBounds(30, 400, 100, 40);
        btnClose.setBorder(null);
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        contentPane.add(btnClose);
    }
}
