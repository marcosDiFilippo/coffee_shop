package controllers;

import dtos.OrderItemDTO;
import models.Category;
import models.Product;
import views.Dashboard;
import views.OrderPanel;
import views.ProductsByCategoryPanel;
import views.SelectCategoryPanel;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private List<OrderItemDTO> cartItems;
    private Dashboard dashboard;
    private JPanel mainContentPanel;

    public OrderController(Dashboard dashboard, JPanel mainContentPanel) {
        this.dashboard = dashboard;
        this.mainContentPanel = mainContentPanel;
        this.cartItems = new ArrayList<>();
    }

    public void startNewOrder() {
        this.cartItems.clear();
        showCategories();
    }

    public void showCategories() {
        mainContentPanel.removeAll();
        SelectCategoryPanel panel = new SelectCategoryPanel(this);
        mainContentPanel.add(panel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void showProductsByCategory(Category category) {
        mainContentPanel.removeAll();
        ProductsByCategoryPanel panel = new ProductsByCategoryPanel(this, category);
        mainContentPanel.add(panel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void showCart() {
        mainContentPanel.removeAll();
        OrderPanel panel = new OrderPanel(this, cartItems);
        mainContentPanel.add(panel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void addToCart(Product product) {
        boolean exists = false;
        for (OrderItemDTO item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                exists = true;
                break;
            }
        }
        if (!exists) {
            cartItems.add(new OrderItemDTO(product, 1));
        }
    }

    public void increaseQuantity(OrderItemDTO item) {
        item.setQuantity(item.getQuantity() + 1);
        showCart(); 
    }

    public void decreaseQuantity(OrderItemDTO item) {
        item.setQuantity(item.getQuantity() - 1);
        if (item.getQuantity() <= 0) {
            cartItems.remove(item);
        }
        showCart(); 
    }

    public void removeItem(OrderItemDTO item) {
        cartItems.remove(item);
        showCart(); 
    }

    public List<OrderItemDTO> getCartItems() {
        return cartItems;
    }

    public void processCheckout(dtos.UserDTO customerDTO, views.CustomerDialog dialog) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (OrderItemDTO item : cartItems) {
            java.math.BigDecimal itemTotal = item.getProduct().getBasePrice().multiply(new java.math.BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }
        
        Long employeeId = dashboard.getCurrentUser().getId();
        services.OrderService orderService = new services.OrderService();
        boolean success = orderService.confirmOrder(customerDTO, cartItems, employeeId, total);
        
        if (success) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Orden registrada con éxito", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            cartItems.clear();
            showCategories();
        } else {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Error al registrar la orden. Intente nuevamente.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
