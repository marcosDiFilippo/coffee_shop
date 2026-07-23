package daos;

import config.DatabaseConnection;
import models.Category;
import models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import contracts.GetterDAO;

public class ProductDAO implements GetterDAO<Long, Product> {

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, c.name as category_name, c.description as category_desc, c.active as category_active, c.requires_size as category_requires_size " +
                       "FROM products p INNER JOIN categories c ON p.category_id = c.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setCategoryId(rs.getLong("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setBasePrice(rs.getBigDecimal("base_price"));
                product.setAvailable(rs.getBoolean("available"));
                
                if (rs.getTimestamp("created_at") != null) {
                    product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }

                Category cat = new Category();
                cat.setId(rs.getLong("category_id"));
                cat.setName(rs.getString("category_name"));
                cat.setDescription(rs.getString("category_desc"));
                cat.setActive(rs.getBoolean("category_active"));
                cat.setRequiresSize(rs.getBoolean("category_requires_size"));
                product.setCategory(cat);

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public Product findById(Long key) {
        String query = "SELECT p.*, c.name as category_name, c.description as category_desc, c.active as category_active, c.requires_size as category_requires_size " +
                       "FROM products p INNER JOIN categories c ON p.category_id = c.id WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setCategoryId(rs.getLong("category_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setBasePrice(rs.getBigDecimal("base_price"));
                    product.setAvailable(rs.getBoolean("available"));
                    
                    if (rs.getTimestamp("created_at") != null) {
                        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    if (rs.getTimestamp("updated_at") != null) {
                        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }

                    Category cat = new Category();
                    cat.setId(rs.getLong("category_id"));
                    cat.setName(rs.getString("category_name"));
                    cat.setDescription(rs.getString("category_desc"));
                    cat.setActive(rs.getBoolean("category_active"));
                    cat.setRequiresSize(rs.getBoolean("category_requires_size"));
                    product.setCategory(cat);

                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product insert(Product product) {
        String query = "INSERT INTO products (category_id, name, description, base_price, available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setBigDecimal(4, product.getBasePrice());
            stmt.setBoolean(5, product.isAvailable());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    public boolean update(Product product) {
        String query = "UPDATE products SET category_id = ?, name = ?, description = ?, base_price = ?, available = ? WHERE id = ?";
        boolean updated = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setBigDecimal(4, product.getBasePrice());
            stmt.setBoolean(5, product.isAvailable());
            stmt.setLong(6, product.getId());

            updated = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updated;
    }

    public boolean toggleAvailable(Long id, boolean available) {
        String query = "UPDATE products SET available = ? WHERE id = ?";
        boolean toggled = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, available);
            stmt.setLong(2, id);

            toggled = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toggled;
    }
}
