package services;

import daos.CategoryDAO;
import dtos.CategoryDTO;
import models.Category;

import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    public CategoryDTO saveCategory(CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            return null;
        }

        Category category = new Category(dto.getId(), dto.getName(), dto.getDescription(), dto.isActive());

        if (category.getId() == null) {
            category.setActive(true);
            category = categoryDAO.insert(category);
        } else {
            boolean updated = categoryDAO.update(category);
            if (!updated) {
                return null;
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
}
