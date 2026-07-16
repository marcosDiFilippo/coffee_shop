package daos;

import models.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO {
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
}
