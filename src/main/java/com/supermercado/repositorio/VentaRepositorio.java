package com.supermercado.repositorio;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Venta;

public class VentaRepositorio extends Repositorio<Venta> {

    public VentaRepositorio(String persistenceUnit) {
        super(persistenceUnit);
    }

    public void registrarVenta(int clienteId, int productoId, int cantidad,
                               BigDecimal precioUnitario, LocalDate fecha) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            Cliente cliente = pm.getObjectById(Cliente.class, clienteId);
            Producto producto = pm.getObjectById(Producto.class, productoId);

            Venta venta = new Venta(cliente, producto, cantidad, precioUnitario, fecha);

            producto.vender(cantidad);
            cliente.setSaldo(cliente.getSaldo().subtract(venta.getTotal()));
            pm.makePersistent(venta);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            pm.close();
        }
    }
}
