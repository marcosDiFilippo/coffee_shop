package daos;

import config.DatabaseConnection;
import dtos.LoginDTO;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public User authenticate(LoginDTO loginDTO) {
        User user = null;
        String query = "SELECT u.* FROM users u INNER JOIN user_credentials uc ON u.id = uc.user_id WHERE uc.username = ? AND uc.password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, loginDTO.getUsername());
            stmt.setString(2, loginDTO.getPassword());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("active"));
                    user.setRol(rs.getString("rol"));
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    if (rs.getTimestamp("updated_at") != null) {
                        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    public Long insertCustomer(Connection conn, User user) throws SQLException {
        String query = "INSERT INTO users (first_name, last_name, email, phone, active, rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setBoolean(5, user.isActive());
            stmt.setString(6, user.getRol());

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
