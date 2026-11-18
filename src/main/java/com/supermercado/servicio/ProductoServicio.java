package com.supermercado.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Proveedor;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;

// CAPA DE SERVICIO
// Reglas de negocio de los productos: ABM, reponer, busquedas y filtros.
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

    // El proveedor es obligatorio: se busca por id y se asigna antes de validar.
    public void registrar(Producto producto, int proveedorId) {
        if (proveedorId <= 0) {
            throw new NegocioException("Elegí un proveedor para el producto");
        }
        Proveedor proveedor = repoProveedores.consultar(Proveedor.class, proveedorId);
        producto.setProveedor(proveedor);
        validar(producto);
        repoProductos.guardar(producto);
    }

    // El producto ya tiene id, asi que guardar lo actualiza.
    public void modificar(Producto producto) {
        validar(producto);
        repoProductos.guardar(producto);
    }

    // No se borra un producto con ventas: quedarian ventas apuntando a nada.
    public void eliminar(int id) {
        boolean tieneVentas = repoVentas.listar(Venta.class).stream()
                .anyMatch(v -> v.getProducto().getId() == id);
        if (tieneVentas) {
            throw new NegocioException("No se puede eliminar un producto que tiene ventas registradas");
        }
        repoProductos.eliminar(Producto.class, id);
    }

    // Operacion de negocio: sumar stock.
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

    // Filtros con streams, como los de la Tienda del proyecto anterior.
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

    // Validacion de campos. Con BigDecimal se compara con compareTo (no con < o >).
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