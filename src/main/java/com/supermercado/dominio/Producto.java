package com.supermercado.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;

// CAPA DE DOMINIO
// Clase abstracta con lo comun a todos los productos.
// Herencia en UNA SOLA TABLA (PRODUCTO): NEW_TABLE aca y SUPERCLASS_TABLE en las subclases.
// La columna TIPO (discriminador) guarda la clase de cada fila: Alimento, Ropa o Electronica.
// Se eligio asi para que cada producto tenga un id unico, porque Venta apunta a "un producto".
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME, column = "TIPO")

public abstract class Producto implements Serializable, iComercializable {
       
    //Atributos     
  // Clave primaria generada por MySQL.
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private int id;
  protected String nombre; 
  // Precio actual del producto, con 2 decimales en la base.
  @Column(length = 12, scale = 2)
  protected BigDecimal precio; 
  protected int stock; 
  protected int stockMinimo; 

  // Relacion Proveedor 1 a N Producto: en la tabla PRODUCTO es una FK al proveedor.
  // defaultFetchGroup = "true": al leer un producto tambien se trae su proveedor.
  @Persistent(defaultFetchGroup = "true")
  private Proveedor proveedor;
   
    //Constructor 
 public Producto ( String nombre,BigDecimal precio, int stock, int stockMinimo ) { 
     this.nombre = nombre; 
     this.precio= precio; 
     this.stock=stock;
     this.stockMinimo= stockMinimo;
 }

 // Constructor vacio que necesita JDO.
 public Producto() {

 }

 // Cada subclase devuelve su categoria. Se usa para filtrar.
 public abstract String getCategoria (); 

  // vender y reponer vienen de la interfaz iComercializable.
  // vender no controla el stock: eso lo valida VentaServicio antes de llamarlo.
  @Override
 public abstract void vender (int cantidad); 

  @Override
 public abstract void reponer (int cantidad); 
  
  public void mostrarInfo(){
    System.out.println ((" ["+ getCategoria() + "]") + nombre + " | Precio: $" + precio +  " | Stock: " + stock +  " | Estado: " + getEstado() );
  }

  //Getters - Setters 
  public int getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public int getStockMinimo() {
    return stockMinimo;
  }

  public void setStockMinimo(int stockMinimo) {
    this.stockMinimo = stockMinimo;
  }

  // El estado NO se guarda en la base: se calcula con el stock actual.
  // Asi nunca queda desactualizado.
  public EstadoProducto getEstado() {
    if (stock <= 0) {
      return EstadoProducto.Sin_Stock;
    }
    return EstadoProducto.Disponible;
  }
  public Proveedor getProveedor() {
    return proveedor;
  }

  public void setProveedor(Proveedor proveedor) {
    this.proveedor = proveedor;
  }


}
