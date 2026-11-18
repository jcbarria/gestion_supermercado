package com.supermercado.dominio;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

// CAPA DE DOMINIO
// Subclase de Producto. SUPERCLASS_TABLE: se guarda en la tabla PRODUCTO,
// en las columnas VENCIMIENTO y REFRIGERADO (en las filas de otros tipos quedan en NULL).
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)

public class Alimento extends Producto {
    // LocalDate: solo fecha, sin hora.
    private LocalDate vencimiento; 
    private boolean refrigerado; 
    
    public Alimento (String nombre, BigDecimal precio, int stock, int stockMinimo, LocalDate vencimiento, boolean refrigerado) {
        super(nombre, precio, stock, stockMinimo);
        this.vencimiento= vencimiento; 
        this.refrigerado= refrigerado; 
    }
    // Constructor vacio para JDO.
    public Alimento() {
    }
       // Resta stock. Que haya stock suficiente lo controla VentaServicio.
       @Override
    public void vender(int cantidad) {
        stock = stock - cantidad;
    }

    @Override
    public void reponer(int cantidad) {
        stock = stock + cantidad;
        
    }
    public LocalDate getVencimiento() {
        return vencimiento;
    }
    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }
    public boolean isRefrigerado() {
        return refrigerado;
    }
    public void setRefrigerado(boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    // Coincide con el valor que usan el filtro y el formulario.
    @Override
    public String getCategoria() {
        return "Alimento";
    }


}
