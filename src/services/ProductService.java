package services;

import daos.ProductDAO;
import dtos.ProductDTO;
import models.Product;

import java.io.File;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;
    private FileService fileService;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.fileService = new FileService();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    public ProductDTO saveProduct(ProductDTO dto, File imageFile) {
        if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getCategoryId() == null || dto.getBasePrice() == null) {
            return null;
        }

        if (imageFile != null) {
            String savedPath = fileService.saveProductImage(imageFile);
            if (savedPath != null) {
                dto.setImagePath(savedPath);
            }
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBasePrice(dto.getBasePrice());
        product.setAvailable(dto.isAvailable());
        product.setImagePath(dto.getImagePath());

        if (product.getId() == null) {
            if (product.getImagePath() == null) {
                product.setImagePath("src/storage/products/default.png");
            }
            Product inserted = productDAO.insert(product);
            dto.setId(inserted.getId());
        } else {
            boolean updated = productDAO.update(product);
            if (!updated) return null;
        }

        return dto;
    }

    public boolean toggleProductAvailability(Long id, boolean currentAvailable) {
        return productDAO.toggleAvailable(id, !currentAvailable);
    }
}
