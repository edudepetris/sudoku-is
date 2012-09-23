package sudoku.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.swing.JTextField;

import sudoku.controller.IController;
import utils.Pair;

public class ListenerMatrix implements KeyListener {

	private Hashtable<JTextField, IController> componentControllerMap;
	private Hashtable<JTextField, Pair<Integer, Integer>> componentControllerMapPos;

	public ListenerMatrix() {
		componentControllerMap = new Hashtable<JTextField, IController>();
		componentControllerMapPos = new Hashtable<JTextField, Pair<Integer, Integer>>();
	}

	public void associate(JTextField celda, IController controller, Integer i,
			Integer j) {
		celda.addKeyListener(this);
		componentControllerMap.put(celda, controller);
		componentControllerMapPos.put(celda, new Pair<Integer, Integer>(i, j));
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char caracter = e.getKeyChar();
		if (((caracter < '1') || (caracter > '9'))
				&& (caracter != KeyEvent.VK_BACK_SPACE)) {
			e.consume();
		} else {
			IController controller = componentControllerMap.get(e.getSource());
			Pair<Integer, Integer> p = componentControllerMapPos.get(e
					.getSource());
			if (null != controller) {
				if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
					e.consume();
					controller.process("|" + e.getKeyChar() + "|"
							+ p.getFirstElem() + "," + p.getSecondElem());
				}

			}

		}
	}
}