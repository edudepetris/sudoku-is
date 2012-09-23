package sudoku.controller;

import sudoku.view.IView;

public interface IController {
	
	public void setView(IView view);

	public void process(String model);
}
