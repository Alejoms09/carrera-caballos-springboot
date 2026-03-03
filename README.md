# Carrera de Caballos con Cartas

Aplicacion web hecha con Spring Boot para simular una carrera de caballos basada en cartas de una baraja inglesa.
Cada palo representa un caballo y la carrera avanza carta por carta hasta que uno llegue a la meta.

## Contenido

1. [Descripcion del juego](#descripcion-del-juego)
2. [Reglas implementadas](#reglas-implementadas)
3. [Tecnologias](#tecnologias)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Configuracion local](#configuracion-local)
6. [Pruebas](#pruebas)
7. [Despliegue en Railway con GitHub](#despliegue-en-railway-con-github)
8. [Checklist de entrega](#checklist-de-entrega)

## Descripcion del juego

El sistema modela cuatro caballos:

- `SPADES`
- `HEARTS`
- `DIAMONDS`
- `CLUBS`

En cada turno se roba una carta del mazo:

- El caballo del palo robado avanza una casilla.
- Si un caballo alcanza la longitud de pista configurada, gana la carrera.

La aplicacion permite jugar paso a paso o correr la carrera completa de forma automatica.

## Reglas implementadas

### Regla base

1. Se crea una pista de longitud `N`.
2. Se roba una carta por turno.
3. Avanza 1 casilla el caballo del palo de esa carta.
4. Gana el primero que llegue a `N`.

### Variante de retroceso (opcional)

1. Al iniciar la carrera se asigna una carta de evento a cada casilla.
2. Cuando una casilla se pisa por primera vez, se revela su evento.
3. El caballo del palo de esa carta retrocede 1 casilla.
4. Ningun caballo puede bajar de posicion 0.

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Web
- Thymeleaf
- Maven Wrapper
- JUnit 5

## Estructura del proyecto

```text
src/main/java/com/caballito/juegodcaballos/
  JuegodcaballosApplication.java
  model/
    Suit.java
    Rank.java
    Card.java
    Deck.java
    Horse.java
    RaceConfig.java
    RaceState.java
  service/
    RaceService.java
  web/
    RaceController.java

src/main/resources/
  application.properties
  templates/
    index.html
  static/
    styles.css

src/test/java/com/caballito/juegodcaballos/
  DeckTest.java
  RaceServiceTest.java
```

## Configuracion local

### Requisitos

- JDK 17 (Temurin recomendado)
- Maven Wrapper incluido en el proyecto

### Ejecutar en desarrollo

```bash
./mvnw spring-boot:run
```

Abrir en navegador:

```text
http://localhost:8080
```

### Empaquetar y ejecutar jar

```bash
./mvnw clean package
java -jar target/*.jar
```

## Pruebas

Correr pruebas unitarias:

```bash
./mvnw test
```

Pruebas incluidas:

- `DeckTest`: valida que el mazo inicie con 52 cartas.
- `RaceServiceTest`: valida que la carrera termine con ganador y que no existan posiciones negativas.

## Despliegue en Railway con GitHub

Repositorio:

```text
https://github.com/Alejoms09/carrera-caballos-springboot.git
```

### 1. Subir cambios a GitHub

```bash
git add .
git commit -m "Proyecto final carrera de caballos"
git branch -M main
git remote add origin https://github.com/Alejoms09/carrera-caballos-springboot.git
git push -u origin main
```

Si ya existe `origin`, solo usar:

```bash
git push
```

### 2. Crear servicio en Railway

1. Entrar a Railway.
2. Crear `New Project`.
3. Elegir `Deploy from GitHub Repo`.
4. Seleccionar el repositorio `carrera-caballos-springboot`.

### 3. Comandos de build/start

Si Railway no los detecta automaticamente:

- Build Command:

```text
./mvnw clean package -DskipTests
```

- Start Command:

```text
java -jar target/*.jar
```

### 4. Puerto para Railway

Archivo `src/main/resources/application.properties`:

```properties
spring.application.name=juegodcaballos
server.port=${PORT:8080}
```

Con esto Railway usa `PORT` y localmente se mantiene `8080`.

## Checklist de entrega

- Codigo en GitHub.
- Aplicacion desplegada en Railway con enlace publico.
- Documento con analisis, diseno, implementacion y pruebas.
- Instrucciones claras para ejecucion local.
