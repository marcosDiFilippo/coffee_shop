package services;

import daos.UserDAO;
import dtos.LoginDTO;
import exceptions.InvalidDataException;
import exceptions.UserAuthenticationException;
import models.User;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getUsername().isEmpty() || loginDTO.getPassword().isEmpty()) {
            throw new InvalidDataException("Debe ingresar usuario y contraseña.");
        }
        
        User user = userDAO.authenticate(loginDTO);
        
        if (user != null && user.isActive()) {
            return user;
        }
        
        throw new UserAuthenticationException("Credenciales incorrectas o usuario inactivo.");
    }
}
