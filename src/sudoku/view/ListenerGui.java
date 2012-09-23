package sudoku.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.AbstractButton;

import sudoku.controller.IController;

public class ListenerGui implements ActionListener {

	private Hashtable<AbstractButton, IController> componentControllerMap;

	public ListenerGui() {
		componentControllerMap = new Hashtable<AbstractButton, IController>();
	}

	public void associate(AbstractButton button, IController controller) {
		button.addActionListener(this);
		componentControllerMap.put(button, controller);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IController controller = componentControllerMap.get(e.getSource());// me
		if (null != controller) {
			controller.process(e.getActionCommand());
		}
	}

}
