#Ballet API - Sistema de Gestión y Taquilla

API RESTful diseñada bajo una arquitectura de Monolito Modular para gestionar la cartelera, usuarios y taquilla de un sistema de Ballet.

* Java 21
* Spring Boot (Web, Security, Data JPA)
* MySQL (Base de datos)
* JWT (Autenticación y Autorización)
* Swagger / OpenAPI (Documentación)

## Despliegue Local
1. Clonar el repositorio.
2. Configurar las credenciales de la base de datos en `src/main/resources/application.properties`.
3. Ejecutar el proyecto con Maven: `mvn spring-boot:run`
4. Acceder a la documentación de Swagger en: `http://localhost:8080/swagger-ui/index.html#/`

## Arquitectura
El proyecto aplica separación por capas (Controllers, Services, Repositories) y centralización de errores globales (`@RestControllerAdvice`), preparado para una futura migración a microservicios físicos si el volumen transaccional lo requiere.
