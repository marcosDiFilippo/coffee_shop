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
}
