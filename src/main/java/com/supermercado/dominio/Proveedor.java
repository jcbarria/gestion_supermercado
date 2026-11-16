package com.supermercado.dominio;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(detachable = "true")
public class Proveedor extends Persona {
    private String empresa;
    private String cuit;

    public Proveedor(int dni, String nombre, String apellido, String empresa, String cuit) {
        super(dni, nombre, apellido);
        this.empresa = empresa;
        this.cuit = cuit;
    }

    public Proveedor() {
    }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Proveedor de la empresa " + empresa + " | CUIT: " + cuit);
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
}