-- Ejecutar con un usuario administrador (por ejemplo root) antes de iniciar la aplicacion.
-- Las tablas las crea DataNucleus automaticamente al ejecutar el programa.

CREATE DATABASE IF NOT EXISTS supermercado;
CREATE USER IF NOT EXISTS 'programacion2'@'localhost' IDENTIFIED BY 'Prog2026';
GRANT ALL PRIVILEGES ON supermercado.* TO 'programacion2'@'localhost';
FLUSH PRIVILEGES;