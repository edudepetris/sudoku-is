package sudoku.view;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import sudoku.controller.FirstActivityController;
import sudoku.controller.IController;
import sudoku.controller.ListenerGui;

/**
 * @author Vista principal para la eleccion de la interfaz
 * 
 */
public class FirstActivity extends JFrame implements IView {
	private static final long serialVersionUID = 1L;
	private JRadioButton radioGui1_;
	private JRadioButton radioGui2_;

	public FirstActivity() {
		super("Bienvenido a Sudoku-ELSE");
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(500, 100);
		setLocationRelativeTo(null);
		setVisible(true);

		ButtonGroup btnG = new ButtonGroup();

		JButton btnsignIn = new JButton("Ingresar");
		getContentPane().add(btnsignIn, BorderLayout.SOUTH);

		ListenerGui buttonListener = new ListenerGui();
		IController fController = new FirstActivityController();
		fController.setView(this);

		buttonListener.associate(btnsignIn, fController);

		radioGui1_ = new JRadioButton("Vista 1");
		getContentPane().add(radioGui1_, BorderLayout.WEST);

		radioGui2_ = new JRadioButton("Vista 2");
		getContentPane().add(radioGui2_, BorderLayout.EAST);

		// relacion logica
		btnG.add(radioGui2_);
		btnG.add(radioGui1_);

	}

	public boolean isSelectVista1() {
		return radioGui1_.isSelected();
	}

	public boolean isSelectVista2() {
		return radioGui2_.isSelected();
	}

	@Override
	public void present(String model) {
		JOptionPane.showMessageDialog(null, model);

	}

}
