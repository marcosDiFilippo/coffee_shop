package daos;

import models.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import config.DatabaseConnection;
import contracts.GetterDAO;

public class OrderItemDAO implements GetterDAO<Long, OrderItem> {
    @Override
    public List<OrderItem> findAll() {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT oi.*, p.name AS product_name, s.name AS size_name " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.id " +
                       "LEFT JOIN sizes s ON oi.size_id = s.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             while (rs.next()) {
                 OrderItem item = new OrderItem();
                 item.setId(rs.getLong("id"));
                 item.setOrderId(rs.getLong("order_id"));
                 item.setProductId(rs.getLong("product_id"));
                 item.setQuantity(rs.getInt("quantity"));
                 item.setUnitPrice(rs.getBigDecimal("unit_price"));
                 item.setSubtotal(rs.getBigDecimal("subtotal"));
                 item.setProductName(rs.getString("product_name"));
                 item.setSizeName(rs.getString("size_name"));
                 long sizeId = rs.getLong("size_id");
                 if (!rs.wasNull()) {
                     item.setSizeId(sizeId);
                 }
                 items.add(item);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    public void insertWithConnection(Connection conn, OrderItem item) throws SQLException {
        String query = "INSERT INTO order_items (order_id, product_id, size_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, item.getOrderId());
            stmt.setLong(2, item.getProductId());
            if (item.getSizeId() != null) {
                stmt.setLong(3, item.getSizeId());
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }
            stmt.setInt(4, item.getQuantity());
            stmt.setBigDecimal(5, item.getUnitPrice());
            stmt.setBigDecimal(6, item.getSubtotal());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public OrderItem findById(Long key) {
        String query = "SELECT oi.*, p.name AS product_name, s.name AS size_name " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.id " +
                       "LEFT JOIN sizes s ON oi.size_id = s.id " +
                       "WHERE oi.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setSubtotal(rs.getBigDecimal("subtotal"));
                    item.setProductName(rs.getString("product_name"));
                    item.setSizeName(rs.getString("size_name"));
                    long sizeId = rs.getLong("size_id");
                    if (!rs.wasNull()) {
                        item.setSizeId(sizeId);
                    }
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT oi.*, p.name AS product_name, s.name AS size_name " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.id " +
                       "LEFT JOIN sizes s ON oi.size_id = s.id " +
                       "WHERE oi.order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setLong(1, orderId);
             try (ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     OrderItem item = new OrderItem();
                     item.setId(rs.getLong("id"));
                     item.setOrderId(rs.getLong("order_id"));
                     item.setProductId(rs.getLong("product_id"));
                     item.setQuantity(rs.getInt("quantity"));
                     item.setUnitPrice(rs.getBigDecimal("unit_price"));
                     item.setSubtotal(rs.getBigDecimal("subtotal"));
                     item.setProductName(rs.getString("product_name"));
                     item.setSizeName(rs.getString("size_name"));
                     long sizeId = rs.getLong("size_id");
                     if (!rs.wasNull()) {
                         item.setSizeId(sizeId);
                     }
                     items.add(item);
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
