package sudoku.controller;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import sudoku.model.Model;
import sudoku.view.FirstGui;
import sudoku.view.IView;
import sudoku.view.PreferenciasGui;
import sudoku.view.SecondGui;
import utils.Constants;
import utils.Pair;

/**
 * Clase que se encarga de controlar las acciones de cada uno de los botones de
 * las gui.
 * 
 * @author karma-ELSE
 * 
 */
public class GuiController implements IController, Observer {

	private int dificultad = Constants.FACIL;
	private IView previaView;
	private IView view;
	private Model modelo = new Model();
	private boolean ventanasAbiertasDistintasDeSudoku = false;

	private static GuiController instance = null;

	public static GuiController getIntance() {
		if (instance == null) {
			instance = new GuiController();
		}
		return instance;
	}

	private GuiController() {
	}

	@Override
	public void setView(IView view) {
		this.view = view;
	}

	@Override
	public void process(String model) {
		model = model.trim();

		modelo.addObserver((Observer) this);

		if (model.compareTo("Nuevo Juego") == 0) {
			// Empieza un nuevo juego
			nuevoJuego();
		}

		if (model.compareTo("Detener") == 0) {
			// Detiene la partida actual
			detener();

		}
		if (model.compareTo("Preferencias") == 0) {
			// Maneja las preferencias del juego
			preferencias();

		}
		if (model.compareTo("Resolver") == 0) {
			if (modelo.getTableroResuelto())
				view.present("Sudoku Resuelto");
			else
				view.present("Para resolver el tablero deberá de iniciar un juego nuevo.");
			// reolver el sudoku de la partida actual
		}
		if (model.compareTo("Borrar") == 0) {
			// reinicia el juego actual
			borrarTablero();
		}
		if (model.compareTo("Pista") == 0) {
			// Ofrece una pista al jugador
			ofrecerPista();
		}
		if (model.compareTo("Cambiar vista") == 0) {
			// Intercambia interfaces
			swapGui();
		}
		if (model.compareTo("Acerca de") == 0) {
			// Menu Ayuda sobre las reglas del juego
			view.present(Constants.AYUDA);

		}
		if (model.compareTo("Salir") == 0) {
			// sale de la aplicacion
			salir();
		}
		if (model.compareTo("Cancelar") == 0) {
			// Cancelar perteneciente a PREFERENCIAS
			view.dispose();
			ventanasAbiertasDistintasDeSudoku = false;
			PreferenciasGui.setNullIntance();
			view = previaView;// retorno a la vista que estaba
		}
		// acpetar perteneciente a PREFERENCIAS
		if (model.compareTo("Aceptar") == 0) {
			runPreferencias((PreferenciasGui) view);
			ventanasAbiertasDistintasDeSudoku = false;
			PreferenciasGui.setNullIntance();

		}
		if (model.compareTo("Top-Ten") == 0) {
			// muestra el top-ten por la view que este en juego
			mostrarTopTen();
		}
		if (model.charAt(0) == '|') {
			// model : Trae lo ingresado por usuario en el juego como string
			// manejo de input sobre la matriz de la view
			teclasAceptadas(model);
		}

	}

	private void borrarTablero() {
		modelo.restaurar();
	}

	private void detener() {
		modelo.timeStop();
		modelo.setFalseSePresionoDetener();
	}

	// este model trae el numero y las posiciones i,j
	private void teclasAceptadas(String model) {
		int posI = this.getSecond(model);
		int posJ = this.getThird(model);
		int number = this.getFirst(model);
		((sudoku.view.AbstractView) view).setTextinMatrix(posI, posJ, number,
				Constants.TECLA_INGRESADA);
		modelo.setTableroPos(posI, posJ, number);

		// si entra por aca es porque en el mejor de los casos completo el
		// tablero correctamente o lo lleno pero incorrectamente
		if (modelo.cantidadMovimiento() >= modelo.cantidadEspacioLibres()) {
			// ver si termino ok el juego para terminarlo
			if (modelo.isLlenoSudoku()) {
				modelo.timeStop();
				modelo.cortarJuegoModelo();
				if (modelo.pedirNombre()) {
					view.present("Juego finalizado. FELICITACIONES \n cantidad de movimiento : "
							+ modelo.cantidadMovimiento());
					this.obtenerNombreTopTen();
					modelo.guardarEnTopTen();
					((sudoku.view.AbstractView) view).dibujarNumeros(null);
				} else {
					view.present("Juego finalizado. FELICITACIONES \n cantidad de movimiento : "
							+ modelo.cantidadMovimiento());
					((sudoku.view.AbstractView) view).dibujarNumeros(null);
				}
			}
		}
	}

	private void salir() {
		boolean salirOk = false;
		if (this.verificacion()) {
			salirOk = true;
			view.dispose();
			modelo.salvarCambios();
			// ACA LLAMAR EL SALVAR CAMBIOS DE MODELO QUE GUARDA LA LISTA DE
			// TOPTEN EN ARCHIVO
			// ES DISTINTO AL GUARDAR SALVAR(GRABA)
		}
		if (!salirOk) {
			view.present("Cierre todas las ventas del juego, \n Si ya esta ejecutando una partida, primero detengala");
		}
	}

	/**
	 * Abre las preferencias si es posible
	 */
	private void preferencias() {
		if (!modelo.seEstaJugando()) {// si no se esta jugando
			previaView = view;
			PreferenciasGui.getIntance();// singleton de preferencias
			ventanasAbiertasDistintasDeSudoku = true;
		} else
			view.present("Detenga la partida actual para poder acceder a preferencias\n o puede que ya este abierto el cartel preferencias");
	}

	/**
	 * Muestra el top-ten por pantalla
	 */
	private void mostrarTopTen() {
		boolean showTopTen = false;
		if (this.verificacion()) {
			showTopTen = true;
			LinkedList<String> lista = modelo.getTopTen(this.dificultad);
			String s = "Nombre y puntaje: \n";
			for (int i = 0; i < lista.size(); i++) {
				s = s + (i + 1) + "°: " + lista.get(i) + "\n";
			}
			view.present(s);
		}
		if (!showTopTen) {
			view.present("Cierre todas las ventas del juego \n Si ya esta ejecutando una partida, primero detengala");
		}
	}

	/**
	 * Intercambia las interfaces graficas
	 * 
	 */
	private void swapGui() {
		boolean cambiar = false;
		if (!modelo.seEstaJugando()) {// si no se esta juegando
			// si no esta abierta preferencias
			if (!ventanasAbiertasDistintasDeSudoku) {
				cambiar = true;
				previaView = view;
				if (view.getClass().getCanonicalName().toString()
						.compareTo(Constants.VIEW2) == 0) {
					new FirstGui();
				} else {
					if (view.getClass().getCanonicalName().toString()
							.compareTo(Constants.VIEW1) == 0) {
						new SecondGui();
					}
				}
				previaView.dispose();
			}
		}
		if (!cambiar) {
			view.present("No se puede cambiar de interfaz ya que puede estar preferencias abiertas \n o esta juganado una partida");
		}
	}

	/**
	 * Inicia un nuevo juego, verificando que sea posible en cuanto al entorno
	 * que se encuantra
	 */
	private void nuevoJuego() {
		boolean newGame = false;
		if (this.verificacion()) {
			modelo.empezarNuevoJuego(dificultad);
			newGame = true;
		}
		if (!newGame) {
			view.present("Cierre todas las ventas del juego para empezar NUEVO JUEGO \n Si ya esta eecutando una partida, primero detengala");
		}

	}

	/**
	 * @param prefView
	 *            setea las preferencias en el modelo
	 */
	private void runPreferencias(PreferenciasGui prefView) {
		if (prefView.isHard()) {
			dificultad = Constants.DIFICIL;
			prefView.dispose();
			ventanasAbiertasDistintasDeSudoku = false;
			view = previaView;// retorno a la vista que estaba
		} else {
			if (prefView.isMedium()) {
				dificultad = Constants.MEDIANA;
				prefView.dispose();
				ventanasAbiertasDistintasDeSudoku = false;
				view = previaView;// retorno a la vista que estaba
			} else {
				if (prefView.isEasy()) {
					dificultad = Constants.FACIL;
					prefView.dispose();
					ventanasAbiertasDistintasDeSudoku = false;
					view = previaView;// retorno a la vista que estaba
				} else {
					prefView.present("No ha seleccionado nivel de dificultad");
				}
			}
		}
	}

	public Integer getFirst(String cad) {
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int1
		return Integer.parseInt(cad.charAt(1) + "");

	}

	public Integer getSecond(String cad) {
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int2
		return Integer.parseInt(cad.charAt(3) + "");
	}

	public Integer getThird(String cad) {
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int3
		return Integer.parseInt(cad.charAt(5) + "");
	}

	/**
	 * Se llama cuando se nesesita el nombre de usuario para ingresar al top-ten
	 */
	public void obtenerNombreTopTen() {
		String msj = "Ingrese su nombre para ingresar al top-ten";
		String nombre = ((sudoku.view.AbstractView) view).askName(msj);
		modelo.setNombreUsuarioTopten(nombre);
	}

	private void ofrecerPista() {
		if (!modelo.generarPista()) {
			view.present("Para solicitar una pista debe iniciar un juego nuevo.");
		}

	}

	private boolean verificacion() {
		return !modelo.seEstaJugando() && !ventanasAbiertasDistintasDeSudoku;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		Pair<Integer, Object> par = (Pair<Integer, Object>) arg;
		// Dibuja la matriz inicial
		if (par.getFirstElem() == Constants.DIBUJARMATRIZ) {
			((sudoku.view.AbstractView) view).dibujarNumeros((int[][]) par
					.getSecondElem());
		} else {
			// Dibuja una pista sobre la matriz
			if (par.getFirstElem() == Constants.DIBUJARPISTA) {
				Pair<Pair<Integer, Integer>, Integer> pista = (Pair<Pair<Integer, Integer>, Integer>) par
						.getSecondElem();
				((sudoku.view.AbstractView) view).setTextinMatrix(pista
						.getFirstElem().getFirstElem(), pista.getFirstElem()
						.getSecondElem(), pista.getSecondElem(), par
						.getFirstElem());
			} else {
				// Borra todos los numeros ingresados por el usuario
				if (par.getFirstElem() == Constants.BORRARJUEGO) {
					((sudoku.view.AbstractView) view)
							.dibujarNumeros((int[][]) par.getSecondElem());
				} else {
					// setea el tiempo
					((sudoku.view.AbstractView) view).setTime(par
							.getSecondElem().toString());
				}
			}
		}

	}

}
