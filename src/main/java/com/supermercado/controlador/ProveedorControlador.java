package com.supermercado.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.supermercado.dominio.Proveedor;
import com.supermercado.servicio.NegocioException;
import com.supermercado.servicio.ProveedorServicio;

@Controller
@RequestMapping("/proveedores")
public class ProveedorControlador {

    private final ProveedorServicio proveedorServicio;

    public ProveedorControlador(ProveedorServicio proveedorServicio) {
        this.proveedorServicio = proveedorServicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorServicio.listar());
        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute("proveedor") Proveedor proveedor,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            model.addAttribute("error", "Revisá los datos: el DNI tiene que ser un número");
            return "proveedores/formulario";
        }
        try {
            proveedorServicio.registrar(proveedor);
            redirect.addFlashAttribute("mensaje", "Proveedor registrado correctamente");
            return "redirect:/proveedores";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "proveedores/formulario";
        }
    }
}