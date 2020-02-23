package laberinto;

import java.util.LinkedList;
import java.util.Random;

// --- Clase Laberinto ---
/**
 * Representación del modelo del laberinto con los métodos para generarlo desde una cuadricula. 
 */
public class ModeloLaberinto {
	Celda[][] laberinto;
	int ancho;
	int alto;

	LinkedList<Celda> stack = new LinkedList<>();
	Random rnd = new Random(); 
	boolean finalizado = false;

	/**
	 * Contructor del modelo, necesita el alto y el ancho del laberinto.
	 * 
	 * @param ancho El ancho del laberinto
	 * @param alto El alto del laberinto
	 */
	public ModeloLaberinto(int ancho, int alto) {
		this.ancho = ancho;
		this.alto = alto;
		this.laberinto = new Celda[alto][ancho];
		for(int x = 0; x < ancho; x++) {
			for (int y = 0; y < alto; y++) {
				laberinto[y][x] = new Celda(x, y);
			}
		}

		for(int x = 0; x < ancho; x++) {
			for (int y = 0; y < alto; y++) {
				LinkedList<Celda> vecinos = new LinkedList<>();
				if (x != 0) {
					vecinos.add(laberinto[y][x - 1]);
				}
				if (x != ancho - 1) {
					vecinos.add(laberinto[y][x + 1]);
				}
				if (y != 0) {
					vecinos.add(laberinto[y - 1][x]);
				}
				if (y != alto - 1) {
					vecinos.add(laberinto[y + 1][x]);
				}
				laberinto[y][x].setVecinos(vecinos);
			}
		}
		int rndX = rnd.nextInt(ancho);
		int rndY = rnd.nextInt(alto);
		stack.push(laberinto[rndY][rndX]);
	}

	/**
	 * Realiza un solo paso en la generación del laberinto, y regresa la Celda en el tope del stack
	 * @return La Celda al tope de stack al iniciar la función 
	 */
	Celda generaSiguiente() {
		if (finalizado)
			return null;

		Celda actual = stack.peek();
		Celda siguiente = actual.getVecinoNoExploradoRandom();
		if (siguiente != null) {
			actual.estado = Celda.EXPLORADO;
			eliminaFrontera(actual, siguiente);
			actual.eliminarDeVecinosNoExplorados();
			actual.vecinosNoExplorados.remove(siguiente);
			stack.push(siguiente);
		} else {
			actual.estado = Celda.TERMINADO;
			actual.eliminarDeVecinosNoExplorados();
			stack.pop();
			finalizado = stack.isEmpty();
			if(finalizado)
				System.out.println("TERMINADO!");
		}
		return actual;
	}

	/**
	 * Elimina las paredes entre celda1 y celda2
	 */
	void eliminaFrontera(Celda celda1, Celda celda2) {
		char direccion = celda1.esVecino(celda2);
		switch(direccion) {
			case Celda.VECINO_ARRIBA:
				celda1.paredA = false;
				celda2.paredB = false;
				break;
			case Celda.VECINO_ABAJO:
				celda1.paredB = false;
				celda2.paredA = false;
				break;
			case Celda.VECINO_IZQUIERDO:
				celda1.paredI = false;
				celda2.paredD = false;
				break;
			case Celda.VECINO_DERECHO:
				celda1.paredD = false;
				celda2.paredI = false;
				break;
		}
	}

	/**
     * Devuelve una representación con caracteres de este laberinto. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
	@Override
	public String toString() {
		String s = "Ancho:" + ancho + " | Alto: " + alto + "\n";
		for(int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				s += laberinto[y][x].estado + " ";
				if (x == ancho - 1) 
					s += "\n";
			}
		}
		return s;
	}

	// --- Clase Celda ---
    /**
     * Representación de cada celda del laberinto.
     */
    class Celda {
    	public static final char VECINO_ARRIBA = 'A';
    	public static final char VECINO_ABAJO = 'B';
    	public static final char VECINO_IZQUIERDO = 'I';
    	public static final char VECINO_DERECHO = 'D';

    	public static final int NO_EXPLORADO = 0;
    	public static final int EXPLORADO = 1;
    	public static final int TERMINADO = 2;

        int celdaX, celdaY;
        boolean paredA; // True para decir que tiene pared Arriba
        boolean paredB; // True para decir que tiene pared aBajo
        boolean paredI; // True para decir que tiene pared Izquierda
        boolean paredD; // True para decir que tiene pared Derecha
        int estado;
        
        LinkedList<Celda> vecinosNoExplorados = new LinkedList<>();
        LinkedList<Celda> vecinos;

        Random rnd = new Random();

        /**
         * Constructor de una celda
         *
         * @param celdaX Coordenada en x
         * @param celdaY Coordenada en y
         * caso.
         */
        Celda(int celdaX, int celdaY) {
            this.celdaX = celdaX;
            this.celdaY = celdaY;
            this.paredA = true;
            this.paredB = true;
            this.paredI = true;
            this.paredD = true;
            this.estado = NO_EXPLORADO;
        }

		/**
		 * Asigna los vecinos que tiene la celda actual
		 * @param vecinos Lista con los vecinos para la Celda (this)
		 */
        void setVecinos(LinkedList<Celda> vecinos) {
        	this.vecinos = vecinos;
        	for (Celda vecino : vecinos) {
        		this.vecinosNoExplorados.add(vecino);
        	}
        }

        /**
         * Nos dice a qué lado se encuentra la celda de la celda original (this) regresando
         * si es de ARRIBA, ABAJO, IZQUIERDA o DERECHA. Si no es vecino regresa ''
         * @param celda La celda de la que queremos saber su posición relativa
         *
         * @return VECINO_ARRIBA, VECINO_ABAJO, VECINO_IZQUIERDO, VECINO_DERECHO o '', dependiendo el caso 
         */
       	char esVecino(Celda celda) {
       		if(celda.celdaY == this.celdaY) {
       			if (celda.celdaX == this.celdaX - 1)
       				return VECINO_IZQUIERDO;
       			if (celda.celdaX == this.celdaX + 1)
       				return VECINO_DERECHO;
       		} else if(celda.celdaX == this.celdaX) {
       			if (celda.celdaY == this.celdaY - 1)
       				return VECINO_ARRIBA;
       			if (celda.celdaY == this.celdaY + 1)
       				return VECINO_ABAJO;
       		}
       		return 'Z';
       	}

       	/**
       	 * Regresa un vecino al azar, si ya no tiene vecinos en su lista regresa null
       	 *
       	 * @return Celda con un vecino al azar o null
       	 */
       	Celda getVecinoNoExploradoRandom() {
       		if(vecinosNoExplorados.isEmpty())
       			return null;
       		int tamanio = vecinosNoExplorados.size();
       		int indiceVecino = rnd.nextInt(tamanio);
       		return vecinosNoExplorados.get(indiceVecino);
       	}

       	/**
       	 * Determina si dos Celda son iguales conforme a las coordenadas (x,y)
		 * @param o el objeto a compara con this.
       	 * @return true si son iguales
       	 */
       	@Override
       	public boolean equals(Object o) {
       		Celda celda = (Celda) o;
       		return (this.celdaX == celda.celdaX && this.celdaY == celda.celdaY);
       	}

   		/**
   	     * Devuelve una representación con caracteres de esta celda.
   	     */
   		@Override
   		public String toString() {
   			return "(" + celdaX + "," + celdaY + ")";
   		}

   		/**
   		 * Se elimina la celda (this) de la lista de vecinos no explorados de los vecinos reales de la celda (this) 
   		 */
   		void eliminarDeVecinosNoExplorados() {
   			for (Celda vecino : vecinos) {
   				vecino.vecinosNoExplorados.remove(this);
   			}
   		}

    }
}