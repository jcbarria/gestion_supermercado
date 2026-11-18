package com.supermercado.dominio;

// CAPA DE DOMINIO
// Interfaz del proyecto anterior: obliga a los productos a tener vender y reponer.
public interface iComercializable {
     void vender (int cantidad);
     void reponer (int cantidad); 
     
}
