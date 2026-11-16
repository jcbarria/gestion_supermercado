package com.supermercado.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.NegocioException;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.VentaServicio;

@Controller
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaServicio ventaServicio;
    private final ClienteServicio clienteServicio;
    private final ProductoServicio productoServicio;

    public VentaControlador(VentaServicio ventaServicio,
                            ClienteServicio clienteServicio,
                            ProductoServicio productoServicio) {
        this.ventaServicio = ventaServicio;
        this.clienteServicio = clienteServicio;
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaServicio.listar());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        cargarListas(model);
        return "ventas/formulario";
    }

    @PostMapping
    public String vender(@RequestParam(required = false) Integer clienteId,
                         @RequestParam(required = false) Integer productoId,
                         @RequestParam(required = false) Integer cantidad,
                         Model model,
                         RedirectAttributes redirect) {
        try {
            if (clienteId == null || productoId == null || cantidad == null) {
                throw new NegocioException("Elegí un cliente, un producto y la cantidad");
            }
            ventaServicio.vender(clienteId, productoId, cantidad);
            redirect.addFlashAttribute("mensaje", "Venta registrada correctamente");
            return "redirect:/ventas";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clienteId", clienteId);
            model.addAttribute("productoId", productoId);
            model.addAttribute("cantidad", cantidad);
            cargarListas(model);
            return "ventas/formulario";
        }
    }

    private void cargarListas(Model model) {
        model.addAttribute("clientes", clienteServicio.listar());
        model.addAttribute("productos", productoServicio.listar());
    }
}