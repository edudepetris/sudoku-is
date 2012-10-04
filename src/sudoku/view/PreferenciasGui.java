package sudoku.view;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import sudoku.controller.GuiController;
import sudoku.controller.IController;
import sudoku.controller.ListenerGui;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class PreferenciasGui extends JFrame implements IView
{

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;
	private JPanel									contentPane_;
	private JRadioButton						rdbtnHard_;
	private JRadioButton						rdbtnEasy_;
	private JRadioButton						rdbtnMedium_;

	private static PreferenciasGui	instance					= null;

	public static PreferenciasGui getIntance()
	{
		if (instance == null) {
			instance = new PreferenciasGui();
		}
		return instance;
	}

	public static void setNullIntance()
	{
		instance = null;
	}

	/**
	 * Create the frame.
	 */
	private PreferenciasGui() {
		super("Preferencias");
		this.setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 400, 125);
		contentPane_ = new JPanel();
		contentPane_.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane_);
		contentPane_.setLayout(new BorderLayout(0, 0));
		ListenerGui buttonListener = new ListenerGui();
		IController controller = GuiController.getIntance();
		controller.setView(this);

		JPanel panel = new JPanel();
		ButtonGroup btnG = new ButtonGroup();
		contentPane_.add(panel, BorderLayout.SOUTH);

		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnCancelar);
		buttonListener.associate(btnCancelar, controller);

		JButton btnAceptar = new JButton("Aceptar");
		panel.add(btnAceptar);
		buttonListener.associate(btnAceptar, controller);

		JPanel panel_1 = new JPanel();
		contentPane_.add(panel_1, BorderLayout.EAST);

		rdbtnEasy_ = new JRadioButton("Facil");
		panel_1.add(rdbtnEasy_);

		rdbtnMedium_ = new JRadioButton("Mediano");
		panel_1.add(rdbtnMedium_);

		rdbtnHard_ = new JRadioButton("Dificil");
		panel_1.add(rdbtnHard_);

		btnG.add(rdbtnHard_);
		btnG.add(rdbtnEasy_);
		btnG.add(rdbtnMedium_);
		JLabel lblNivelDeDificultad = DefaultComponentFactory.getInstance()
				.createTitle("Nivel de Dificultad:");
		contentPane_.add(lblNivelDeDificultad, BorderLayout.NORTH);
	}

	public boolean isHard()
	{
		return rdbtnHard_.isSelected();
	}

	public boolean isMedium()
	{
		return rdbtnMedium_.isSelected();
	}

	public boolean isEasy()
	{
		return rdbtnEasy_.isSelected();
	}

	@Override
	public void present(String model)
	{
		JOptionPane.showMessageDialog(null, model);
	}

}
