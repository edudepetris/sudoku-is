package sudoku.controller;

import java.util.LinkedList;

import sudoku.model.Model;
import sudoku.view.FirstGui;
import sudoku.view.IView;
import sudoku.view.PreferenciasGui;
import sudoku.view.SecondGui;
import utils.Constants;

/**
 * Clase que se encarga de controlar las acciones de cada uno de los botones de
 * las gui.
 * 
 * @author karma-ELSE
 * 
 */
public class GuiController implements IController
{

	private int										difficulty_												= Constants.FACIL;
	private IView									viewPrevious_;
	private IView									view_;
	private Model									model_;
	private boolean								hasOpenViewsDiferentThansSudoku_	= false;
	private boolean								canLoadObserver_									= true;
	private static GuiController	instance													= null;

	public static GuiController getIntance()
	{
		if (instance == null) {
			instance = new GuiController();
		}
		return instance;
	}

	private GuiController() {
		model_ = new Model();
	}

	@Override
	public void setView(IView view)
	{
		this.view_ = view;
	}

	@Override
	public void process(String model)
	{
		model = model.trim();

		if (canLoadObserver_) {
			model_.loadObserver(view_);
			canLoadObserver_ = false;
		}

		if (model.compareTo("Nuevo Juego") == 0) {
			// Empieza un nuevo juego
			newGame();
		}

		if (model.compareTo("Detener") == 0) {
			// Detiene la partida actual
			stopGame();

		}
		if (model.compareTo("Preferencias") == 0) {
			// Maneja las preferencias del juego
			preferences();

		}

		if (model.compareTo("Resolver") == 0) {
			resolve();
		}

		if (model.compareTo("Borrar") == 0) {
			// reinicia el juego actual
			deletePlays();
		}

		if (model.compareTo("Pista") == 0) {
			// Ofrece una pista al jugador
			provideClue();
		}

		if (model.compareTo("Cambiar vista") == 0) {
			// Intercambia interfaces
			swapGui();
		}

		if (model.compareTo("Acerca de") == 0) {
			// Menu Ayuda sobre las reglas del juego
			view_.present(Constants.AYUDA);

		}

		if (model.compareTo("Salir") == 0) {
			// sale de la aplicacion
			exit();
		}

		if (model.compareTo("Cancelar") == 0) {
			// Cancelar perteneciente a PREFERENCIAS
			view_.dispose();
			hasOpenViewsDiferentThansSudoku_ = false;
			PreferenciasGui.setNullIntance();
			view_ = viewPrevious_;// retorno a la vista que estaba
		}
		// acpetar perteneciente a PREFERENCIAS
		if (model.compareTo("Aceptar") == 0) {
			runPreferencias((PreferenciasGui) view_);
			hasOpenViewsDiferentThansSudoku_ = false;
			PreferenciasGui.setNullIntance();

		}

		if (model.compareTo("Top-Ten") == 0) {
			// muestra el top-ten por la view que este en juego
			showTopTen();
		}
		if (model.charAt(0) == '|') {
			// model : Trae lo ingresado por usuario en el juego como string
			// manejo de input sobre la matriz de la view
			acceptedKeys(model);
		}

	}

	private void resolve()
	{
		if (model_.isRunningGame()) {
			model_.setTrueIsPressResolve();
			model_.endGame();
			view_.present("Solucion");
		} else
			view_.present("Opcion no valida, inicie un juego");

	}

	private void deletePlays()
	{
		model_.restoreBoard();
	}

	private void stopGame()
	{
		model_.setTruePressStop();// avisa que se presiono stop
		model_.endGame();
		// se podria borrar el tablero ACA

	}

	// este model trae el numero y las posiciones i,j
	private void acceptedKeys(String model)
	{
		int posI = this.getSecond(model);
		int posJ = this.getThird(model);
		int number = this.getFirst(model);
		((sudoku.view.AbstractView) view_).setNumberInSudoku(posI, posJ, number,
				Constants.TECLA_INGRESADA);
		model_.setBoardPos(posI, posJ, number);

		// si entra por aca es porque en el mejor de los casos completo el
		// tablero correctamente o lo lleno pero incorrectamente
		if (model_.getMovementAmount() >= model_.getAmountFreeSpace()) {
			// ver si termino ok el juego para terminarlo
			if (model_.isSolvedSudoku()) {
				model_.endGame();
				if (model_.nameRequest()) {// si el usuario tiene que entrar el top-ten
					view_
							.present("Juego finalizado. FELICITACIONES \n cantidad de movimiento : "
									+ model_.getMovementAmount());
					this.obtenerNombreTopTen();
					model_.saveInTopTen();
					((sudoku.view.AbstractView) view_).drawingBoard(null);// vacia el sdku
				} else {// si el usuario no ingresa al top-ten
					view_
							.present("Juego finalizado. FELICITACIONES \n cantidad de movimiento : "
									+ model_.getMovementAmount());
					((sudoku.view.AbstractView) view_).drawingBoard(null);
				}
			}
		}
	}

	private void exit()
	{
		boolean salirOk = false;
		if (this.verificacion()) {
			salirOk = true;
			view_.dispose();
			model_.saveChanges();
			// ACA LLAMAR EL SALVAR CAMBIOS DE MODELO QUE GUARDA LA LISTA DE
			// TOPTEN EN ARCHIVO
			// ES DISTINTO AL GUARDAR SALVAR(GRABA)
		}
		if (!salirOk) {
			view_
					.present("Cierre todas las ventas del juego, \n Si ya esta ejecutando una partida, primero detengala");
		}
	}

	/**
	 * Abre las preferencias si es posible
	 */
	private void preferences()
	{
		if (!model_.isRunningGame()) {// si no se esta jugando
			viewPrevious_ = view_;
			PreferenciasGui.getIntance();// singleton de preferencias
			hasOpenViewsDiferentThansSudoku_ = true;
		} else
			view_
					.present("Detenga la partida actual para poder acceder a preferencias\n o puede que ya este abierto el cartel preferencias");
	}

	/**
	 * Muestra el top-ten por pantalla
	 */
	private void showTopTen()
	{
		boolean showTopTen = false;
		if (this.verificacion()) {
			showTopTen = true;
			LinkedList<String> lista = model_.getTopTen(this.difficulty_);
			String s = "Nombre y puntaje: \n";
			for (int i = 0; i < lista.size(); i++) {
				s = s + (i + 1) + "°: " + lista.get(i) + "\n";
			}
			view_.present(s);
		}
		if (!showTopTen) {
			view_
					.present("Cierre todas las ventas del juego \n Si ya esta ejecutando una partida, primero detengala");
		}
	}

	/**
	 * Intercambia las interfaces graficas
	 * 
	 */
	private void swapGui()
	{
		boolean cambiar = false;
		if (!model_.isRunningGame()) {// si no se esta juegando
			// si no esta abierta preferencias
			if (!hasOpenViewsDiferentThansSudoku_) {
				cambiar = true;
				viewPrevious_ = view_;
				if (view_.getClass().getCanonicalName().toString()
						.compareTo(Constants.VIEW2) == 0) {
					new FirstGui();
				} else {
					if (view_.getClass().getCanonicalName().toString()
							.compareTo(Constants.VIEW1) == 0) {
						new SecondGui();
					}
				}
				viewPrevious_.dispose();
			}
		}
		if (!cambiar) {
			view_
					.present("No se puede cambiar de interfaz ya que puede estar preferencias abiertas \n o esta juganado una partida");
		}
	}

	/**
	 * Inicia un nuevo juego, verificando que sea posible en cuanto al entorno que
	 * se encuantra
	 */
	private void newGame()
	{
		boolean newGame = false;
		if (this.verificacion()) {
			model_.newGame(difficulty_);
			newGame = true;
		}
		if (!newGame) {
			view_
					.present("Cierre todas las ventas del juego para empezar NUEVO JUEGO \n Si ya esta eecutando una partida, primero detengala");
		}

	}

	/**
	 * @param prefView
	 *          setea las preferencias en el modelo
	 */
	private void runPreferencias(PreferenciasGui prefView)
	{
		if (prefView.isHard()) {
			difficulty_ = Constants.DIFICIL;
			prefView.dispose();
			hasOpenViewsDiferentThansSudoku_ = false;
			view_ = viewPrevious_;// retorno a la vista que estaba
		} else {
			if (prefView.isMedium()) {
				difficulty_ = Constants.MEDIANA;
				prefView.dispose();
				hasOpenViewsDiferentThansSudoku_ = false;
				view_ = viewPrevious_;// retorno a la vista que estaba
			} else {
				if (prefView.isEasy()) {
					difficulty_ = Constants.FACIL;
					prefView.dispose();
					hasOpenViewsDiferentThansSudoku_ = false;
					view_ = viewPrevious_;// retorno a la vista que estaba
				} else {
					prefView.present("No ha seleccionado nivel de dificultad");
				}
			}
		}
	}

	private Integer getFirst(String cad)
	{
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int1
		return Integer.parseInt(cad.charAt(1) + "");

	}

	private Integer getSecond(String cad)
	{
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int2
		return Integer.parseInt(cad.charAt(3) + "");
	}

	private Integer getThird(String cad)
	{
		// Dada una cadena de la forma "|Int1|Int2,Int3" obtengo Int3
		return Integer.parseInt(cad.charAt(5) + "");
	}

	/**
	 * Se llama cuando se nesesita el nombre de usuario para ingresar al top-ten
	 */
	private void obtenerNombreTopTen()
	{
		String msj = "Ingrese su nombre para ingresar al top-ten";
		String nombre = ((sudoku.view.AbstractView) view_).askName(msj);
		model_.setUserNameTopTen(nombre);
	}

	private void provideClue()
	{
		if (!model_.generateClue()) {
			view_.present("Para solicitar una pista debe iniciar un juego nuevo.");
		}

	}

	private boolean verificacion()
	{
		return !model_.isRunningGame() && !hasOpenViewsDiferentThansSudoku_;

	}

}
