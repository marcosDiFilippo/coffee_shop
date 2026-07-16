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

    public java.math.BigDecimal calculateSubtotal(OrderItemDTO item) {
        java.math.BigDecimal multiplier = (item.getSize() != null) ? item.getSize().getPriceMultiplier() : java.math.BigDecimal.ONE;
        return item.getProduct().getBasePrice().multiply(multiplier).multiply(new java.math.BigDecimal(item.getQuantity()));
    }

    public void addToCart(Product product) {
        if (product.getCategory() != null && product.getCategory().isRequiresSize()) {
            daos.SizeDAO sizeDAO = new daos.SizeDAO();
            List<models.Size> sizes = sizeDAO.findAll();
            if (!sizes.isEmpty()) {
                String[] options = new String[sizes.size()];
                for (int i = 0; i < sizes.size(); i++) {
                    options[i] = sizes.get(i).getName();
                }
                int choice = javax.swing.JOptionPane.showOptionDialog(
                        dashboard,
                        "Seleccione el tamaño para " + product.getName() + ":",
                        "Selección de Tamaño",
                        javax.swing.JOptionPane.DEFAULT_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (choice >= 0) {
                    models.Size selectedSize = sizes.get(choice);
                    addOrUpdateItem(product, selectedSize);
                }
            } else {
                addOrUpdateItem(product, null);
            }
        } else {
            addOrUpdateItem(product, null);
        }
    }

    private void addOrUpdateItem(Product product, models.Size size) {
        boolean exists = false;
        for (OrderItemDTO item : cartItems) {
            Long itemSizeId = item.getSize() != null ? item.getSize().getId() : null;
            Long newSizeId = size != null ? size.getId() : null;

            if (item.getProduct().getId().equals(product.getId()) &&
                ((itemSizeId == null && newSizeId == null) || (itemSizeId != null && itemSizeId.equals(newSizeId)))) {
                item.setQuantity(item.getQuantity() + 1);
                item.setSubtotal(calculateSubtotal(item));
                exists = true;
                break;
            }
        }
        if (!exists) {
            OrderItemDTO newItem = new OrderItemDTO(product, 1, size);
            newItem.setSubtotal(calculateSubtotal(newItem));
            cartItems.add(newItem);
        }
        javax.swing.JOptionPane.showMessageDialog(dashboard, "Producto agregado al carrito.");
    }

    public void increaseQuantity(OrderItemDTO item) {
        item.setQuantity(item.getQuantity() + 1);
        item.setSubtotal(calculateSubtotal(item));
        showCart(); 
    }

    public void decreaseQuantity(OrderItemDTO item) {
        item.setQuantity(item.getQuantity() - 1);
        if (item.getQuantity() <= 0) {
            cartItems.remove(item);
        } else {
            item.setSubtotal(calculateSubtotal(item));
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
            total = total.add(item.getSubtotal());
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
