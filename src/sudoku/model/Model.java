package sudoku.model;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import sudoku.view.IView;
import utils.Constants;
import utils.Pair;

/**
 * Modelo. maneja toda la logica del sudoku. Interactua con Board y con Time
 * 
 */
public class Model extends Observable
{
	
	private boolean	isNameRequest_;
	private boolean	isPressStop_;
	private boolean	isPressResolve_;
	private boolean	isAskedClue_;
	private float		nScore_;
	private int			nDifficulty_;
	private String	userNameTopTen_;
	private Time		time_;

	// tablero de sudoku
	private Board		board_;
	private TopTen	topTen_;

	public Model() {
		topTen_ = TopTen.getIntance();
		loadFileTopTen();
		time_ = Time.getIntance();
	}

	/**
	 * @param view
	 *          {@link IView} carga los observadores
	 */
	public void loadObserver(IView view)
	{	
		this.addObserver((Observer) view);
		time_.addObserver((Observer) view);
	}

	/**
	 * @return {@link Boolean} True ssi se esta corriendo una partida
	 */
	public boolean isRunningGame()
	{
		return time_.isRunnig();
	}

	/**
	 * corta el juego si se lleno el sudoku correctamente, si no teiene que
	 * seguirlo (falta implementar)
	 */
	public void endGame()
	{
		if (!isPressStop_) {
			if (board_.isSudoku() && !isAskedClue_ && !isPressResolve_) {
				board_.setSudokuFill(true);// setea true lleno sudoku en Board
				// ver que el puntaje debe variar acorde a la dificultad del
				// juego
				time_.timeStop();// corta el tiempo
				nScore_ = board_.getMovementAmount() / time_.getFinalTime();
				isNameRequest_ = true;// true a la variable pedir nombre
			} else if (isPressResolve_) {
				board_.resolverBoard();
				board_.setSudokuFill(true);
				isAskedClue_ = true;
				this.setChanged();
				Pair<Integer, Object> par = new Pair<Integer, Object>(1,
						board_.getBoard());
				this.notifyObservers(par);
				time_.timeStop();
			}
		} else {// se presiono stop
			time_.timeStop();
		}

	}

	/**
	 * @param difficulty
	 *          Inicia un nuevo juego.
	 */
	public void newGame(int difficulty)
	{
		isAskedClue_ = false;
		isPressStop_ = false;
		isNameRequest_ = false;
		isPressResolve_ = false;
		board_ = new Board(difficulty);
		board_.setSudokuFill(false);
		this.nDifficulty_ = difficulty;
		this.setChanged();
		Pair<Integer, Object> par = new Pair<Integer, Object>(1, board_.getBoard());
		this.notifyObservers(par);
		time_.timeGo();
	}

	/**
	 * @return true ssi el sudoku ha sido resuelto correctamente
	 */
	public boolean isSolvedSudoku()
	{
		return board_.isSolvedSudoku();
	}

	/**
	 * guarda un usuario en la lista de topTen (en caso de corresponder)
	 */
	public void saveInTopTen()
	{
		topTen_.insert(this.nDifficulty_, userNameTopTen_ + " - " + nScore_);
	}

	/**
	 * almacena en el archivo "TopTen.txt" toda la lista de topTen con los mejores
	 * jugadores. Este metodo debe ser llamado al salir del juego
	 */
	public void saveChanges()
	{
		topTen_.setTopTen(Constants.FACIL);
		topTen_.setTopTen(Constants.MEDIANA);
		topTen_.setTopTen(Constants.DIFICIL);
	}

	/**
	 * carga las listas de topTen con cada uno de los archivos
	 */
	public void loadFileTopTen()
	{
		topTen_.getTopTen(Constants.FACIL);
		topTen_.getTopTen(Constants.MEDIANA);
		topTen_.getTopTen(Constants.DIFICIL);
	}

	/**
	 * @param difficulty
	 * @return lista de top ten de la dificultad pasada como parametro
	 */
	public LinkedList<String> getTopTen(int difficulty)
	{
		if (difficulty == Constants.FACIL)
			return topTen_.getListaTopTenEasy();
		if (difficulty == Constants.MEDIANA)
			return topTen_.getListaTopTenMedium();
		if (difficulty == Constants.DIFICIL)
			return topTen_.getListaTopTenHard();
		return null;
	}

	/**
	 * * @return {@link Boolean} true ssi se nesesita el nombre para juardar en *
	 * lista top-ten EL controlador esta pendiente de esto
	 * 
	 * pedirNombre
	 */
	public boolean nameRequest()
	{
		return isNameRequest_;
	}

	/**
	 * @param namemodelo
	 *          {@link String} modifica el nombre de usuario topTen
	 */
	public void setUserNameTopTen(String name)
	{
		userNameTopTen_ = name;
	}

	/**
	 * @return cantidad de movimientos realizados por el usuario
	 */
	public int getMovementAmount()
	{
		return board_.getMovementAmount();
	}

	/**
	 * @return cantida de espacios libres en el sudoku
	 */
	public int getAmountFreeSpace()
	{
		return board_.getFreeSpacesOnBoard();
	}

	/**
	 * setea true a la variable presiono stop
	 */
	public void setTruePressStop()
	{
		this.isPressStop_ = true;
	}

	/**
	 * @return true ssi se genero una pista, false en caso contrario (matriz
	 *         cargada correctamente)
	 */
	public boolean generateClue()
	{
		boolean generatedClue = false;
		if (time_.isRunnig()) {
			generatedClue = board_.isGenerateClue();
		}
		if (generatedClue) {
			isAskedClue_ = true;
			Pair<Integer, Integer> pos = new Pair<Integer, Integer>(
					board_.getRowClue(), board_.getColClue());
			Pair<Pair<Integer, Integer>, Integer> pista = new Pair<Pair<Integer, Integer>, Integer>(
					pos, board_.getGeneratedClue());
			Pair<Integer, Object> par = new Pair<Integer, Object>(
					Constants.DIBUJARPISTA, pista);
			setChanged();
			notifyObservers(par);
		}
		return generatedClue;
	}

	/**
	 * Restaura el tablero al que se inicio con nuevo juego
	 */
	public void restoreBoard()
	{
		board_.restoreBoard();
		Pair<Integer, int[][]> par = new Pair<Integer, int[][]>(
				Constants.BORRARJUEGO, board_.getBoard());
		setChanged();
		notifyObservers(par);
	}

	/**
	 * @param posI
	 *          {@link Integer}
	 * @param posJ
	 *          {@link Integer}
	 * @param number
	 *          {@link Integer} setea la posicion dada en el Board.
	 */
	public void setBoardPos(int posI, int posJ, int number)
	{
		board_.setBoardPos(posI, posJ, number);
	}

	public void setTrueIsPressResolve()
	{
		isPressResolve_ = true;
	}
}
