package controllers;

import dtos.OrderItemDTO;
import exceptions.InvalidDataException;
import exceptions.ProductUnavailableException;
import exceptions.TransactionFailedException;
import models.Category;
import models.Product;
import models.Size;
import services.OrderService;
import views.Dashboard;
import views.orders.OrderPanel;
import views.orders.ProductsByCategoryPanel;
import views.orders.SelectCategoryPanel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import services.SizeService;
import dtos.UserDTO;
import models.User;
import services.UserService;
import views.orders.CustomerDialog;

public class OrderController {
    private List<OrderItemDTO> cartItems;
    private Dashboard dashboard;
    private JPanel mainContentPanel;
    private OrderService orderService;

    public OrderController(Dashboard dashboard, JPanel mainContentPanel) {
        this.dashboard = dashboard;
        this.mainContentPanel = mainContentPanel;
        this.cartItems = new ArrayList<>();
        this.orderService = new OrderService();
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

    public Double calculateSubtotal(OrderItemDTO item) {
        Double multiplier = (item.getSize() != null) ? item.getSize().getPriceMultiplier() : 1.0;
        return item.getProduct().getBasePrice() * multiplier * item.getQuantity();
    }

    public void addToCart(Product product) {
        if (product.getCategory() != null && product.getCategory().isRequiresSize()) {
            SizeService sizeService = new SizeService();
            List<Size> sizes = sizeService.getActiveSizes();
            if (!sizes.isEmpty()) {
                String[] options = new String[sizes.size()];
                for (int i = 0; i < sizes.size(); i++) {
                    options[i] = sizes.get(i).getName();
                }
                int choice = JOptionPane.showOptionDialog(
                        dashboard,
                        "Seleccione el tamaño para " + product.getName() + ":",
                        "Selección de Tamaño",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (choice >= 0) {
                    Size selectedSize = sizes.get(choice);
                    addOrUpdateItem(product, selectedSize);
                }
            } else {
                addOrUpdateItem(product, null);
            }
        } else {
            addOrUpdateItem(product, null);
        }
    }

    private void addOrUpdateItem(Product product, Size size) {
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
        JOptionPane.showMessageDialog(dashboard, "Producto agregado al carrito.");
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

    public void processCheckout(UserDTO customerDTO, CustomerDialog dialog) {
        Double total = 0.0;
        for (OrderItemDTO item : cartItems) {
            total += item.getSubtotal();
        }
        
        Long employeeId = dashboard.getCurrentUser().getId();
        try {
            this.orderService.confirmOrder(customerDTO, cartItems, employeeId, total);
            JOptionPane.showMessageDialog(dialog, "Orden registrada con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            cartItems.clear();
            showCategories();
        } catch (ProductUnavailableException | TransactionFailedException | InvalidDataException e) {
            JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSearchExistingCustomer(CustomerDialog dialog) {
        String phone = JOptionPane.showInputDialog(dialog, "Ingrese el numero de telefono del cliente registrado (solo numeros):", "Buscar Cliente", JOptionPane.QUESTION_MESSAGE);
        if (phone == null) return;
        phone = phone.trim();
        if (!phone.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(dialog, "Formato de telefono invalido. Solo se permiten numeros.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserService userService = new UserService();
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            JOptionPane.showMessageDialog(dialog, "El cliente no se encuentra registrado en el sistema.", "Cliente no encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Double total = 0.0;
        for (OrderItemDTO item : cartItems) {
            total += item.getSubtotal();
        }
        
        Long employeeId = dashboard.getCurrentUser().getId();
        try {
            this.orderService.confirmOrderForExistingCustomer(user.getId(), cartItems, employeeId, total);
            JOptionPane.showMessageDialog(dialog, "Orden registrada con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            cartItems.clear();
            showCategories();
        } catch (ProductUnavailableException | TransactionFailedException | InvalidDataException e) {
            JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
