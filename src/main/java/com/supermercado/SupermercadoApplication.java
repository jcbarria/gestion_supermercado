package com.supermercado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase de arranque de Spring Boot. Se ejecuta con: mvn spring-boot:run
// @SpringBootApplication busca componentes (controladores, configuracion) en este
// paquete y en todos los de abajo. Por eso esta en com.supermercado.
@SpringBootApplication
public class SupermercadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermercadoApplication.class, args);
    }
}