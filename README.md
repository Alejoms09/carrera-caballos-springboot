# Juego de Caballos Multiusuario

Aplicación Spring Boot + Thymeleaf + MySQL para apuestas de carrera de caballos con reglas de puntos.

## Funcionalidades implementadas

- Registro/login de usuarios con sesión web.
- Al registrarse, cada usuario recibe **1000 puntos**.
- Límite de plataforma: **4 grupos**, con **máximo 4 usuarios por grupo**.
- Apuesta de valor variable (puntos).
- Si gana la apuesta: pago de **apuesta x5**.
- Compra de puntos ilimitada:
  - **1000 puntos = 10.000 COP** por paquete.
- Historial persistente de apuestas y compras.

## Stack

- Java 17
- Spring Boot 3.5.11
- Spring Web + Thymeleaf
- Spring Data JPA
- MySQL Connector/J
- Maven Wrapper

## Configuración de base de datos

`src/main/resources/application.properties`:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/juegodcaballos?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DATABASE_USER:root}
spring.datasource.password=${DATABASE_PASSWORD:}
spring.jpa.hibernate.ddl-auto=update
```

### MySQL en XAMPP

1. Inicia MySQL desde XAMPP.
2. Importa `database/mysql/juegodcaballos_mysql.sql` en phpMyAdmin o consola.
3. Ejecuta la app localmente.

## Ejecutar local

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

URL:

```text
http://localhost:8080
```

## Pruebas

```powershell
.\mvnw.cmd test
```

## Deploy en Railway (MySQL)

Configura variables en Railway:

- `DATABASE_URL` (jdbc url de Railway MySQL)
- `DATABASE_USER`
- `DATABASE_PASSWORD`
- También funciona con `MYSQL_URL`, `MYSQLUSER`, `MYSQLPASSWORD`
- `PORT` (Railway la inyecta automáticamente)

Comandos (si Railway los solicita):

- Build: `./mvnw clean package -DskipTests`
- Start: `java -jar target/*.jar`
