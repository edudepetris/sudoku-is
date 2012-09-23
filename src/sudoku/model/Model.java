package sudoku.model;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Observable;

import utils.Constants;
import utils.Pair;

public class Model extends Observable implements Runnable {

	private boolean pedirNombre = false;
	private boolean llenoSudoku = false;
	private boolean presionoDetener = false;
	private boolean cicle;
	private boolean seEstaJugando = false;
	private boolean pidioPista = false;

	private long tiempo_inicial = 0;
	private long tiempoFinal = 0;// tiempo final del juego

	private float puntaje;
	private int dificultad;
	private int cantMovimientos = 0;
	private int cantidadEspaciosLibres = 0;// depende de la matriz que se carge
	private String nombreUsuarioTopten;
	private Calendar tiempo;
	private Thread hilo;

	// clon del tablero de nuevoJuego para poder así restaurar la partidad
	// (Borrar jugadas) en cualquier momento
	private int[][] tableroAuxiliar = new int[9][9];

	private int[][] tablero = { { 0, 0, 0, 2, 6, 0, 7, 0, 1 },
			{ 6, 8, 0, 0, 7, 0, 0, 9, 0 }, { 1, 9, 0, 0, 0, 4, 5, 0, 0 },
			{ 8, 2, 0, 1, 0, 0, 0, 4, 0 }, { 0, 0, 4, 6, 0, 2, 9, 0, 0 },
			{ 0, 5, 0, 0, 0, 3, 0, 2, 8 }, { 0, 0, 9, 3, 0, 0, 0, 7, 4 },
			{ 0, 4, 0, 0, 5, 0, 0, 3, 6 }, { 7, 0, 3, 0, 1, 8, 0, 0, 0 } };

	private int[][] tableroResuelto = { { 4, 3, 5, 2, 6, 9, 7, 8, 1 },
			{ 6, 8, 2, 5, 7, 1, 4, 9, 3 }, { 1, 9, 7, 8, 3, 4, 5, 6, 2 },
			{ 8, 2, 6, 1, 9, 5, 3, 4, 7 }, { 3, 7, 4, 6, 8, 2, 9, 1, 5 },
			{ 9, 5, 1, 7, 4, 3, 6, 2, 8 }, { 5, 1, 9, 3, 2, 6, 8, 7, 4 },
			{ 2, 4, 8, 9, 5, 7, 1, 3, 6 }, { 7, 6, 3, 4, 1, 8, 2, 5, 9 } };

	// depende de la matriz que se cargue
	private TopTen topTen;

	public Model() {
		topTen = TopTen.getIntance();
		cargarArchivosTopTen();
	}

	public int[][] getTablero() {
		return this.tablero;
	}

	public int getTableroPos(int i, int j) {
		return this.tablero[i][j];
	}

	public void setTablero(int[][] tablero) {
		this.tablero = tablero;
	}

	public void setTableroPos(int fil, int col, int number) {
		this.tablero[fil][col] = number;
		this.cantMovimientos++;
	}

	public void timeGo() {
		tiempo_inicial = System.currentTimeMillis();
		hilo = new Thread((Runnable) this);
		hilo.start();
		seEstaJugando = true;
		cantMovimientos = 0;
	}

	public void timeStop() {
		tiempoFinal = System.currentTimeMillis() - tiempo_inicial;
		cicle = false;
		seEstaJugando = false;
	}

	@Override
	public void run() {
		tiempo = Calendar.getInstance();
		cicle = true;
		long tiempo_actual = 0;
		while (cicle) {
			tiempo.setTimeInMillis(System.currentTimeMillis() - tiempo_inicial);
			if ((tiempo.getTimeInMillis() / 1000) != tiempo_actual) {
				setChanged();
				Pair<Integer, Object> par = new Pair<Integer, Object>();
				par.setFirstElem(0);
				par.setSecondElem(tiempo.get(Calendar.MINUTE) + ":"
						+ tiempo.get(Calendar.SECOND));
				notifyObservers(par);
				tiempo_actual = tiempo.getTimeInMillis() / 1000;
			}
		}
	}

	/**
	 * @return {@link Boolean} True ssi se esta corriendo una partida
	 */
	public boolean seEstaJugando() {
		return seEstaJugando;
	}

	/**
	 * corta el juego
	 */
	public void cortarJuegoModelo() {
		if (!presionoDetener) {
			if (esSudoku() && !tieneEspaciosVacios() && !pidioPista) {
				llenoSudoku = true;
				// entonces termino el juego
				// ver que el puntaje debe variar acorde a la dificultad del
				// juego
				puntaje = cantMovimientos / tiempoFinal;
				pedirNombre = true;
			}
		}
	}

	/**
	 * @return true ssi el sudoku ha sido resuelto correctamente
	 */
	public boolean isLlenoSudoku() {
		return llenoSudoku;
	}

	/**
	 * guarda un usuario en la lista de topTen (en caso de corresponder)
	 */
	public void guardarEnTopTen() {
		topTen.insertar(this.dificultad, nombreUsuarioTopten + " - " + puntaje);
	}
	
	/**
	 * almacena en el archivo "TopTen.txt" toda la lista de topTen con los
	 * mejores jugadores. Este metodo debe ser llamado al salir del juego
	 */
	public void salvarCambios() {
		topTen.setTopTen(Constants.FACIL);
		topTen.setTopTen(Constants.MEDIANA);
		topTen.setTopTen(Constants.DIFICIL);
	}

	/**
	 * carga las listas de topTen con cada uno de los archivos
	 */
	public void cargarArchivosTopTen() {
		topTen.getTopTen(Constants.FACIL);
		topTen.getTopTen(Constants.MEDIANA);
		topTen.getTopTen(Constants.DIFICIL);
	}
	
	/**
	 * @param dificultad
	 * @return lista de top ten de la dificultad pasada como parametro 
	 */
	public LinkedList<String> getTopTen(int dificultad) {
		if (dificultad == Constants.FACIL)
			return topTen.getListaTopTenEasy();
		if (dificultad == Constants.MEDIANA)
			return topTen.getListaTopTenMedium();
		if (dificultad == Constants.DIFICIL)
			return topTen.getListaTopTenHard();
		return null;
	}
	
		/**
	 * @param m
	 *            : matriz del juego
	 * @return true ssi la matriz del juego fue completada correctamente
	 */
	private boolean esSudoku() {
		if (repetidosEnCuadriculas())
			return false;
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				if (existeEnFila(tablero, i, tablero[i][j])
						|| existeEnColumna(tablero, j, tablero[i][j]))
					return false;
			}
		}
		return true;
	}

	/**
	 * @return true ssi existe algun elemento en el tablero tal que
	 *         tablero[i][j] == 0
	 */
	private boolean tieneEspaciosVacios() {
		for (int i = 0; i < tablero.length; i++)
			for (int j = 0; j < tablero.length; j++)
				if (tablero[i][j] == 0)
					return true;
		return false;
	}

	/**
	 * @param m
	 * @param i
	 * @param valor
	 * @return true ssi existe otro numero igual a valor dentro de la fila i en
	 *         la matriz m
	 */
	private boolean existeEnFila(int[][] m, int i, int valor) {
		for (int k = 0; k < tablero.length; k++)
			if (k != i && m[i][k] == valor)
				return true;
		return false;
	}

	/**
	 * @param m
	 * @param j
	 * @param valor
	 * @return true ssi existe otro numero igual a valor dentro de la columna j
	 *         en la matriz m
	 */
	private boolean existeEnColumna(int[][] m, int j, int valor) {
		for (int k = 0; k < tablero.length; k++)
			if (k != j && m[k][j] == valor)
				return true;
		return false;
	}

	/**
	 * @param m
	 * @return true ssi existen elementos iguales dentro de la misma cuadricula
	 *         o existe algun 0
	 */
	private boolean repetidosEnCuadriculas() {
		for (int i = 0; i < tablero.length; i++) {
			if (seRepitenEnLista(numerosGrupo(i)))
				return true;
		}
		return false;
	}

	/**
	 * @param list
	 * @return true ssi existen dos elementos iguales en la lista, o si alguno
	 *         de ellos es 0
	 */
	private boolean seRepitenEnLista(LinkedList<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i) == list.get(j))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return true ssi se pulsa la opcion Resolver mientras se esta jugando una
	 *         partida
	 */
	public boolean getTableroResuelto() {
		if (this.seEstaJugando()) {
//----------Me parece que el problema de la pista 0 esta por aca!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			tablero = tableroResuelto;
			llenoSudoku = true;
			pidioPista = true;
			this.seEstaJugando = false;
			this.setTablero(tablero);
			this.setChanged();
			Pair<Integer, Object> par = new Pair<Integer, Object>(1, tablero);
			this.notifyObservers(par);
			this.timeStop();
			return true;
		} else
			return false;
	}

	/**
	 * @param m
	 *            : matriz del tablero
	 * @return retorna una lista con los numero que estan la grupo elejido del
	 *         tablero
	 */
	public LinkedList<Integer> numerosGrupo(int grupo) {
		int i = 0, j = 0, maxJ = 0, maxI = 0;
		switch (grupo) {
		case 1:
			i = 0;
			j = 0;
			maxI = 3;
			maxJ = 3;
			break;
		case 2:
			i = 0;
			j = 3;
			maxJ = 6;
			maxI = 3;
			break;
		case 3:
			i = 0;
			j = 6;
			maxJ = 9;
			maxI = 3;
			break;
		case 4:
			i = 3;
			j = 0;
			maxJ = 3;
			maxI = 6;
			break;
		case 5:
			i = 3;
			j = 3;
			maxJ = 6;
			maxI = 6;
			break;
		case 6:
			i = 3;
			j = 6;
			maxJ = 9;
			maxI = 6;
			break;
		case 7:
			i = 6;
			j = 0;
			maxJ = 3;
			maxI = 9;
			break;
		case 8:
			i = 6;
			j = 3;
			maxJ = 6;
			maxI = 9;
			break;
		case 9:
			i = 6;
			j = 6;
			maxJ = 9;
			maxI = 9;
			break;
		}// end case

		LinkedList<Integer> lista = new LinkedList<Integer>();
		for (int a = i; a < maxI; a++)
			for (int b = j; b < maxJ; b++)
				if (tablero[a][b] != 0)
					lista.add(tablero[a][b]);

		return lista;
	}

	public void empezarNuevoJuego(int dificultad) {
		llenoSudoku = false;
		int[][] tablero;
		this.dificultad = dificultad;
		if (dificultad == Constants.FACIL) {
			// Generador de tablero de acuerdo a la dificultad
			tablero = this.tablero;
			// clono el tablero.
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					tableroAuxiliar[i][j] = tablero[i][j];
				}
			}

		} else {
			
			
//  FALTA INCIALIZAR EL TABLERO DE FORMA ADECUADA DE ACUERDO A LA DIFICULTAD
			if (dificultad == Constants.MEDIANA) {
				tablero = this.tablero;
			} else {
				tablero = this.tablero;
			}
		}
		this.seEstaJugando = true;
		this.cantMovimientos = 0;
		this.setTablero(tablero);
		this.setChanged();
		Pair<Integer, Object> par = new Pair<Integer, Object>(1, tablero);
		this.notifyObservers(par);
		this.timeGo();
	}

	/**
	 * * @return {@link Boolean} true ssi se nesesita el nombre para juardar en
	 * * lista top-ten EL controlador esta pendiente de esto
	 */
	public boolean pedirNombre() {
		return pedirNombre;
	}

	/**
	 * @param namemodelo
	 *            .salvarCambios(); modifica el nombre de usuario topTen
	 */
	public void setNombreUsuarioTopten(String name) {
		nombreUsuarioTopten = name;
	}

	public int cantidadMovimiento() {
		return cantMovimientos;
	}

	public int cantidadEspacioLibres() {
		return cantidadEspaciosLibres;
	}

	public void setFalseSePresionoDetener() {
		this.presionoDetener = false;
	}

	/**
	 * @return true ssi se genero una pista, false en caso contrario (matriz
	 *         cargada correctamente)
	 */
	public boolean generarPista() {
		boolean pistaGenerada = false;
		int col = 0;
		int fil = 0;
		if (this.seEstaJugando) {
			col = 0;
			while ((!pistaGenerada) && (col < tablero.length)) {
				fil = 0;
				while ((!pistaGenerada) && (fil < tablero.length)) {
					if ((tablero[fil][col] == 0)
							|| (tablero[fil][col] != tableroResuelto[fil][col])) {
						tablero[fil][col] = tableroResuelto[fil][col];
						pistaGenerada = true;
					}
					if (!pistaGenerada) {
						fil++;
					}
				}
				if (!pistaGenerada) {
					col++;
				}
			}
		}
		if (pistaGenerada) {
			pidioPista = true;
			Pair<Integer, Integer> pos = new Pair<Integer, Integer>(fil, col);
			Pair<Pair<Integer, Integer>, Integer> pista = new Pair<Pair<Integer, Integer>, Integer>(
					pos, tableroResuelto[fil][col]);
			System.out.println("1:"+tablero[fil][col] + "    2:"+tableroResuelto[fil][col]);
			Pair<Integer, Object> par = new Pair<Integer, Object>(
					Constants.DIBUJARPISTA, pista);
			setChanged();
			notifyObservers(par);
		}
		return pistaGenerada;
	}

	/**
	 * Restaura el tablero al que se inicio con nuevo juego
	 */
	public void restaurar() {
		// clono el tablero.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.tablero[i][j] = this.tableroAuxiliar[i][j];
				cantMovimientos += cantMovimientos;
			}
		}
		Pair<Integer, int[][]> par = new Pair<Integer, int[][]>(
				Constants.BORRARJUEGO, this.tablero);
		setChanged();
		notifyObservers(par);
	}
}
