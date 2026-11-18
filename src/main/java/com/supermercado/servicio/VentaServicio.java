package com.supermercado.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;
import com.supermercado.repositorio.VentaRepositorio;

// CAPA DE SERVICIO
// La operacion principal del negocio: vender.
public class VentaServicio {

    private final VentaRepositorio repoVentas;
    private final IRepositorio<Cliente> repoClientes;
    private final IRepositorio<Producto> repoProductos;

    public VentaServicio(VentaRepositorio repoVentas,
                         IRepositorio<Cliente> repoClientes,
                         IRepositorio<Producto> repoProductos) {
        this.repoVentas = repoVentas;
        this.repoClientes = repoClientes;
        this.repoProductos = repoProductos;
    }

    // 1) valida la cantidad, 2) valida el stock, 3) valida el saldo.
    // Si algo no cumple lanza NegocioException y no se guarda nada.
    // Si todo esta bien, el repositorio registra la venta en una transaccion.
    public void vender(int clienteId, int productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad tiene que ser mayor que cero");
        }

        Cliente cliente = repoClientes.consultar(Cliente.class, clienteId);
        Producto producto = repoProductos.consultar(Producto.class, productoId);

        if (producto.getStock() < cantidad) {
            throw new NegocioException("Stock insuficiente de " + producto.getNombre()
                    + ". Pedido: " + cantidad + " | Disponible: " + producto.getStock());
        }

        // Se guarda el precio actual: si despues cambia, la venta mantiene este.
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        if (cliente.getSaldo().compareTo(total) < 0) {
            throw new NegocioException("Saldo insuficiente. El total es $" + total
                    + " y el saldo del cliente es $" + cliente.getSaldo());
        }

        repoVentas.registrarVenta(clienteId, productoId, cantidad, precioUnitario, LocalDate.now());
    }

    public List<Venta> listar() {
        return repoVentas.listar(Venta.class);
    }

    // Historial de compras: todas las ventas de ese cliente.
    public List<Venta> historialDeCliente(int clienteId) {
        return listar().stream()
                .filter(v -> v.getCliente().getId() == clienteId)
                .collect(Collectors.toList());
    }
}