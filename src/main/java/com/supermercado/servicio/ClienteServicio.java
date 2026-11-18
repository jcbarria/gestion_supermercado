package com.supermercado.servicio;

import java.math.BigDecimal;
import java.util.List;

import com.supermercado.dominio.Cliente;
import com.supermercado.dominio.Venta;
import com.supermercado.repositorio.IRepositorio;

// CAPA DE SERVICIO
// Reglas de negocio de los clientes: ABM, DNI unico y no borrar si tiene ventas.
public class ClienteServicio {

    private final IRepositorio<Cliente> repoClientes;
    private final IRepositorio<Venta> repoVentas;

    public ClienteServicio(IRepositorio<Cliente> repoClientes, IRepositorio<Venta> repoVentas) {
        this.repoClientes = repoClientes;
        this.repoVentas = repoVentas;
    }

    public void registrar(Cliente cliente) {
        validar(cliente);
        controlarDniRepetido(cliente);
        repoClientes.guardar(cliente);
    }

    // Se busca el cliente guardado y se le copian los datos nuevos del formulario
    // (el objeto que llega del formulario no tiene id).
    public void modificar(int id, Cliente datos) {
        Cliente cliente = buscarPorId(id);
        cliente.setDni(datos.getDni());
        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setSaldo(datos.getSaldo());

        validar(cliente);
        controlarDniRepetido(cliente);
        repoClientes.guardar(cliente);
    }

    // No se borra un cliente con ventas.
    public void eliminar(int id) {
        boolean tieneVentas = repoVentas.listar(Venta.class).stream()
                .anyMatch(v -> v.getCliente().getId() == id);
        if (tieneVentas) {
            throw new NegocioException("No se puede eliminar un cliente que tiene ventas registradas");
        }
        repoClientes.eliminar(Cliente.class, id);
    }

    public List<Cliente> listar() {
        return repoClientes.listar(Cliente.class);
    }

    public Cliente buscarPorId(int id) {
        return repoClientes.consultar(Cliente.class, id);
    }

    public List<Cliente> buscarPorNombre(String texto) {
        return repoClientes.buscarPorNombre(Cliente.class, texto);
    }

    // c.getId() != cliente.getId(): al modificar, no cuenta al mismo cliente.
    private void controlarDniRepetido(Cliente cliente) {
        boolean dniRepetido = listar().stream()
                .anyMatch(c -> c.getDni() == cliente.getDni() && c.getId() != cliente.getId());
        if (dniRepetido) {
            throw new NegocioException("Ya existe un cliente con el DNI " + cliente.getDni());
        }
    }

    private void validar(Cliente cliente) {
        if (cliente.getDni() <= 0) {
            throw new NegocioException("El DNI es obligatorio");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()
                || cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new NegocioException("El nombre y el apellido son obligatorios");
        }
        if (cliente.getSaldo() == null || cliente.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El saldo no puede ser negativo");
        }
    }
}