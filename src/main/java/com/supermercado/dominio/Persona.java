package com.supermercado.dominio;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

// CAPA DE DOMINIO
// Clase abstracta con los datos comunes de Cliente y Proveedor.
// COMPLETE_TABLE: no existe la tabla PERSONA. Cada subclase tiene su propia tabla
// (CLIENTE y PROVEEDOR) con estos atributos incluidos.
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.COMPLETE_TABLE)
public abstract class Persona implements Serializable, Comparable<Persona> {
    //Atributos
    // Clave primaria. La genera MySQL al guardar (autoincremental), por eso no tiene setter.
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private int id;

    // El DNI no se puede repetir: la base lo impide con una restriccion UNIQUE.
    @Unique
    private int dni;
    private String nombre;
    private String apellido;

    //Constructor
    public Persona(int dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Constructor vacio: JDO lo usa para crear el objeto cuando lo lee de la base.
    public Persona() {
    }

    public void mostrarInfo() {
        System.out.println("DNI: " + dni + " | Nombre: " + nombre + " " + apellido);
    }

    // Dos personas son iguales si tienen el mismo DNI.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Persona otra = (Persona) obj;
        return this.dni == otra.dni;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(dni);
    }

    // Permite ordenar personas por apellido.
    @Override
    public int compareTo(Persona otra) {
        return this.apellido.compareToIgnoreCase(otra.apellido);
    }

    public int getId() {
        return id;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}