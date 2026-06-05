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
## Controles 🐍
* Flechas de dirección / WASD: Mover a la mascota.
* Selección de dificultad: Seleccionar la dificultad con el mouse antes de iniciar
* 1, 2 o 3: Para elegir a tu mascota.
* ENTER: Para pasar al siguiente nivel.
* ESC: Salir del juego.




