package services;

import daos.StatsDAO;
import dtos.CategoryTopProductDTO;
import exceptions.TransactionFailedException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatsService {

    private StatsDAO statsDAO;

    public StatsService() {
        this.statsDAO = new StatsDAO();
    }

    public int getTotalProducts() {
        try {
            return statsDAO.getTotalProducts();
        } catch (SQLException e) {
            throw new TransactionFailedException("Error al obtener el total de productos de la base de datos.");
        }
    }

    public BigDecimal getTotalRevenue() {
        try {
            return statsDAO.getTotalRevenue();
        } catch (SQLException e) {
            throw new TransactionFailedException("Error al obtener los ingresos totales de la base de datos.");
        }
    }

    public List<CategoryTopProductDTO> getTopProductsByCategory() {
        try {
            List<CategoryTopProductDTO> allRanked = statsDAO.getTopProductsByCategory();
            List<CategoryTopProductDTO> topProducts = new ArrayList<>();

            //evitamos categorias duplicadas
            Set<String> categoriesUnique = new HashSet<>();

            for (CategoryTopProductDTO dto : allRanked) {
                if (!categoriesUnique.contains(dto.getCategoryName())) {
                    topProducts.add(dto);
                    categoriesUnique.add(dto.getCategoryName());
                }
            }
            
            return topProducts;
        } catch (SQLException e) {
            throw new TransactionFailedException("Error al obtener los productos más vendidos por categoría.");
        }
    }
}
