package com.supermercado.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.VentaServicio;

@Controller
public class InicioControlador {

    private final ProductoServicio productoServicio;
    private final ClienteServicio clienteServicio;
    private final VentaServicio ventaServicio;

    public InicioControlador(ProductoServicio productoServicio,
                             ClienteServicio clienteServicio,
                             VentaServicio ventaServicio) {
        this.productoServicio = productoServicio;
        this.clienteServicio = clienteServicio;
        this.ventaServicio = ventaServicio;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("cantidadProductos", productoServicio.listar().size());
        model.addAttribute("cantidadClientes", clienteServicio.listar().size());
        model.addAttribute("cantidadVentas", ventaServicio.listar().size());
        model.addAttribute("cantidadStockBajo", productoServicio.listarStockBajo().size());
        return "inicio";
    }
}