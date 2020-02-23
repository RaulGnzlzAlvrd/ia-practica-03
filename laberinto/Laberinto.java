package laberinto;

import processing.core.PApplet;
import java.awt.Color;
import laberinto.ModeloLaberinto.Celda;

public class Laberinto extends PApplet {
	int ancho = 25; // Cantidad de celdas a lo ancho del laberinto
	int alto = 25; // Cantidad de celdas a lo alto del laberinto

	int tamanioCelda = 20; // Tamaño en pixeles de cada celda del laberinto

	boolean inicializado = false;
	ModeloLaberinto modeloLaberinto;

	@Override
	public void settings() {
		size(ancho * tamanioCelda, alto * tamanioCelda);
	}

	@Override
	public void setup() {
		modeloLaberinto = new ModeloLaberinto(ancho, alto);
		frameRate(60);
	}
	
	@Override
	public void draw() {
		// Dibujamos por primera vez el laberinto
	 	if(!inicializado) {
	  		dibujaLaberinto();
	  		inicializado = true;
		}
		// Dibuja el laberinto en cada paso de generacion
		if (!modeloLaberinto.finalizado){
			Celda actual = modeloLaberinto.generaSiguiente();
			dibujaCelda(actual.celdaX, actual.celdaY);
		}
	}

	/**
	 * Se encarga de pintar en pantalla el laberinto en el estado actual
	 */
	public void dibujaLaberinto() {
		for (int x = 0; x < modeloLaberinto.ancho; x++) {
			for (int y = 0; y < modeloLaberinto.alto; y++) {
				dibujaCelda(x, y);
			}
		}
	}

	/**
	 * Dibuja en pantalla la celda en la posición (x,y) del laberinto
	 * @param x La coordenada X de la celda a dibujar
	 * @param y La coordenada Y de la celda a dibujar
	 */
	public void dibujaCelda(int x, int y) {
		Celda celda = modeloLaberinto.laberinto[y][x];
		noStroke();
		switch(celda.estado) {
			case 0:
				fill(Color.blue.getRGB());
				break;
			case 1:
				fill(Color.red.getRGB());
				break;
			case 2:
				fill(Color.white.getRGB());
				break;
		}
		rect(x * tamanioCelda, y * tamanioCelda, tamanioCelda, tamanioCelda);
		stroke(Color.black.getRGB());
		if(celda.paredA) {
			line(x * tamanioCelda, y * tamanioCelda, (x + 1) * tamanioCelda, y * tamanioCelda);
		}
		if (celda.paredB) {
			line(x * tamanioCelda, (y + 1) * tamanioCelda, (x + 1) * tamanioCelda, (y + 1) * tamanioCelda);
		}
		if (celda.paredI) {
			line(x * tamanioCelda, y * tamanioCelda, x * tamanioCelda, (y + 1) * tamanioCelda);
		}
		if (celda.paredD) {
			line((x + 1) * tamanioCelda, y * tamanioCelda, (x + 1) * tamanioCelda, (y + 1) * tamanioCelda);
		}
	}

	static public void main(String args[]) {
        PApplet.main(new String[]{"laberinto.Laberinto"});
    }
}