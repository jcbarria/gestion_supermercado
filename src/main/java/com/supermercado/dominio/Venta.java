package com.supermercado.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;

@PersistenceCapable(detachable = "true")
public class Venta {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private int id;

    @Persistent(defaultFetchGroup = "true")
    private Cliente cliente;

    @Persistent(defaultFetchGroup = "true")
    private Producto producto;

    private int cantidad;
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

    public Venta() {
    }

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