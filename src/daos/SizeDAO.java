package daos;

import config.DatabaseConnection;
import models.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import contracts.GetterDAO;

public class SizeDAO implements GetterDAO<Long, Size> {
    @Override
    public List<Size> findAll() {
        List<Size> sizes = new ArrayList<>();
        String query = "SELECT * FROM sizes ORDER BY price_multiplier ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             
             while (rs.next()) {
                 Size size = new Size();
                 size.setId(rs.getLong("id"));
                 size.setName(rs.getString("name"));
                 size.setPriceMultiplier(rs.getBigDecimal("price_multiplier"));
                 size.setActive(rs.getBoolean("active"));
                 sizes.add(size);
             }
        } catch (SQLException e) {
        }
        return sizes;
    }

    @Override
    public Size findById(Long key) {
        String query = "SELECT * FROM sizes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setLong(1, key);
             try (ResultSet rs = stmt.executeQuery()) {
                 if (rs.next()) {
                     Size size = new Size();
                     size.setId(rs.getLong("id"));
                     size.setName(rs.getString("name"));
                     size.setPriceMultiplier(rs.getBigDecimal("price_multiplier"));
                     size.setActive(rs.getBoolean("active"));
                     return size;
                 }
             }
        } catch (SQLException e) {
        }
        return null;
    }

    public void insert(Size size) throws SQLException {
        String query = "INSERT INTO sizes (name, price_multiplier) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, size.getName());
            stmt.setBigDecimal(2, size.getPriceMultiplier());
            stmt.executeUpdate();
        }
    }

    public void update(Size size) throws SQLException {
        String query = "UPDATE sizes SET name = ?, price_multiplier = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, size.getName());
            stmt.setBigDecimal(2, size.getPriceMultiplier());
            stmt.setLong(3, size.getId());
            stmt.executeUpdate();
        }
    }

    public boolean toggleActive(Long id, boolean active) throws SQLException {
        String query = "UPDATE sizes SET active = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, active);
            stmt.setLong(2, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Size> findAllActive() {
        List<Size> sizes = new ArrayList<>();
        String query = "SELECT * FROM sizes WHERE active = TRUE ORDER BY price_multiplier ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
             
             while (rs.next()) {
                 Size size = new Size();
                 size.setId(rs.getLong("id"));
                 size.setName(rs.getString("name"));
                 size.setPriceMultiplier(rs.getBigDecimal("price_multiplier"));
                 size.setActive(rs.getBoolean("active"));
                 sizes.add(size);
             }
        } catch (SQLException e) {
        }
        return sizes;
    }
}
