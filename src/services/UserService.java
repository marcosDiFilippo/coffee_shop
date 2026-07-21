package services;

import config.DatabaseConnection;
import daos.UserDAO;
import dtos.UserDTO;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<UserDTO> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean saveUser(UserDTO dto) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            User user = new User();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setRol(dto.getRol());

            if (dto.getId() == null) {
                user.setActive(true);
                Long userId = userDAO.insertUserWithConnection(conn, user);
                if (userId == null) {
                    conn.rollback();
                    return false;
                }
                userDAO.insertCredentials(conn, userId, dto.getUsername(), dto.getPassword());
            } else {
                user.setId(dto.getId());
                userDAO.updateUserWithConnection(conn, user);
                userDAO.updateCredentials(conn, user.getId(), dto.getUsername(), dto.getPassword());
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean toggleUserStatus(Long id, boolean currentActive) {
        return userDAO.toggleActive(id, !currentActive);
    }
}
