# Gestion de stock - Supermercado

Proyecto integrador de Programacion II.

## Integrantes

- Juan Barria
- Martin Eduardo Yurquina
- Franco Rivas

## De que se trata

Es una aplicacion web para manejar el stock de un supermercado. Se pueden cargar productos (alimentos, ropa y electronica), clientes y proveedores, y registrar ventas controlando que haya stock y que al cliente le alcance el saldo.

Partimos del proyecto de la tienda que hicimos en el primer cuatrimestre y lo pasamos a una aplicacion web con base de datos.

## Que se puede hacer

- Alta, baja, modificacion y listado de productos. Cada producto tiene que tener un proveedor.
- Alta, baja, modificacion y listado de clientes.
- Alta y listado de proveedores.
- Registrar ventas. Si no hay stock o el cliente no tiene saldo, la venta no se hace y se muestra el error.
- Reponer stock de un producto.
- Buscar productos por nombre, filtrarlos por categoria y ver los que tienen stock bajo.
- Ver el historial de compras de cada cliente.

No se puede borrar un producto o un cliente que tenga ventas, ni un proveedor que tenga productos, para no dejar datos colgados.

## Tecnologias

Java 17, Maven, DataNucleus JDO, MySQL, Spring Boot, Thymeleaf y Bootstrap.

## Como correrlo

Hace falta tener instalado Java 17, Maven y MySQL.

1. Clonar el repo:

   git clone https://github.com/jcbarria/gestion_supermercado.git

2. Crear la base: abrir el archivo `database/crear_base.sql` en MySQL Workbench y ejecutarlo con el usuario root. Eso crea la base `supermercado` y el usuario que usa la aplicacion. Las tablas se crean solas cuando arranca el programa.

3. Desde la carpeta del proyecto, ejecutar:

   mvn spring-boot:run

4. Entrar a http://localhost:8080 desde el navegador.

El usuario de la base es `programacion2` y la contraseña `Prog2026`. Es un usuario de prueba que crea el script, solo tiene permisos sobre la base del proyecto.