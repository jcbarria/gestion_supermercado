package com.supermercado.dominio;

import java.math.BigDecimal;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Column;

// CAPA DE DOMINIO
// Cliente hereda id, dni, nombre y apellido de Persona y agrega el saldo.
// Sus compras no se guardan aca: cada compra es una Venta (Cliente 1 a N Venta).
@PersistenceCapable(detachable = "true")
public class Cliente extends Persona {
    // Dinero con BigDecimal porque double tiene errores de redondeo.
    // scale = 2: la columna guarda 2 decimales (sin esto MySQL redondeaba).
    @Column(length = 12, scale = 2)
    private BigDecimal saldo;

    public Cliente(int dni, String nombre, String apellido, BigDecimal saldo) {
        super(dni, nombre, apellido);
        this.saldo = saldo;
    }

    // Constructor vacio para JDO y para que Spring arme el objeto desde el formulario.
    public Cliente() {
    }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Saldo del cliente: " + saldo + "$");
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}