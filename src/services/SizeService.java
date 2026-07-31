package services;

import daos.SizeDAO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Size;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SizeService {

    private SizeDAO dao;

    public SizeService() {
        this.dao = new SizeDAO();
    }

    public List<Size> getAllSizes() {
        return dao.findAll();
    }

    public void saveSize(Size size) {
        if (size.getName() == null || size.getName().trim().isEmpty()) {
            throw new InvalidDataException("El nombre del tamaño no puede estar vacío.");
        }
        if (size.getPriceMultiplier() == null || size.getPriceMultiplier().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("El multiplicador de precio debe ser mayor a 0.");
        }

        try {
            if (size.getId() == null) {
                dao.insert(size);
            } else {
                dao.update(size);
            }
        } catch (SQLException e) {
            throw new TransactionFailedException("Error al guardar el tamaño en la base de datos: " + e.getMessage());
        }
    }

    public void toggleSize(Long id, boolean active) {
        try {
            boolean success = dao.toggleActive(id, active);
            if (!success) {
                throw new TransactionFailedException("No se pudo actualizar el estado del tamaño.");
            }
        } catch (SQLException e) {
            throw new TransactionFailedException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    public List<Size> getActiveSizes() {
        return dao.findAllActive();
    }
}
