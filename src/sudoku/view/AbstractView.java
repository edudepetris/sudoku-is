package sudoku.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import sudoku.controller.GuiController;
import sudoku.controller.IController;
import sudoku.controller.ListenerGui;
import sudoku.controller.ListenerMatrix;
import utils.Constants;
import utils.Pair;

/**
 * @author edu
 * 
 */
public abstract class AbstractView implements sudoku.view.IView, Observer
{

	AbstractView		abstractView_							= this;

	// enlace controlador de vista
	ListenerGui			buttonListener_		= new ListenerGui();
	IController			controller_				= GuiController.getIntance();

	// enlace controlador matriz
	ListenerMatrix	listenerMatrix_		= new ListenerMatrix();
	IController			controllerMatrix_	= GuiController.getIntance();

	/**
	 * Setea la posicion i,j de las matriz con el numero
	 * 
	 * @param posI
	 * @param posJ
	 * @param number
	 */
	public abstract void setNumberInSudoku(int posI, int posJ, int number,
			int constant);

	/**
	 * Dibuja una matriz en la view.
	 * 
	 * @param m
	 *          {@link int[][] } matriz de int
	 */
	public abstract void drawingBoard(int[][] m);

	public String askName(String msj)
	{
		return JOptionPane.showInputDialog(msj);
	}

	/**
	 * @param time
	 *          Setea el tiempo en la view
	 */
	public abstract void setTime(String time);

	@Override
	public void present(String model)
	{
		JOptionPane.showMessageDialog(null, model);
	}

	@Override
	public abstract void dispose();

	/*
	 * (non-Javadoc) Manejador de los eventos correspondientes a cambios en el
	 * modelo.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg)
	{

		Pair<Integer, Object> pair = (Pair<Integer, Object>) arg;
		// Dibuja la matriz inicial
		if (pair.getFirstElem() == Constants.DIBUJARMATRIZ) {
			((sudoku.view.AbstractView) abstractView_).drawingBoard((int[][]) pair
					.getSecondElem());
		} else {
			// Dibuja una pista sobre la matriz
			if (pair.getFirstElem() == Constants.DIBUJARPISTA) {
				Pair<Pair<Integer, Integer>, Integer> pista = (Pair<Pair<Integer, Integer>, Integer>) pair
						.getSecondElem();
				((sudoku.view.AbstractView) abstractView_).setNumberInSudoku(pista
						.getFirstElem().getFirstElem(), pista.getFirstElem()
						.getSecondElem(), pista.getSecondElem(), pair.getFirstElem());
			} else {
				// Borra todos los numeros ingresados por el usuario
				if (pair.getFirstElem() == Constants.BORRARJUEGO) {
					((sudoku.view.AbstractView) abstractView_).drawingBoard((int[][]) pair
							.getSecondElem());
				} else {
					// setea el tiempo
					((sudoku.view.AbstractView) abstractView_).setTime(pair.getSecondElem()
							.toString());
				}
			}
		}

	}

}
