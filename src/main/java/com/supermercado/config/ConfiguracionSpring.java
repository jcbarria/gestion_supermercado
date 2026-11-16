package com.supermercado.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Proveedor;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;
import com.supermercado.repositorio.Repositorio;
import com.supermercado.repositorio.VentaRepositorio;
import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.ProveedorServicio;
import com.supermercado.servicio.VentaServicio;

@Configuration
public class ConfiguracionSpring {

    private static final String UNIDAD = "Programacion2";

    // Repositorios

    @Bean
    public IRepositorio<Proveedor> repoProveedores() {
        return new Repositorio<>(UNIDAD);
    }

    @Bean
    public IRepositorio<Producto> repoProductos() {
        return new Repositorio<>(UNIDAD);
    }

    @Bean
    public IRepositorio<Cliente> repoClientes() {
        return new Repositorio<>(UNIDAD);
    }

    @Bean
    public VentaRepositorio repoVentas() {
        return new VentaRepositorio(UNIDAD);
    }

    // Servicios

    @Bean
    public ProveedorServicio proveedorServicio(IRepositorio<Proveedor> repoProveedores) {
        return new ProveedorServicio(repoProveedores);
    }

    @Bean
    public ProductoServicio productoServicio(IRepositorio<Producto> repoProductos,
                                             IRepositorio<Proveedor> repoProveedores,
                                             IRepositorio<Venta> repoVentas) {
        return new ProductoServicio(repoProductos, repoProveedores, repoVentas);
    }

    @Bean
    public ClienteServicio clienteServicio(IRepositorio<Cliente> repoClientes,
                                           IRepositorio<Venta> repoVentas) {
        return new ClienteServicio(repoClientes, repoVentas);
    }

    @Bean
    public VentaServicio ventaServicio(VentaRepositorio repoVentas,
                                       IRepositorio<Cliente> repoClientes,
                                       IRepositorio<Producto> repoProductos) {
        return new VentaServicio(repoVentas, repoClientes, repoProductos);
    }
}