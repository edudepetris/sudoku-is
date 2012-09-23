package sudoku.controller;

import sudoku.view.FirstActivity;
import sudoku.view.FirstGui;
import sudoku.view.IView;
import sudoku.view.SecondGui;

/**
 * @author karma Controlador de la vista de eleccion de vistas
 * 
 */
public class FirstActivityController implements IController {

	private FirstActivity view;

	@Override
	public void setView(IView view) {
		this.view = (FirstActivity) view;
	}

	@Override
	public void process(String model) {
		// model es el nombre del boton que se presionó
		if (model.compareTo("Ingresar") == 0) {
			if (view.isSelectVista1()) {
				view.dispose();
				new FirstGui();

			} else {
				if (view.isSelectVista2()) {
					view.dispose();
					new SecondGui();
				} else
					view.present("No seleccionó ninguna vista");
			}
		}

	}

}
