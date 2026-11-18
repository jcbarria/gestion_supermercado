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

import com.supermercado.dominio.Producto;
import com.supermercado.servicio.NegocioException;
import com.supermercado.servicio.ProductoServicio;
import com.supermercado.servicio.ProveedorServicio;

// CAPA DE CONTROLADORES
// Pantallas de productos. Usa ProductoFormulario porque Producto es abstracta.
@Controller
@RequestMapping("/productos")
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final ProveedorServicio proveedorServicio;

    public ProductoControlador(ProductoServicio productoServicio, ProveedorServicio proveedorServicio) {
        this.productoServicio = productoServicio;
        this.proveedorServicio = proveedorServicio;
    }

    // La misma pagina sirve para buscar, filtrar por categoria o ver stock bajo,
    // segun que parametro llegue en la direccion.
    @GetMapping
    public String listar(@RequestParam(required = false) String buscar,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(required = false) boolean stockBajo,
                         Model model) {
        if (stockBajo) {
            model.addAttribute("productos", productoServicio.listarStockBajo());
        } else if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("productos", productoServicio.buscarPorNombre(buscar));
        } else if (categoria != null && !categoria.isBlank()) {
            model.addAttribute("productos", productoServicio.filtrarPorCategoria(categoria));
        } else {
            model.addAttribute("productos", productoServicio.listar());
        }
        model.addAttribute("buscar", buscar);
        model.addAttribute("categoria", categoria);
        model.addAttribute("stockBajo", stockBajo);
        return "productos/lista";
    }

    // GET /productos/nuevo?tipo=Alimento : el tipo decide que campos muestra el formulario.
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam String tipo, Model model) {
        ProductoFormulario formulario = new ProductoFormulario();
        formulario.setTipo(tipo);
        model.addAttribute("producto", formulario);
        model.addAttribute("proveedores", proveedorServicio.listar());
        return "productos/formulario";
    }

    @PostMapping
    public String guardar(@ModelAttribute("producto") ProductoFormulario formulario,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            return volverAlFormulario(model, null, "Revisá los datos: precio, stock y garantía tienen que ser números");
        }
        try {
            // proveedorId es Integer: si no se eligio proveedor llega null y se pasa 0 (el servicio lo rechaza).
            int proveedorId = formulario.getProveedorId() == null ? 0 : formulario.getProveedorId();
            productoServicio.registrar(formulario.crearProducto(), proveedorId);
            redirect.addFlashAttribute("mensaje", "Producto registrado correctamente");
            return "redirect:/productos";
        } catch (NegocioException e) {
            return volverAlFormulario(model, null, e.getMessage());
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("producto", ProductoFormulario.desde(productoServicio.buscarPorId(id)));
        model.addAttribute("productoId", id);
        model.addAttribute("proveedores", proveedorServicio.listar());
        return "productos/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable int id,
                             @ModelAttribute("producto") ProductoFormulario formulario,
                             BindingResult resultado,
                             Model model,
                             RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            return volverAlFormulario(model, id, "Revisá los datos: precio, stock y garantía tienen que ser números");
        }
        try {
            if (formulario.getProveedorId() == null) {
                throw new NegocioException("Elegí un proveedor para el producto");
            }
            Producto producto = productoServicio.buscarPorId(id);
            // Se busca el producto guardado, se le copian los datos y se le asigna el proveedor elegido.
            formulario.copiarA(producto);
            producto.setProveedor(proveedorServicio.buscarPorId(formulario.getProveedorId()));
            productoServicio.modificar(producto);
            redirect.addFlashAttribute("mensaje", "Producto modificado correctamente");
            return "redirect:/productos";
        } catch (NegocioException e) {
            return volverAlFormulario(model, id, e.getMessage());
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes redirect) {
        try {
            productoServicio.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Producto eliminado");
        } catch (NegocioException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    // POST /productos/{id}/reponer : la cantidad viene del campo de la fila del listado.
    @PostMapping("/{id}/reponer")
    public String reponer(@PathVariable int id,
                          @RequestParam(defaultValue = "0") int cantidad,
                          RedirectAttributes redirect) {
        try {
            productoServicio.reponer(id, cantidad);
            redirect.addFlashAttribute("mensaje", "Stock repuesto: +" + cantidad);
        } catch (NegocioException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    // Vuelve al formulario con el error y carga de nuevo los proveedores del desplegable.
    private String volverAlFormulario(Model model, Integer productoId, String error) {
        model.addAttribute("error", error);
        model.addAttribute("productoId", productoId);
        model.addAttribute("proveedores", proveedorServicio.listar());
        return "productos/formulario";
    }
}