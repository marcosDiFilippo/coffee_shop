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
                item.setQuantity(dto.getQuantity());
                item.setUnitPrice(dto.getProduct().getBasePrice());
                item.setSubtotal(dto.getProduct().getBasePrice().multiply(new BigDecimal(dto.getQuantity())));
                
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
}
