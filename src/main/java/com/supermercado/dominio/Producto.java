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

@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.COMPLETE_TABLE)

public abstract class Producto implements Serializable, iComercializable {
       
    //Atributos     
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private int id;
  protected String nombre; 
  @Column(length = 12, scale = 2)
  protected BigDecimal precio; 
  protected int stock; 
  protected int stockMinimo; 

  @Persistent(defaultFetchGroup = "true")
  private Proveedor proveedor;
   
    //Constructor 
 public Producto ( String nombre,BigDecimal precio, int stock, int stockMinimo ) { 
     this.nombre = nombre; 
     this.precio= precio; 
     this.stock=stock;
     this.stockMinimo= stockMinimo;
 }

 public Producto() {

 }

 public abstract String getCategoria (); 

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
