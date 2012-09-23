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

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class PreferenciasGui extends JFrame implements IView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JRadioButton rdbtnDificil;
	private JRadioButton rdbtnFacil;
	private JRadioButton rdbtnMediano;
	
	private static PreferenciasGui instance = null;
	
	
	
	public static PreferenciasGui getIntance(){
		if (instance == null) {
			instance = new PreferenciasGui();
		}
		return instance;
	}
	
	public  static void setNullIntance (){
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		ListenerGui buttonListener = new ListenerGui();
		IController controller = GuiController.getIntance();
		controller.setView(this);
		
		JPanel panel = new JPanel();
		ButtonGroup btnG = new ButtonGroup();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnCancelar = new JButton("Cancelar");
		panel.add(btnCancelar);
		buttonListener.associate(btnCancelar, controller);
		
		JButton btnAceptar = new JButton("Aceptar");
		panel.add(btnAceptar);
		buttonListener.associate(btnAceptar, controller);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
		
		rdbtnFacil = new JRadioButton("Facil");
		panel_1.add(rdbtnFacil);
		
		rdbtnMediano = new JRadioButton("Mediano");
		panel_1.add(rdbtnMediano);
		
		rdbtnDificil = new JRadioButton("Dificil");
		panel_1.add(rdbtnDificil);
		
		btnG.add(rdbtnDificil);
		btnG.add(rdbtnFacil);
		btnG.add(rdbtnMediano);
		JLabel lblNivelDeDificultad = DefaultComponentFactory.getInstance().createTitle("Nivel de Dificultad:");
		contentPane.add(lblNivelDeDificultad, BorderLayout.NORTH);
	}
	
	public boolean isHard (){
		return rdbtnDificil.isSelected();
	}
	
	public boolean isMedium (){
		return rdbtnMediano.isSelected();
	}
	
	public boolean isEasy (){
		return rdbtnFacil.isSelected();
	}

	@Override
	public void present(String model) {
		JOptionPane.showMessageDialog(null, model);		
	}


}
