package com.supermercado.dominio;

import java.math.BigDecimal;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

// CAPA DE DOMINIO
// Subclase de Producto. Se guarda en la tabla PRODUCTO, columnas TALLE y TEMPORADA.
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
public class Ropa extends Producto {
    private String talle;
    private String temporada;

    public Ropa(String nombre, BigDecimal precio, int stock, int stockMinimo, String talle, String temporada) {
        super(nombre, precio, stock, stockMinimo);
        this.talle = talle;
        this.temporada = temporada;
    }

    // Constructor vacio para JDO.
    public Ropa() {
    }

    @Override
    public void vender(int cantidad) {
        stock = stock - cantidad;
    }

    @Override
    public void reponer(int cantidad) {
        stock = stock + cantidad;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    // Coincide con el valor que usan el filtro y el formulario.
    @Override
    public String getCategoria() {
        return "Ropa";
    }
}