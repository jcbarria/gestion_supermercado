package com.supermercado.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;

// CAPA DE DOMINIO
// Cada compra de un cliente. Es la entidad que une Cliente y Producto:
// Cliente 1 a N Venta y Producto 1 a N Venta (las dos FK estan en la tabla VENTA).
// No tiene setters: una venta no se modifica despues de hecha.
@PersistenceCapable(detachable = "true")
public class Venta {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private int id;

    // FK al cliente. defaultFetchGroup trae el cliente al leer la venta.
    @Persistent(defaultFetchGroup = "true")
    private Cliente cliente;

    // FK al producto.
    @Persistent(defaultFetchGroup = "true")
    private Producto producto;

    private int cantidad;
    // Precio del producto AL MOMENTO de la venta.
    // Si despues cambia el precio, la venta mantiene el que se cobro.
    @Column(length = 12, scale = 2)
    private BigDecimal precioUnitario;
    private LocalDate fecha;

    public Venta(Cliente cliente, Producto producto, int cantidad, BigDecimal precioUnitario, LocalDate fecha) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fecha = fecha;
    }

    // Constructor vacio para JDO.
    public Venta() {
    }

    // El total no se guarda, se calcula. Con BigDecimal se multiplica con multiply (no con *).
    public BigDecimal getTotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}