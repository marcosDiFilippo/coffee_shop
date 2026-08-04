package daos;

import config.DatabaseConnection;
import models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import contracts.GetterDAO;

public class CategoryDAO implements GetterDAO<Long, Category> {

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setActive(rs.getBoolean("active"));
                category.setRequiresSize(rs.getBoolean("requires_size"));
                if (rs.getTimestamp("created_at") != null) {
                    category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                categories.add(category);
            }
        } catch (SQLException e) {
        }

        return categories;
    }

    @Override
    public Category findById(Long key) {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getLong("id"));
                    category.setName(rs.getString("name"));
                    category.setDescription(rs.getString("description"));
                    category.setActive(rs.getBoolean("active"));
                    category.setRequiresSize(rs.getBoolean("requires_size"));
                    if (rs.getTimestamp("created_at") != null) {
                        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    if (rs.getTimestamp("updated_at") != null) {
                        category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                    return category;
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Category insert(Category category) {
        String query = "INSERT INTO categories (name, description, requires_size, active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isRequiresSize());
            stmt.setBoolean(4, category.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
        }

        return category;
    }

    public boolean update(Category category) {
        String query = "UPDATE categories SET name = ?, description = ?, requires_size = ? WHERE id = ?";
        boolean updated = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isRequiresSize());
            stmt.setLong(4, category.getId());

            updated = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
        }

        return updated;
    }

    public boolean toggleActive(Long id, boolean active) {
        String query = "UPDATE categories SET active = ? WHERE id = ?";
        boolean toggled = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, active);
            stmt.setLong(2, id);

            toggled = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
        }

        return toggled;
    }

    public boolean delete(Connection conn, Long id) throws SQLException {
        List<Long> orderIds = new ArrayList<>();
        String findOrdersQuery = "SELECT DISTINCT o.id FROM orders o " +
                                 "JOIN order_items oi ON o.id = oi.order_id " +
                                 "JOIN products p ON oi.product_id = p.id " +
                                 "WHERE p.category_id = " + id;
        
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(findOrdersQuery);
            while (rs.next()) {
                orderIds.add(rs.getLong(1));
            }
            rs.close();

            if (!orderIds.isEmpty()) {
                StringBuilder ids = new StringBuilder();
                for (int i = 0; i < orderIds.size(); i++) {
                    ids.append(orderIds.get(i));
                    if (i < orderIds.size() - 1) ids.append(",");
                }
                stmt.executeUpdate("DELETE FROM order_items WHERE order_id IN (" + ids + ")");
                stmt.executeUpdate("DELETE FROM orders WHERE id IN (" + ids + ")");
            }

            return stmt.executeUpdate("DELETE FROM categories WHERE id = " + id) > 0;
        }
    }
}
