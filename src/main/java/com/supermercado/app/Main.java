package com.supermercado.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.supermercado.dominio.Alimento;
import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Producto;
import com.supermercado.dominio.Proveedor;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;
import com.supermercado.repositorio.Repositorio;
import com.supermercado.repositorio.VentaRepositorio;
import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.NegocioException;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.VentaServicio;

// Pruebas por consola que se usaron antes de tener la aplicacion web.
// Ya no hace falta para usar el sistema (arranca SupermercadoApplication),
// pero sirve para mostrar como se probo la persistencia y las reglas del negocio.
public class Main {
    public static void main(String[] args) {
        inicializarTablas();
        //guardarAlimento();
        // listarAlimentos();
        // buscarProductos();
        probarBloqueB();
    }

    private static void inicializarTablas() {
        String unidad = "Programacion2";
        new Repositorio<Proveedor>(unidad).listar(Proveedor.class);
        new Repositorio<Producto>(unidad).listar(Producto.class);
        new Repositorio<Cliente>(unidad).listar(Cliente.class);
        new Repositorio<Venta>(unidad).listar(Venta.class);
    }

    private static void guardarAlimento() {
        IRepositorio<Alimento> repo = new Repositorio<>("Programacion2");

        Proveedor proveedor = new Proveedor(20111222, "Juan", "Gomez", "La Serenisima", "30-12345678-9");

        Alimento leche = new Alimento("Leche entera", new BigDecimal("1500.50"), 20, 5,
                LocalDate.of(2026, 12, 1), true);
        leche.setProveedor(proveedor);

        repo.guardar(leche);
        System.out.println("Alimento guardado con id: " + leche.getId());
    }

    private static void listarAlimentos() {
        IRepositorio<Alimento> repo = new Repositorio<>("Programacion2");

        List<Alimento> alimentos = repo.listar(Alimento.class);
        for (Alimento a : alimentos) {
            a.mostrarInfo();
            System.out.println("  Proveedor: " + a.getProveedor().getEmpresa());
        }
    }

    private static void buscarProductos() {
        IRepositorio<Alimento> repo = new Repositorio<>("Programacion2");

        List<Alimento> encontrados = repo.buscarPorNombre(Alimento.class, "LECH");
        System.out.println("Encontrados: " + encontrados.size());
        for (Alimento a : encontrados) {
            a.mostrarInfo();
        }
    }

    private static void probarBloqueB() {
        String unidad = "Programacion2";
        IRepositorio<Cliente> repoClientes = new Repositorio<>(unidad);
        IRepositorio<Producto> repoProductos = new Repositorio<>(unidad);
        IRepositorio<Proveedor> repoProveedores = new Repositorio<>(unidad);
        VentaRepositorio repoVentas = new VentaRepositorio(unidad);

        ClienteServicio clientes = new ClienteServicio(repoClientes, repoVentas);
        ProductoServicio productos = new ProductoServicio(repoProductos, repoProveedores, repoVentas);
        VentaServicio ventas = new VentaServicio(repoVentas, repoClientes, repoProductos);

        System.out.println("--- 1. Registrar cliente ---");
        Cliente ana = new Cliente(30111222, "Ana", "Perez", new BigDecimal("10000.00"));
        clientes.registrar(ana);
        int anaId = ana.getId();
        System.out.println("Cliente registrado con id " + anaId);

        System.out.println("--- 2. Venta valida: 3 leches ---");
        ventas.vender(anaId, 1, 3);
        System.out.println("Stock de la leche: " + productos.buscarPorId(1).getStock());
        System.out.println("Saldo de Ana: " + clientes.buscarPorId(anaId).getSaldo());

        System.out.println("--- 3. Ventas que tienen que fallar ---");
        probarError("Cantidad cero", () -> ventas.vender(anaId, 1, 0));
        probarError("Stock insuficiente", () -> ventas.vender(anaId, 1, 100));
        probarError("Saldo insuficiente", () -> ventas.vender(anaId, 1, 15));
        probarError("Borrar producto con ventas", () -> productos.eliminar(1));
        probarError("Cliente con DNI repetido",
                () -> clientes.registrar(new Cliente(30111222, "Otra", "Persona", BigDecimal.ZERO)));

        System.out.println("--- 4. Historial de Ana ---");
        for (Venta v : ventas.historialDeCliente(anaId)) {
            System.out.println(v.getFecha() + " | " + v.getProducto().getNombre()
                    + " x" + v.getCantidad() + " | Total: $" + v.getTotal());
        }
    }

    private static void probarError(String caso, Runnable accion) {
        try {
            accion.run();
            System.out.println(caso + ": ERROR, se permitio");
        } catch (NegocioException e) {
            System.out.println(caso + ": rechazado -> " + e.getMessage());
        }
    }
}