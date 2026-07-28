package services;

import daos.CategoryDAO;
import dtos.CategoryDTO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Category;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public CategoryDTO saveCategory(CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la categoría no puede estar vacío.");
        }

        Category category = new Category(dto.getId(), dto.getName(), dto.getDescription(), dto.isActive(), dto.isRequiresSize());

        if (category.getId() == null) {
            category.setActive(true);
            category = categoryDAO.insert(category);
        } else {
            boolean updated = categoryDAO.update(category);
            if (!updated) {
                throw new TransactionFailedException("Fallo al actualizar la categoría.");
            }
        }

        dto.setId(category.getId());
        dto.setActive(category.isActive());
        return dto;
    }

    public boolean toggleCategoryStatus(Long id, boolean currentStatus) {
        if (id == null) {
            return false;
        }
        return categoryDAO.toggleActive(id, !currentStatus);
    }

    public boolean deleteCategoryHard(Long categoryId) {
        if (categoryId == null) {
            throw new InvalidDataException("El ID de la categoría es nulo.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            boolean success = categoryDAO.delete(conn, categoryId);
            if (success) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                throw new TransactionFailedException("Error al procesar la eliminación de la categoría.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new TransactionFailedException("Excepción SQL al intentar eliminar la categoría.");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
