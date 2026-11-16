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

    public List<Venta> historialDeCliente(int clienteId) {
        return listar().stream()
                .filter(v -> v.getCliente().getId() == clienteId)
                .collect(Collectors.toList());
    }
}