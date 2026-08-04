package services;

import config.DatabaseConnection;
import daos.UserDAO;
import dtos.UserDTO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<UserDTO> getUsersByFilter(String filterType) {
        return userDAO.findUsersByFilter(filterType);
    }

    public User getUserByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    public boolean saveUser(UserDTO dto) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
                throw new InvalidDataException("El nombre del usuario no puede estar vacio.");
            }
            if (dto.getFirstName().length() > 50) {
                throw new InvalidDataException("El nombre del usuario no puede exceder los 50 caracteres.");
            }
            if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
                throw new InvalidDataException("El apellido del usuario no puede estar vacio.");
            }
            if (dto.getLastName().length() > 50) {
                throw new InvalidDataException("El apellido del usuario no puede exceder los 50 caracteres.");
            }
            if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new InvalidDataException("El email del usuario es invalido.");
            }
            if (dto.getEmail().length() > 100) {
                throw new InvalidDataException("El email del usuario no puede exceder los 100 caracteres.");
            }
            if (dto.getPhone() == null || !dto.getPhone().matches("^[0-9]+$")) {
                throw new InvalidDataException("El telefono del usuario es invalido. Solo se permiten numeros.");
            }
            if (dto.getPhone().length() > 20) {
                throw new InvalidDataException("El telefono del usuario no puede exceder los 20 caracteres.");
            }
            if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
                throw new InvalidDataException("El nombre de usuario no puede estar vacio.");
            }
            if (dto.getUsername().length() > 50) {
                throw new InvalidDataException("El nombre de usuario no puede exceder los 50 caracteres.");
            }

            User user = new User();
            user.setFirstName(dto.getFirstName().trim());
            user.setLastName(dto.getLastName().trim());
            user.setEmail(dto.getEmail().trim());
            user.setPhone(dto.getPhone().trim());
            user.setRol(dto.getRol());

            if (dto.getId() == null) {
                if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
                    throw new InvalidDataException("La contrasena es obligatoria para nuevos usuarios.");
                }
                user.setActive(true);
                Long userId = userDAO.insertUserWithConnection(conn, user);
                if (userId == null) {
                    conn.rollback();
                    throw new TransactionFailedException("No se pudo registrar al usuario en este momento. Verifique los datos e intente nuevamente.");
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
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new TransactionFailedException("Ocurrió un error interno al intentar guardar el usuario. Si el problema persiste, contacte al soporte técnico.");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public boolean toggleUserStatus(Long id, boolean currentActive) {
        return userDAO.toggleActive(id, !currentActive);
    }
}
