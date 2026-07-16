package daos;

import models.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
