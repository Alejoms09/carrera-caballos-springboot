# Juego de Caballos Multiusuario

Aplicacion Spring Boot + Thymeleaf + MySQL para apuestas de carrera de caballos con reglas de puntos.

## Funcionalidades

- Registro y login con sesion web.
- Cada usuario nuevo recibe 1000 puntos.
- Limite de plataforma: 4 grupos de 4 usuarios (16 usuarios maximo).
- Apuesta de valor variable.
- Si gana la apuesta: pago de apuesta x5.
- Compra ilimitada de puntos:
  - 1000 puntos por 10.000 COP por paquete.
- Historial persistente de apuestas y compras.
- Replay progresivo de cada apuesta en pantalla dedicada.

## Stack

- Java 17
- Spring Boot 3.5.11
- Spring Web + Thymeleaf
- Spring Data JPA
- MySQL Connector/J
- Maven Wrapper

## Base de datos local (XAMPP)

1. Inicia MySQL en XAMPP.
2. Importa el script:
   - `database/mysql/juegodcaballos_mysql.sql`
3. Ejecuta la app:

```powershell
.\mvnw.cmd spring-boot:run
```

URL local:

```text
http://localhost:8080
```

## Railway

### 1) Servicio Web

Repo: `Alejoms09/carrera-caballos-springboot`
Branch: `main`

Este repo incluye `railway.json` con start command:

```text
java -jar target/*.jar
```

### 2) Servicio MySQL en Railway

Crea un servicio MySQL dentro del mismo proyecto de Railway.

### 3) Variables en el servicio Web

Asigna variables referenciando el servicio MySQL:

- `MYSQLHOST`
- `MYSQLPORT`
- `MYSQLDATABASE`
- `MYSQLUSER`
- `MYSQLPASSWORD`

Opcional:

- `JDBC_DATABASE_URL` (si quieres forzar una URL JDBC completa)

El puerto `PORT` lo inyecta Railway automaticamente.

### 4) Verificar que deployo la version nueva

Abre:

```text
https://<tu-dominio-railway>/version
```

Debe mostrar:

- Nombre de app
- `commit=<hash>`

Si no coincide con el ultimo commit de `main`, haz `Redeploy latest commit` y marca `Clear build cache`.

### 5) Migrar datos desde tu XAMPP a Railway (opcional)

Exportar local:

```powershell
C:\xampp\mysql\bin\mysqldump.exe -u root juegodcaballos > juegodcaballos_dump.sql
```

Importar en Railway MySQL (usando host/port/user/pass/db de Railway):

```powershell
mysql -h <MYSQLHOST> -P <MYSQLPORT> -u <MYSQLUSER> -p <MYSQLDATABASE> < juegodcaballos_dump.sql
```

## Pruebas

```powershell
.\mvnw.cmd test
```
