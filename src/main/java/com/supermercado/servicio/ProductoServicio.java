package com.supermercado.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Proveedor;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;

public class ProductoServicio {

    private final IRepositorio<Producto> repoProductos;
    private final IRepositorio<Proveedor> repoProveedores;
    private final IRepositorio<Venta> repoVentas;

    public ProductoServicio(IRepositorio<Producto> repoProductos,
                            IRepositorio<Proveedor> repoProveedores,
                            IRepositorio<Venta> repoVentas) {
        this.repoProductos = repoProductos;
        this.repoProveedores = repoProveedores;
        this.repoVentas = repoVentas;
    }

    public void registrar(Producto producto, int proveedorId) {
        Proveedor proveedor = repoProveedores.consultar(Proveedor.class, proveedorId);
        producto.setProveedor(proveedor);
        validar(producto);
        repoProductos.guardar(producto);
    }

    public void modificar(Producto producto) {
        validar(producto);
        repoProductos.guardar(producto);
    }

    public void eliminar(int id) {
        boolean tieneVentas = repoVentas.listar(Venta.class).stream()
                .anyMatch(v -> v.getProducto().getId() == id);
        if (tieneVentas) {
            throw new NegocioException("No se puede eliminar un producto que tiene ventas registradas");
        }
        repoProductos.eliminar(Producto.class, id);
    }

    public void reponer(int id, int cantidad) {
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad a reponer tiene que ser mayor que cero");
        }
        Producto producto = buscarPorId(id);
        producto.reponer(cantidad);
        repoProductos.guardar(producto);
    }

    public List<Producto> listar() {
        return repoProductos.listar(Producto.class);
    }

    public Producto buscarPorId(int id) {
        return repoProductos.consultar(Producto.class, id);
    }

    public List<Producto> buscarPorNombre(String texto) {
        return repoProductos.buscarPorNombre(Producto.class, texto);
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        return listar().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public List<Producto> listarStockBajo() {
        return listar().stream()
                .filter(p -> p.getStock() < p.getStockMinimo())
                .collect(Collectors.toList());
    }

    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new NegocioException("El nombre es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("El precio tiene que ser mayor que cero");
        }
        if (producto.getStock() < 0 || producto.getStockMinimo() < 0) {
            throw new NegocioException("El stock y el stock mínimo no pueden ser negativos");
        }
        if (producto.getProveedor() == null) {
            throw new NegocioException("El producto tiene que tener un proveedor");
        }
    }
}