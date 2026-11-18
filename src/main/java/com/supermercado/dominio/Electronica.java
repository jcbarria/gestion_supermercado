package com.supermercado.dominio;

import java.math.BigDecimal;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

// CAPA DE DOMINIO
// Subclase de Producto. Se guarda en la tabla PRODUCTO, columnas MARCA y GARANTIA.
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)

public class Electronica extends Producto {
    private String marca;
    // Garantia en meses.
    private int garantia;

    public Electronica(String nombre, BigDecimal precio, int stock, int stockMinimo, String marca, int garantia) {
        super(nombre, precio, stock, stockMinimo);
        this.marca = marca;
        this.garantia = garantia;
    }

    // Constructor vacio para JDO.
    public Electronica() {
    }

    @Override
    public void vender(int cantidad) {
        stock = stock - cantidad;
    }

    @Override
    public void reponer(int cantidad) {
        stock = stock + cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    // Coincide con el valor que usan el filtro y el formulario.
    @Override
    public String getCategoria() {
        return "Electronica";
    }
}