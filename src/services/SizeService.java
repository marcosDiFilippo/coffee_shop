package services;

import daos.SizeDAO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Size;

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
            throw new InvalidDataException("El nombre del tamaño no puede estar vacio.");
        }
        if (size.getName().trim().length() > 50) {
            throw new InvalidDataException("El nombre del tamaño no puede exceder los 50 caracteres.");
        }
        if (size.getPriceMultiplier() == null || size.getPriceMultiplier() <= 0.0) {
            throw new InvalidDataException("El multiplicador de precio debe ser mayor a 0.");
        }

        try {
            if (size.getId() == null) {
                dao.insert(size);
            } else {
                dao.update(size);
            }
        } catch (SQLException e) {
            throw new TransactionFailedException("No se pudo guardar el nuevo tamaño. Ocurrió un error inesperado.");
        }
    }

    public void toggleSize(Long id, boolean active) {
        try {
            boolean success = dao.toggleActive(id, active);
            if (!success) {
                throw new TransactionFailedException("No se pudo actualizar el estado del tamaño.");
            }
        } catch (SQLException e) {
            throw new TransactionFailedException("Ocurrió un error inesperado al intentar cambiar el estado del tamaño.");
        }
    }

    public List<Size> getActiveSizes() {
        return dao.findAllActive();
    }
}
