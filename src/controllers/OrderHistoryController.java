package controllers;

import models.Order;
import services.OrderService;
import views.OrderHistoryPanel;
import views.OrderManagementDialog;
import views.Dashboard;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
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
                order.getCustomerName(),
                order.getTotal().toString(),
                order.getStatus() != null ? order.getStatus().getDisplayName() : "",
                date,
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
}
