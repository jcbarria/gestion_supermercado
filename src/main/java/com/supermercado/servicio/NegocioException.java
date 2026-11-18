package com.supermercado.servicio;

// CAPA DE SERVICIO
// Excepcion para cuando no se cumple una regla del negocio (stock, saldo, DNI repetido...).
// El mensaje es el que despues se muestra en la pantalla. Extiende RuntimeException
// para no tener que declararla con throws en cada metodo.
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}