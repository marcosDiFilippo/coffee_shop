package daos;

import config.DatabaseConnection;
import dtos.LoginDTO;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
}
