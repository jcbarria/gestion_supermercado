package com.supermercado.dominio;

import java.math.BigDecimal;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Column;

@PersistenceCapable(detachable = "true")
public class Cliente extends Persona {
    @Column(length = 12, scale = 2)
    private BigDecimal saldo;

    public Cliente(int dni, String nombre, String apellido, BigDecimal saldo) {
        super(dni, nombre, apellido);
        this.saldo = saldo;
    }

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