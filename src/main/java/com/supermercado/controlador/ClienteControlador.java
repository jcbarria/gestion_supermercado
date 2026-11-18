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

// CAPA DE CONTROLADORES
// Pantallas de clientes: ABM, busqueda e historial de compras.
@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;
    private final VentaServicio ventaServicio;

    public ClienteControlador(ClienteServicio clienteServicio, VentaServicio ventaServicio) {
        this.clienteServicio = clienteServicio;
        this.ventaServicio = ventaServicio;
    }

    // GET /clientes?buscar=texto : si viene "buscar" filtra por nombre, si no muestra todos.
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

    // POST /clientes : alta de un cliente.
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

    // GET /clientes/{id}/editar : @PathVariable toma el id de la direccion.
    // clienteId le avisa al formulario que es una modificacion y no un alta.
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("cliente", clienteServicio.buscarPorId(id));
        model.addAttribute("clienteId", id);
        return "clientes/formulario";
    }

    // POST /clientes/{id} : guarda los cambios.
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

    // Eliminar es un POST (un formulario), no un link: un GET no deberia cambiar datos.
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

    // GET /clientes/{id}/historial : compras del cliente.
    @GetMapping("/{id}/historial")
    public String historial(@PathVariable int id, Model model) {
        model.addAttribute("cliente", clienteServicio.buscarPorId(id));
        model.addAttribute("ventas", ventaServicio.historialDeCliente(id));
        return "clientes/historial";
    }
}