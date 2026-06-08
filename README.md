# Domestic-Snake 🐾
Es un videojuego robusto y escalable basado en el clásico Snake, desarrollado en Java utilizando el framework LibGDX. 
El proyecto implementa arquitectura orientada a objetos y patrones de diseño para ofrecer una experiencia personalizada con múltiples mascotas y entornos dinámicos.
## Características Principales 🐶
* Múltiples Mascotas: Juega como Gato, Perro o Pez mediante lógicas de personalización independientes.
* Sistema de Ítems Temáticos: El personaje recolecta objetos valiosos (ovillos de lana, huesos, conchitas) que se van acumulando físicamente en una hilera detrás de él.
* Interfaz de Usuario: Menú de configuración dinámico con selección de dificultad por mouse, navegación por teclado para las mascotas y salida rápida con la tecla ESC.
## Arquitectura y Patrones de Diseño 🐱
Para transformar el prototipo geométrico inicial en un software modular y escalable, se aplicaron los siguientes principios de ingeniería:
* Patrón Strategy (SkinStrategy): Centraliza la apariencia y características de cada mascota. Permite cambiar entre GatoStrategy, PerroStrategy y PezStrategy de forma polimórfica, modificando texturas, objetos recolectables y colores del entorno sin alterar la lógica principal del juego.
* Patrón Builder (PartidaBuilder): Permite construir una partida a partir de las opciones seleccionadas por el jugador antes de comenzar. Configura aspectos como la mascota y la dificultad, generando un objeto Partida completamente configurado e inmutable mediante el método build(), lo que desacopla el menú visual de la lógica del juego.
* Clase Abstracta y Polimorfismo (ObjetoMapa): Representa de forma genérica cualquier entidad estática del mapa, centralizando las coordenadas compartidas mediante un Vector2 y obligando a sus clases hijas (Comida y Obstaculo) a implementar el método alColisionar(), lo que permite procesar las colisiones del entorno de forma genérica.
* Separación de Responsabilidades: Serpiente encapsula el modelo lógico, las posiciones de su cuerpo y el movimiento de la mascota, mientras que PantallaJuego actúa como controlador central del juego, gestionando el tiempo, las colisiones y el renderizado.
## Requisitos e Instalación 🐟
### Requisitos Previos
* Java JDK (Versión 8)
* IDE recomendado: Eclipse
### Ejecución
1. Clona este repositorio: https://github.com/patriciaazucar8-stack/Snake-LibGDX-PUCV.git
2. Importa el proyecto en tu IDE como un proyecto existente.
3. Ejecuta la clase principal de lanzamiento llamada `DesktopLauncher.java`
## 🎮 Cómo Jugar

Sigue estas instrucciones para configurar tu partida, controlar a tu mascota y registrar tus mejores puntuaciones.

### 1. Configuración Inicial (Menú y Selector)
* **Iniciar el juego:** En la pantalla principal, presiona la pantalla para avanzar al selector.
* **Seleccionar Dificultad:** Haz clic con el **Mouse** directamente sobre las opciones de velocidad en la pantalla para ajustar la rapidez de la partida.
* **Seleccionar tu Mascota:** Utiliza los números de tu teclado para elegir tu compañero de juego:
  * `1` ➔ **Gato** (Busca lanas, arranca de los perros)
  * `2` ➔ **Perro** (Busca huesos)
  * `3` ➔ **Pez** (Busca conchitas)

### 2. Controles en la Partida
Utiliza cualquiera de las dos configuraciones de teclado disponibles simultáneamente para mover a tu mascota por el mapa:
* **Configuración Clásica:** Flechas de dirección (`Arriba`, `Abajo`, `Izquierda`, `Derecha`).
* **Configuración Alternativa:** Teclas `W` (Arriba), `S` (Abajo), `A` (Izquierda) y `D` (Derecha).
* **Meta:** Recolecta los objetos correspondientes a tu mascota para cumplir el objetivo del nivel. Al lograrlo, presiona la tecla `ENTER` para avanzar al siguiente escenario.

### 3. Pantalla de Fin de Juego (Game Over) y Tabla de Posiciones
* **Avanzar al Registro:** Cuando tu mascota se debilite y aparezca la pantalla de *Mascota Debilitada*, presiona la tecla `ENTER` para trasladarte a la pantalla de clasificaciones (*Leaderboard*).
* **Registrar tu Puntaje:** Escribe tu nombre utilizando el teclado alfanumérico. 
  * _Nota:_ Si presionas `ENTER` con el campo vacío, el sistema te registrará automáticamente con el nombre por defecto **"Mascota"**.
* **Post-Partida:** Una vez guardado el puntaje, puedes presionar:
  * `J` ➔ Para reiniciar y jugar una nueva partida de forma directa.
  * `M` ➔ Para regresar al Menú Principal.

### 4. Salida de Emergencia
* Presiona la tecla `ESC` (Escape) en cualquier momento de la ejecución para cerrar el juego inmediatamente de forma segura.




