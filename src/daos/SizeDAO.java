package daos;

import config.DatabaseConnection;
import models.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SizeDAO {
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
                 sizes.add(size);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sizes;
    }
}
