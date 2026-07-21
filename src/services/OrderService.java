package services;

import config.DatabaseConnection;
import daos.OrderDAO;
import daos.OrderItemDAO;
import daos.UserDAO;
import dtos.OrderItemDTO;
import dtos.UserDTO;
import models.Order;
import models.OrderItem;
import models.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    public OrderService() {
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
    }

    public boolean confirmOrder(UserDTO customerDTO, List<OrderItemDTO> cart, Long employeeId, BigDecimal total) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            User customer = new User();
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhone(customerDTO.getPhone());
            customer.setActive(true);
            customer.setRol("CUSTOMER");

            Long customerId = userDAO.insertCustomer(conn, customer);
            if (customerId == null) {
                conn.rollback();
                return false;
            }

            Order order = new Order();
            order.setCustomerId(customerId);
            order.setEmployeeId(employeeId);
            order.setStatus("PENDING");
            order.setTotal(total);

            Long orderId = orderDAO.insertWithConnection(conn, order);
            if (orderId == null) {
                conn.rollback();
                return false;
            }

            for (OrderItemDTO dto : cart) {
                OrderItem item = new OrderItem();
                item.setOrderId(orderId);
                item.setProductId(dto.getProduct().getId());
                if (dto.getSize() != null) {
                    item.setSizeId(dto.getSize().getId());
                }
                item.setQuantity(dto.getQuantity());
                
                BigDecimal multiplier = (dto.getSize() != null) ? dto.getSize().getPriceMultiplier() : BigDecimal.ONE;
                item.setUnitPrice(dto.getProduct().getBasePrice().multiply(multiplier));
                item.setSubtotal(dto.getSubtotal());
                
                orderItemDAO.insertWithConnection(conn, item);
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemDAO.findByOrderId(orderId);
    }

    public boolean updateOrderStatus(Long orderId, String newStatus) {
        Order currentOrder = orderDAO.findById(orderId);
        if (currentOrder == null) {
            throw new IllegalArgumentException("La orden especificada no existe.");
        }
        
        String currentStatus = currentOrder.getStatus();
        
        if (newStatus.equals("CANCELLED")) {
            if (currentStatus.equals("DELIVERED") || currentStatus.equals("CANCELLED")) {
                throw new IllegalArgumentException("No se puede cancelar un pedido que ya ha sido entregado o cancelado.");
            }
        } else {
            int currentIndex = getStatusIndex(currentStatus);
            int newIndex = getStatusIndex(newStatus);
            if (newIndex <= currentIndex) {
                throw new IllegalArgumentException("El nuevo estado debe representar un avance en el proceso y no se puede volver atrás.");
            }
        }
        
        return orderDAO.updateStatus(orderId, newStatus);
    }

    private int getStatusIndex(String status) {
        switch (status) {
            case "PENDING": return 0;
            case "PREPARING": return 1;
            case "READY": return 2;
            case "DELIVERED": return 3;
            default: return -1;
        }
    }
}
