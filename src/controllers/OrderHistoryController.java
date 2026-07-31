package controllers;

import models.Order;
import services.OrderService;
import views.OrderHistoryPanel;
import views.OrderManagementDialog;
import views.Dashboard;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import exceptions.TransactionFailedException;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class OrderHistoryController {
    private OrderHistoryPanel panel;
    private OrderService service;

    public OrderHistoryController(OrderHistoryPanel panel) {
        this.panel = panel;
        this.service = new OrderService();
        loadOrders();
    }

    public void loadOrders() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);
        
        List<Order> orders = service.getAllOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Order order : orders) {
            String date = order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "";
            model.addRow(new Object[]{
                order.getId(),
                date,
                order.getCustomerName(),
                order.getTotal().toString(),
                order.getStatus() != null ? order.getStatus().getDisplayName() : "",
                ""
            });
        }
    }

    public void openManagementDialog(Long orderId) {
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(panel);
        OrderManagementDialog dialog = new OrderManagementDialog(dashboard, this, orderId);
        dialog.setVisible(true);
    }
    
    public OrderService getService() {
        return service;
    }

    public void deleteOrder(Long id) {
        try {
            service.deleteOrderHard(id);
            loadOrders();
        } catch (TransactionFailedException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
