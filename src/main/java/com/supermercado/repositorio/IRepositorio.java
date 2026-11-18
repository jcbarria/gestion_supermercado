package com.supermercado.repositorio;

import java.util.List;

// CAPA DE REPOSITORIO
// Interfaz generica: el "contrato" de lo que puede hacer un repositorio.
// <T> es el tipo de entidad (Producto, Cliente...). Los servicios solo conocen esta interfaz.
public interface IRepositorio<T> {

    void guardar(T objeto);

    void guardar(List<T> objetos);

    T consultar(Class<T> clase, int id);

    List<T> listar(Class<T> clase);

    // Borra sin preguntar: las reglas (no borrar si tiene ventas) estan en los servicios.
    void eliminar(Class<T> clase, int id);

    // Busca por el atributo nombre (sirve para Producto y para Persona).
    List<T> buscarPorNombre(Class<T> clase, String texto);
}