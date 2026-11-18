package com.supermercado.controlador;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.supermercado.dominio.Alimento;
import com.supermercado.dominio.Electronica;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Ropa;
import com.supermercado.servicio.NegocioException;

// Objeto del formulario de productos (no es una entidad, no se guarda en la base).
// Hace falta porque Producto es abstracta y Spring no puede crearla para cargarle los datos.
// Tiene los campos de todas las categorias y despues se convierte en la subclase que corresponde.
public class ProductoFormulario {

    private String tipo;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private int stockMinimo;
    // Integer y no int: si no se elige proveedor llega vacio (null), y un int no acepta null.
    private Integer proveedorId;

    private String talle;
    private String temporada;

    private String marca;
    // Integer por lo mismo: el campo puede quedar vacio.
    private Integer garantia;

    // Formato en el que llega la fecha del input type="date" (por ejemplo 2026-12-01).
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate vencimiento;
    private boolean refrigerado;

    // Alta: crea la subclase segun el tipo elegido.
    public Producto crearProducto() {
        if ("Alimento".equals(tipo)) {
            return new Alimento(nombre, precio, stock, stockMinimo, vencimiento, refrigerado);
        }
        if ("Ropa".equals(tipo)) {
            return new Ropa(nombre, precio, stock, stockMinimo, talle, temporada);
        }
        if ("Electronica".equals(tipo)) {
            return new Electronica(nombre, precio, stock, stockMinimo, marca, garantia == null ? 0 : garantia);
        }
        throw new NegocioException("Categoría de producto inválida");
    }

    // Modificacion: pasa los datos del formulario a un producto que ya existe.
    // "instanceof Alimento alimento" pregunta el tipo y, si coincide, ya lo convierte (Java 17).
    public void copiarA(Producto producto) {
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setStockMinimo(stockMinimo);

        if (producto instanceof Alimento alimento) {
            alimento.setVencimiento(vencimiento);
            alimento.setRefrigerado(refrigerado);
        } else if (producto instanceof Ropa ropa) {
            ropa.setTalle(talle);
            ropa.setTemporada(temporada);
        } else if (producto instanceof Electronica electronica) {
            electronica.setMarca(marca);
            electronica.setGarantia(garantia == null ? 0 : garantia);
        }
    }

    // Lo contrario: llena el formulario con un producto guardado, para editarlo.
    public static ProductoFormulario desde(Producto producto) {
        ProductoFormulario f = new ProductoFormulario();
        f.tipo = producto.getCategoria();
        f.nombre = producto.getNombre();
        f.precio = producto.getPrecio();
        f.stock = producto.getStock();
        f.stockMinimo = producto.getStockMinimo();
        f.proveedorId = producto.getProveedor() != null ? producto.getProveedor().getId() : null;

        if (producto instanceof Alimento alimento) {
            f.vencimiento = alimento.getVencimiento();
            f.refrigerado = alimento.isRefrigerado();
        } else if (producto instanceof Ropa ropa) {
            f.talle = ropa.getTalle();
            f.temporada = ropa.getTemporada();
        } else if (producto instanceof Electronica electronica) {
            f.marca = electronica.getMarca();
            f.garantia = electronica.getGarantia();
        }
        return f;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }

    public String getTalle() { return talle; }
    public void setTalle(String talle) { this.talle = talle; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }

    public LocalDate getVencimiento() { return vencimiento; }
    public void setVencimiento(LocalDate vencimiento) { this.vencimiento = vencimiento; }

    public boolean isRefrigerado() { return refrigerado; }
    public void setRefrigerado(boolean refrigerado) { this.refrigerado = refrigerado; }
}