package sudoku.view;

import java.util.LinkedList;

import javax.swing.JOptionPane;

public abstract class AbstractView implements sudoku.view.IView {


	/**
	 * Setea la posicion i,j de las matriz con el numero
	 * 
	 * @param posI
	 * @param posJ
	 * @param number
	 */
	public abstract void setTextinMatrix(int posI, int posJ, int number,
			int constant);

	/**
	 * Dibuja una matriz en la view.
	 * 
	 * @param m
	 *            {@link int[][] } matriz de int
	 * @param inmovil
	 *            {@link LinkedList String} Lista de string que tiene los pares
	 *            ij que son elementos inmovliles ya que son perteneciente al
	 *            nivle de dificultad del juego
	 */
	public abstract void dibujarNumeros(int[][] m);

	public String askName(String msj) {
		return JOptionPane.showInputDialog(msj);
	}

	public abstract  void setTime(String time);
	
	@Override
	public void present(String model) {
		JOptionPane.showMessageDialog(null, model);
	}

	@Override
	public abstract void dispose();

}
