package com.supermercado.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.VentaServicio;

// CAPA DE CONTROLADORES
// Pagina de inicio con los contadores.
@Controller
public class InicioControlador {

    private final ProductoServicio productoServicio;
    private final ClienteServicio clienteServicio;
    private final VentaServicio ventaServicio;

    // Spring crea el controlador y le pasa los servicios (inyeccion por constructor).
    // Los controladores nunca usan repositorios directamente: siempre pasan por los servicios.
    public InicioControlador(ProductoServicio productoServicio,
                             ClienteServicio clienteServicio,
                             VentaServicio ventaServicio) {
        this.productoServicio = productoServicio;
        this.clienteServicio = clienteServicio;
        this.ventaServicio = ventaServicio;
    }

    // GET / : carga los datos en el Model y devuelve el nombre de la vista (templates/inicio.html).
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("cantidadProductos", productoServicio.listar().size());
        model.addAttribute("cantidadClientes", clienteServicio.listar().size());
        model.addAttribute("cantidadVentas", ventaServicio.listar().size());
        model.addAttribute("cantidadStockBajo", productoServicio.listarStockBajo().size());
        return "inicio";
    }
}