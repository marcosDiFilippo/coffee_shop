package daos;

import config.DatabaseConnection;
import dtos.LoginDTO;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import dtos.UserDTO;
import enums.UserRole;
import contracts.GetterDAO;

public class UserDAO implements GetterDAO<Long, User> {
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
                    user.setRol(UserRole.fromString(rs.getString("rol")));
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

    @Override
    public User findById(Long key) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("active"));
                    user.setRol(UserRole.fromString(rs.getString("rol")));
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
            stmt.setString(6, user.getRol().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public List<UserDTO> findAllDTOs() {
        List<UserDTO> users = new ArrayList<>();
        String query = "SELECT u.*, uc.username FROM users u LEFT JOIN user_credentials uc ON u.id = uc.user_id WHERE u.rol IN ('EMPLOYEE', 'MANAGER', 'ADMIN')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getLong("id"));
                dto.setFirstName(rs.getString("first_name"));
                dto.setLastName(rs.getString("last_name"));
                dto.setEmail(rs.getString("email"));
                dto.setPhone(rs.getString("phone"));
                dto.setActive(rs.getBoolean("active"));
                dto.setRol(UserRole.fromString(rs.getString("rol")));
                dto.setUsername(rs.getString("username"));
                users.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setActive(rs.getBoolean("active"));
                user.setRol(UserRole.fromString(rs.getString("rol")));
                if (rs.getTimestamp("created_at") != null) {
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Long insertUserWithConnection(Connection conn, User user) throws SQLException {
        return insertCustomer(conn, user);
    }

    public void insertCredentials(Connection conn, Long userId, String username, String password) throws SQLException {
        String query = "INSERT INTO user_credentials (user_id, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
        }
    }

    public void updateUserWithConnection(Connection conn, User user) throws SQLException {
        String query = "UPDATE users SET first_name=?, last_name=?, email=?, phone=?, rol=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRol().name());
            stmt.setLong(6, user.getId());
            stmt.executeUpdate();
        }
    }

    public void updateCredentials(Connection conn, Long userId, String username, String password) throws SQLException {
        if (password != null && !password.isEmpty()) {
            String query = "UPDATE user_credentials SET username=?, password=? WHERE user_id=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setLong(3, userId);
                stmt.executeUpdate();
            }
        } else {
            String query = "UPDATE user_credentials SET username=? WHERE user_id=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setLong(2, userId);
                stmt.executeUpdate();
            }
        }
    }

    public boolean toggleActive(Long id, boolean active) {
        String query = "UPDATE users SET active = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, active);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
