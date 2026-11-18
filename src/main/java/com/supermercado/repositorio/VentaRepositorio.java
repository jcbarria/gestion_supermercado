package com.supermercado.repositorio;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Venta;

// CAPA DE REPOSITORIO
// Repositorio de ventas: hereda todo de Repositorio y agrega registrarVenta.
public class VentaRepositorio extends Repositorio<Venta> {

    public VentaRepositorio(String persistenceUnit) {
        super(persistenceUnit);
    }

    // La venta toca tres cosas: crea la Venta, resta stock y resta saldo.
    // Las tres van en UNA transaccion: o se guardan todas o ninguna.
    public void registrarVenta(int clienteId, int productoId, int cantidad,
                               BigDecimal precioUnitario, LocalDate fecha) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            // Se buscan DENTRO de la transaccion: JDO los vigila y los cambios de stock
            // y saldo se guardan solos en el commit (no hace falta llamar a guardar).
            Cliente cliente = pm.getObjectById(Cliente.class, clienteId);
            Producto producto = pm.getObjectById(Producto.class, productoId);

            Venta venta = new Venta(cliente, producto, cantidad, precioUnitario, fecha);

            producto.vender(cantidad);
            cliente.setSaldo(cliente.getSaldo().subtract(venta.getTotal()));
            pm.makePersistent(venta);

            // Recien aca se escribe todo en la base.
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                // Si algo fallo se deshace todo: no queda una venta a medias.
                tx.rollback();
            }
            throw e;
        } finally {
            pm.close();
        }
    }
}
