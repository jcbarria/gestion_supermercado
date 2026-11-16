package com.supermercado.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.supermercado.dominio.Alimento;
import com.supermercado.dominio.Proveedor;
import com.supermercado.repositorio.IRepositorio;
import com.supermercado.repositorio.Repositorio;

public class Main {
    public static void main(String[] args) {
        //guardarAlimento();
        listarAlimentos();
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
}
