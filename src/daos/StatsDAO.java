package daos;

import config.DatabaseConnection;
import dtos.CategoryTopProductDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO {

    public int getTotalProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public BigDecimal getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total) FROM orders WHERE status = 'DELIVERED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                BigDecimal sum = rs.getBigDecimal(1);
                if (sum != null) {
                    return sum;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public List<CategoryTopProductDTO> getTopProductsByCategory() throws SQLException {
        List<CategoryTopProductDTO> results = new ArrayList<>();

        String sql = "SELECT c.name AS category_name, p.name AS product_name, SUM(oi.quantity) AS total_sold " +
                     "FROM products p " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "JOIN order_items oi ON p.id = oi.product_id " +
                     "GROUP BY c.id, p.id " +
                     "ORDER BY c.name ASC, total_sold DESC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                String catName = rs.getString("category_name");
                String prodName = rs.getString("product_name");
                int totalSold = rs.getInt("total_sold");

                results.add(new CategoryTopProductDTO(catName, prodName, totalSold));
            }
        }
        return results;
    }
}
