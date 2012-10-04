package sudoku.model;

import java.util.LinkedList;

import utils.Constants;

/**
 * Esta clase tiene que tener todo el manejo de tablero, todos los metodos que
 * se usan en modelo que tienen que ver con tablero van en esta clase
 * 
 */
public class Board
{

	// tablero con que se va a jugar
	private int[][]	board_							= { { 0, 0, 0, 2, 6, 0, 7, 0, 1 },
			{ 6, 8, 0, 0, 7, 0, 0, 9, 0 }, { 1, 9, 0, 0, 0, 4, 5, 0, 0 },
			{ 8, 2, 0, 1, 0, 0, 0, 4, 0 }, { 0, 0, 4, 6, 0, 2, 9, 0, 0 },
			{ 0, 5, 0, 0, 0, 3, 0, 2, 8 }, { 0, 0, 9, 3, 0, 0, 0, 7, 4 },
			{ 0, 4, 0, 0, 5, 0, 0, 3, 6 }, { 7, 0, 3, 0, 1, 8, 0, 0, 0 } };

	private int[][]	boardResolved_			= { { 4, 3, 5, 2, 6, 9, 7, 8, 1 },
			{ 6, 8, 2, 5, 7, 1, 4, 9, 3 }, { 1, 9, 7, 8, 3, 4, 5, 6, 2 },
			{ 8, 2, 6, 1, 9, 5, 3, 4, 7 }, { 3, 7, 4, 6, 8, 2, 9, 1, 5 },
			{ 9, 5, 1, 7, 4, 3, 6, 2, 8 }, { 5, 1, 9, 3, 2, 6, 8, 7, 4 },
			{ 2, 4, 8, 9, 5, 7, 1, 3, 6 }, { 7, 6, 3, 4, 1, 8, 2, 5, 9 } };

	private int[][]	auxiliarBoard_			= new int[9][9];

	// cantidad de movimientos del usuario.
	private int			nMovementAmount_;

	// espacios libres del tablero.
	private int			nFreeSpacesOnBoard_	= 0;

	// pistas que se generan.
	private int			nClue_;
	private int			nRowClue_;
	private int			nColumnClue_;

	// si se lleno el sudoku
	private boolean	isFillsudoku_;

	/**
	 * el constructor tendria que pedir al web service el sudoku y traerlosy usar
	 * esos
	 * 
	 * @param difficulty
	 *          {@link Integer}
	 */
	public Board(int difficulty) {
		switch (difficulty) {
		case Constants.FACIL:

			break;
		case Constants.MEDIANA:

			break;

		default:// DIFICIL
			break;
		}

		// genero un backup del tablero inicial.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				auxiliarBoard_[i][j] = board_[i][j];
		}
		this.setMovementAmount(0);
		// this.setAmountFreeSpace(NUMERO QUE SE CALCULA DEL SUDOKU QUE SE TREA)
	}

	public boolean isSudokuFill()
	{
		return isFillsudoku_;
	}

	public void setSudokuFill(boolean sudokuFill)
	{
		this.isFillsudoku_ = sudokuFill;
	}

	public int getFreeSpacesOnBoard()
	{
		return nFreeSpacesOnBoard_;
	}

	public void setFreeSpacesOnBoard(int nFreeSpaces)
	{
		this.nFreeSpacesOnBoard_ = nFreeSpaces;
	}

	public int getMovementAmount()
	{
		return nMovementAmount_;
	}

	public void setMovementAmount(int movementAmount)
	{
		this.nMovementAmount_ = movementAmount;
	}

	/**
	 * @return true ssi la matriz del juego fue completada correctamente
	 */
	public boolean isSudoku()
	{
		if (isRepeatedInGridView())// repetido en cuadricula
			return false;
		for (int i = 0; i < board_.length; i++) {
			for (int j = 0; j < board_.length; j++) {
				boolean existsInRow_ = isInTheRow(board_, i, board_[i][j]);
				boolean existsInColumn_ = isInTheColumn(board_, j, board_[i][j]);
				if (existsInRow_ || existsInColumn_)
					return false;
			}
		}
		return true;
	}

	/**
	 * @return true ssi existe algun elemento en el tablero tal que tablero[i][j]
	 *         == 0
	 */
	public boolean hasEmptySpaces()
	{
		for (int i = 0; i < board_.length; i++)
			for (int j = 0; j < board_.length; j++)
				if (board_[i][j] == 0)
					return true;
		return false;
	}

	/**
	 * @param matrix
	 * @param row
	 * @param value
	 * @return true ssi existe otro numero igual a valor dentro de la fila i en la
	 *         matriz m
	 */
	private boolean isInTheRow(int[][] matrix, int row, int value)
	{
		for (int k = 0; k < board_.length; k++)
			if (k != row && matrix[row][k] == value)
				return true;
		return false;
	}

	/**
	 * @param matrix
	 * @param column
	 * @param value
	 * @return true ssi existe otro numero igual a valor dentro de la columna j en
	 *         la matriz m
	 */
	private boolean isInTheColumn(int[][] matrix, int column, int value)
	{
		for (int k = 0; k < board_.length; k++)
			if (k != column && matrix[k][column] == value)
				return true;
		return false;
	}

	/**
	 * @return true ssi existen elementos iguales dentro de la misma cuadricula o
	 *         existe algun 0
	 */
	private boolean isRepeatedInGridView()
	{
		for (int i = 0; i < board_.length; i++) {
			if (isRepeatedInTheList(grupNumbers(i)))
				return true;
		}
		return false;
	}

	/**
	 * @param list
	 * @return true ssi existen dos elementos iguales en la lista, o si alguno de
	 *         ellos es 0
	 */
	private boolean isRepeatedInTheList(LinkedList<Integer> list)
	{
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i) == list.get(j))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return retorna una lista con los numero que estan la grupo elejido del
	 *         tablero
	 */
	public LinkedList<Integer> grupNumbers(int grup)
	{
		int i = 0, j = 0, maxJ = 0, maxI = 0;
		switch (grup) {
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

		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int a = i; a < maxI; a++)
			for (int b = j; b < maxJ; b++)
				if (board_[a][b] != 0)
					list.add(board_[a][b]);

		return list;
	}

	/**
	 * @param row
	 * @param column
	 * @return numero en una posicion dada
	 */
	public int getBoardPos(int row, int column)
	{
		return this.board_[row][column];
	}

	/**
	 * @param row
	 * @param column
	 * @param number
	 *          Cambia un numero en el tablero y aumenta la cantidad de
	 *          movimientos. Este metodo es llamado cuando el usuario realiza un
	 *          cambio
	 */
	public void setBoardPos(int row, int column, int number)
	{
		this.board_[row][column] = number;
		this.nMovementAmount_++;
	}

	/**
	 * @return true ssi el sudoku ha sido resuelto correctamente
	 */
	public boolean isSolvedSudoku()
	{
		return isFillsudoku_;
	}

	public int[][] getBoard()
	{
		return board_;
	}

	public void setBoard(int[][] board)
	{
		this.board_ = board;
	}

	/**
	 * @return retorna un sudoku resuelto
	 */
	public int[][] getBoardResolved()
	{
		return boardResolved_;
	}

	/**
	 * Restaura el tablero al que se inicio con nuevo juego
	 */
	public void restoreBoard()
	{
		// clono el tablero.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.board_[i][j] = this.auxiliarBoard_[i][j];
				nMovementAmount_ += nMovementAmount_;
			}
		}
	}

	/**
	 * Resuelve el sudoku que se esta usando por el usuario
	 */
	public void resolverBoard()
	{
		board_ = boardResolved_;
	}

	/**
	 * @param row
	 * @param col
	 * @return el numero alojado en el sudoku resuelto en la posicion dada
	 */
	public int getNumberBoardResolved(int row, int col)
	{
		return boardResolved_[row][col];
	}

	/**
	 * @return true ssi se genero una pista. Este metodo tambien, si la genera la
	 *         coloca en el sudoku.
	 */
	public boolean isGenerateClue()
	{
		boolean generatedClue = false;
		int col = 0;
		int fil = 0;
		while ((!generatedClue) && (col < board_.length)) {
			fil = 0;
			while ((!generatedClue) && (fil < board_.length)) {
				boolean isNotEqual = (board_[fil][col] != boardResolved_[fil][col]);
				if ((board_[fil][col] == 0) || isNotEqual) {
					board_[fil][col] = boardResolved_[fil][col];
					generatedClue = true;
				}
				if (!generatedClue) {
					fil++;
				}
			}
			if (!generatedClue) {
				col++;
			}
		}
		if (generatedClue) {
			// carga los datos de la pista
			nClue_ = board_[fil][col];
			nRowClue_ = fil;
			nColumnClue_ = col;
		}
		return generatedClue;
	}

	public int getGeneratedClue()
	{
		return nClue_;
	}

	public int getRowClue()
	{
		return nRowClue_;
	}

	public int getColClue()
	{
		return nColumnClue_;
	}
}
