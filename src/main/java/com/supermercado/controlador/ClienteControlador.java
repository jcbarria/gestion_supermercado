package com.supermercado.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.supermercado.dominio.Cliente;
import com.supermercado.servicio.ClienteServicio;
import com.supermercado.servicio.NegocioException;
import com.supermercado.servicio.VentaServicio;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;
    private final VentaServicio ventaServicio;

    public ClienteControlador(ClienteServicio clienteServicio, VentaServicio ventaServicio) {
        this.clienteServicio = clienteServicio;
        this.ventaServicio = ventaServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("clientes", clienteServicio.buscarPorNombre(buscar));
        } else {
            model.addAttribute("clientes", clienteServicio.listar());
        }
        model.addAttribute("buscar", buscar);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute("cliente") Cliente cliente,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            model.addAttribute("error", "Revisá los datos: el DNI y el saldo tienen que ser números");
            return "clientes/formulario";
        }
        try {
            clienteServicio.registrar(cliente);
            redirect.addFlashAttribute("mensaje", "Cliente registrado correctamente");
            return "redirect:/clientes";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "clientes/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("cliente", clienteServicio.buscarPorId(id));
        model.addAttribute("clienteId", id);
        return "clientes/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable int id,
                             @ModelAttribute("cliente") Cliente datos,
                             BindingResult resultado,
                             Model model,
                             RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            model.addAttribute("error", "Revisá los datos: el DNI y el saldo tienen que ser números");
            model.addAttribute("clienteId", id);
            return "clientes/formulario";
        }
        try {
            clienteServicio.modificar(id, datos);
            redirect.addFlashAttribute("mensaje", "Cliente modificado correctamente");
            return "redirect:/clientes";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clienteId", id);
            return "clientes/formulario";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes redirect) {
        try {
            clienteServicio.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Cliente eliminado");
        } catch (NegocioException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/historial")
    public String historial(@PathVariable int id, Model model) {
        model.addAttribute("cliente", clienteServicio.buscarPorId(id));
        model.addAttribute("ventas", ventaServicio.historialDeCliente(id));
        return "clientes/historial";
    }
}