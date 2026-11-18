package com.supermercado.dominio;

// CAPA DE DOMINIO
// Excepcion del proyecto anterior. Hoy no se usa: las reglas lanzan NegocioException.
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException (String msg){
        super (msg); 
        
    }

}
