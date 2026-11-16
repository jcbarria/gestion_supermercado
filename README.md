# Gestión de stock de un supermercado

Proyecto integrador de Programación II.

## Integrantes

- Juan Barria
- Martín Eduardo Yurquina
- Franco Rivas

## Descripción

Aplicación web para que un supermercado gestione sus productos, clientes y proveedores, registre ventas con control de stock y saldo, y consulte su catálogo.

> Estado: en desarrollo. Por ahora está implementado el modelo de dominio con persistencia en MySQL mediante DataNucleus JDO.

## Funcionalidades

1. ABM de productos (ropa, alimento y electrónica), con proveedor obligatorio.
2. ABM de clientes.
3. Alta y listado de proveedores.
4. Consulta de ventas e historial de compras de cada cliente.
5. Búsqueda de productos por nombre, filtro por categoría y alerta de stock bajo.
6. Venta: valida cantidad, stock y saldo, y registra la venta en una transacción.
7. Reposición de stock.

No se puede eliminar un producto o un cliente con ventas, ni un proveedor con productos.

## Tecnologías

Java 17, Maven, DataNucleus JDO, MySQL 8. Próximamente: Spring Boot, Thymeleaf y Bootstrap.

## Cómo ejecutarlo

### Requisitos

- Java 17 (JDK)
- Maven
- MySQL 8

### Pasos

1. Clonar el repositorio:
   ```
   git clone https://github.com/jcbarria/gestion_supermercado.git
   ```
2. Crear la base de datos: abrir `database/crear_base.sql` en MySQL Workbench y ejecutarlo con un usuario administrador (por ejemplo `root`). Crea la base `supermercado` y el usuario `programacion2`.
3. Compilar y procesar las clases persistentes:
   ```
   mvn clean process-classes
   ```
4. Ejecutar la clase `com.supermercado.app.Main`.

Las tablas se crean automáticamente la primera vez que se ejecuta el programa.

### Credenciales

El usuario `programacion2` con contraseña `Prog2026` es un usuario de prueba creado por el script, con permisos solo sobre la base `supermercado` y acceso solo desde `localhost`. En un entorno real se reemplazaría por credenciales propias.