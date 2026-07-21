package daos;

import models.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import config.DatabaseConnection;

public class OrderDAO {
    public Long insertWithConnection(Connection conn, Order order) throws SQLException {
        String query = "INSERT INTO orders (customer_id, employee_id, status, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, order.getCustomerId());
            if (order.getEmployeeId() != null) {
                stmt.setLong(2, order.getEmployeeId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            stmt.setString(3, order.getStatus());
            stmt.setBigDecimal(4, order.getTotal());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.first_name, u.last_name FROM orders o JOIN users u ON o.customer_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             while (rs.next()) {
                 Order order = new Order();
                 order.setId(rs.getLong("id"));
                 order.setCustomerId(rs.getLong("customer_id"));
                 order.setEmployeeId(rs.getLong("employee_id"));
                 order.setStatus(rs.getString("status"));
                 order.setTotal(rs.getBigDecimal("total"));
                 order.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
                 if (rs.getTimestamp("created_at") != null) {
                     order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                 }
                 orders.add(order);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean updateStatus(Long orderId, String newStatus) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setString(1, newStatus);
             stmt.setLong(2, orderId);
             return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Order findById(Long id) {
        String query = "SELECT o.*, u.first_name, u.last_name FROM orders o JOIN users u ON o.customer_id = u.id WHERE o.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setLong(1, id);
             try (ResultSet rs = stmt.executeQuery()) {
                 if (rs.next()) {
                     Order order = new Order();
                     order.setId(rs.getLong("id"));
                     order.setCustomerId(rs.getLong("customer_id"));
                     order.setEmployeeId(rs.getLong("employee_id"));
                     order.setStatus(rs.getString("status"));
                     order.setTotal(rs.getBigDecimal("total"));
                     order.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
                     if (rs.getTimestamp("created_at") != null) {
                         order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                     }
                     return order;
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
