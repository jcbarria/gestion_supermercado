package com.supermercado.servicio;

import java.util.List;

import com.supermercado.dominio.Proveedor;
import com.supermercado.repositorio.IRepositorio;

public class ProveedorServicio {

    private final IRepositorio<Proveedor> repositorio;

    public ProveedorServicio(IRepositorio<Proveedor> repositorio) {
        this.repositorio = repositorio;
    }

    public void registrar(Proveedor proveedor) {
        validar(proveedor);

        boolean dniRepetido = repositorio.listar(Proveedor.class).stream()
                .anyMatch(p -> p.getDni() == proveedor.getDni());
        if (dniRepetido) {
            throw new NegocioException("Ya existe un proveedor con el DNI " + proveedor.getDni());
        }

        repositorio.guardar(proveedor);
    }

    public List<Proveedor> listar() {
        return repositorio.listar(Proveedor.class);
    }

    public Proveedor buscarPorId(int id) {
        return repositorio.consultar(Proveedor.class, id);
    }

    private void validar(Proveedor proveedor) {
        if (proveedor.getDni() <= 0) {
            throw new NegocioException("El DNI es obligatorio");
        }
        if (estaVacio(proveedor.getNombre()) || estaVacio(proveedor.getApellido())) {
            throw new NegocioException("El nombre y el apellido son obligatorios");
        }
        if (estaVacio(proveedor.getEmpresa())) {
            throw new NegocioException("La empresa es obligatoria");
        }
        if (estaVacio(proveedor.getCuit())) {
            throw new NegocioException("El CUIT es obligatorio");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}