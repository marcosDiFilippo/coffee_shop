package services;

import daos.UserDAO;
import dtos.LoginDTO;
import models.User;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getUsername().isEmpty() || loginDTO.getPassword().isEmpty()) {
            return null;
        }
        
        User user = userDAO.authenticate(loginDTO);
        
        if (user != null && user.isActive()) {
            return user;
        }
        
        return null;
    }
}
