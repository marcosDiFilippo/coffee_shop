package services;

import daos.ProductDAO;
import dtos.ProductDTO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public ProductDTO saveProduct(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("El nombre del producto no puede estar vacio.");
        }
        if (dto.getName().trim().length() > 100) {
            throw new InvalidDataException("El nombre del producto no puede exceder los 100 caracteres.");
        }
        if (dto.getDescription() != null && dto.getDescription().trim().length() > 255) {
            throw new InvalidDataException("La descripcion no puede exceder los 255 caracteres.");
        }
        if (dto.getCategoryId() == null) {
            throw new InvalidDataException("La categoria es obligatoria.");
        }
        if (dto.getBasePrice() == null || dto.getBasePrice() <= 0) {
            throw new InvalidDataException("El precio base debe ser un numero mayor a 0.");
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBasePrice(dto.getBasePrice());
        product.setAvailable(dto.isAvailable());

        if (product.getId() == null) {
            Product inserted = productDAO.insert(product);
            dto.setId(inserted.getId());
        } else {
            boolean updated = productDAO.update(product);
            if (!updated) throw new TransactionFailedException("Error al actualizar el producto.");
        }

        return dto;
    }

    public boolean toggleProductAvailability(Long id, boolean currentAvailable) {
        return productDAO.toggleAvailable(id, !currentAvailable);
    }
}
