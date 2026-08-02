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
import enums.OrderStatus;
import enums.UserRole;
import exceptions.InvalidDataException;
import exceptions.InvalidOrderStateException;
import exceptions.ProductUnavailableException;
import exceptions.TransactionFailedException;

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

    public boolean confirmOrder(UserDTO customerDTO, List<OrderItemDTO> cart, Long employeeId, Double total) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            User customer = new User();
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhone(customerDTO.getPhone());
            customer.setActive(true);
            customer.setRol(UserRole.CUSTOMER);

            if (userDAO.existsUserByEmail(conn, customer.getEmail())) {
                throw new InvalidDataException("El email ingresado del cliente ya existe. Por favor, ingrese otro email.");
            }

            Long customerId = userDAO.insertCustomer(conn, customer);

            if (customerId == null) {
                throw new TransactionFailedException("No se pudo registrar al nuevo cliente para esta orden. Por favor, intente nuevamente.");
            }

            Order order = new Order();
            order.setCustomerId(customerId);
            order.setEmployeeId(employeeId);
            order.setStatus(OrderStatus.PENDING);
            order.setTotal(total);

            Long orderId = orderDAO.insertWithConnection(conn, order);

            if (orderId == null) {
                throw new TransactionFailedException("No se pudo crear la orden. Ocurrió un problema interno, intente nuevamente.");
            }

            for (OrderItemDTO dto : cart) {
                if (!dto.getProduct().isAvailable()) {
                    throw new ProductUnavailableException("El producto " + dto.getProduct().getName() + " no está disponible.");
                }

                OrderItem item = new OrderItem();
                item.setOrderId(orderId);
                item.setProductId(dto.getProduct().getId());

                if (dto.getSize() != null) {
                    item.setSizeId(dto.getSize().getId());
                }

                item.setQuantity(dto.getQuantity());
                Double multiplier = (dto.getSize() != null) ? dto.getSize().getPriceMultiplier() : 1.0;
                item.setUnitPrice(dto.getProduct().getBasePrice() * multiplier);
                item.setSubtotal(dto.getSubtotal());
                
                orderItemDAO.insertWithConnection(conn, item);
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {}
            
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                
                throw new TransactionFailedException("Ocurrió un error inesperado y no se pudo confirmar el pedido.");
        } finally {
            try {
                conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (Exception ex) {
                
            }
        }
    }

    public boolean confirmOrderForExistingCustomer(Long customerId, List<OrderItemDTO> cart, Long employeeId, Double total) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            Order order = new Order();
            order.setCustomerId(customerId);
            order.setEmployeeId(employeeId);
            order.setStatus(OrderStatus.PENDING);
            order.setTotal(total);

            Long orderId = orderDAO.insertWithConnection(conn, order);

            if (orderId == null) {
                throw new TransactionFailedException("No se pudo crear la orden. Ocurrió un problema interno, intente nuevamente.");
            }

            for (OrderItemDTO dto : cart) {
                if (!dto.getProduct().isAvailable()) {
                    throw new ProductUnavailableException("El producto " + dto.getProduct().getName() + " no esta disponible.");
                }

                OrderItem item = new OrderItem();
                item.setOrderId(orderId);
                item.setProductId(dto.getProduct().getId());
                if (dto.getSize() != null) {
                    item.setSizeId(dto.getSize().getId());
                }
                item.setQuantity(dto.getQuantity());
                Double multiplier = (dto.getSize() != null) ? dto.getSize().getPriceMultiplier() : 1.0;
                item.setUnitPrice(dto.getProduct().getBasePrice() * multiplier);
                item.setSubtotal(dto.getSubtotal());
                
                orderItemDAO.insertWithConnection(conn, item);
            }

            conn.commit();
            return true;

        } catch (InvalidDataException | InvalidOrderStateException | ProductUnavailableException | TransactionFailedException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }
            throw e;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }
            throw new TransactionFailedException("Ocurrió un error inesperado y no se pudo confirmar el pedido.");
        } finally {
            try {
                conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (Exception ex) {
                
            }
        }
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemDAO.findByOrderId(orderId);
    }

    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order currentOrder = orderDAO.findById(orderId);
        if (currentOrder == null) {
            throw new InvalidOrderStateException("La orden especificada no existe.");
        }
        
        OrderStatus currentStatus = currentOrder.getStatus();
        
        if (newStatus == OrderStatus.CANCELLED) {
            if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELLED) {
                throw new InvalidOrderStateException("No se puede cancelar un pedido que ya ha sido entregado o cancelado.");
            }
        } else {
            if (newStatus.ordinal() <= currentStatus.ordinal()) {
                throw new InvalidOrderStateException("El nuevo estado debe representar un avance en el proceso y no se puede volver atrás.");
            }
        }
        
        return orderDAO.updateStatus(orderId, newStatus);
    }

    public void deleteOrderHard(Long orderId) {
        boolean success = orderDAO.deleteOrderHard(orderId);
        if (!success) {
            throw new TransactionFailedException("No se pudo eliminar la orden.");
        }
    }
}
