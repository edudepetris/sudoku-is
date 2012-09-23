package sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import sudoku.controller.GuiController;
import sudoku.controller.IController;
import utils.Constants;

/**
 * @author ELSE Una de las vista grafica del juego
 * 
 */
public class FirstGui extends sudoku.view.AbstractView {

	private JFrame frame = new JFrame();
	private JPanel contentPane;
	JTextField matrix[][] = new JTextField[9][9];
	private final int ALTO = 1;
	private JLabel lblTiempo;

	private List<String> inm = new LinkedList<String>();

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("static-access")
	public FirstGui() {
		frame.setTitle("Sudoku-ELSE");
		((JFrame) frame).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(480, 640);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		inm.add("00");
		inm.add("40");
		inm.add("36");
		inm.add("93");

		// Enlace controlador con vista
		ListenerGui buttonListener = new ListenerGui();
		IController controller = GuiController.getIntance();
		controller.setView((IView) this);

		ListenerMatrix listenerMatrix = new ListenerMatrix();
		IController controllerMatrix = GuiController.getIntance();
		controllerMatrix.setView((IView) this);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		// menu archivo
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmNuevo = new JMenuItem("Nuevo Juego");
		mntmNuevo.setMnemonic('N');
		mnArchivo.add(mntmNuevo);
		mnArchivo.addSeparator();
		buttonListener.associate(mntmNuevo, controller);

		JMenuItem mntmDetener = new JMenuItem("Detener");
		mnArchivo.add(mntmDetener);
		mntmDetener.setMnemonic('D');
		mnArchivo.addSeparator();
		buttonListener.associate(mntmDetener, controller);

		JMenuItem mntmTopTen = new JMenuItem("Top-Ten");
		mnArchivo.add(mntmTopTen);
		mnArchivo.addSeparator();
		buttonListener.associate(mntmTopTen, controller);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);
		mntmSalir.setMnemonic('S');
		buttonListener.associate(mntmSalir, controller);
		// fin menu archivo

		// menu editar
		JMenu mnEditar = new JMenu("Editar");

		mnArchivo.setMnemonic('E');
		menuBar.add(mnEditar);

		JMenuItem mntmPreferencias = new JMenuItem("Preferencias");
		mntmPreferencias.setMnemonic('P');
		mnEditar.add(mntmPreferencias);
		buttonListener.associate(mntmPreferencias, controller);

		JMenuItem mntmCambiarVista = new JMenuItem("Cambiar vista");
		mntmCambiarVista.setMnemonic('P');
		mnEditar.add(mntmCambiarVista);
		buttonListener.associate(mntmCambiarVista, controller);
		// fin menu editar

		// menu ayuda
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);

		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mntmAcercaDe.setMnemonic('A');
		mnAyuda.add(mntmAcercaDe);
		buttonListener.associate(mntmAcercaDe, controller);
		// fin menu ayuda

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);

		// matriz
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridLayout grilla = new GridLayout(9, 9);
		panel.setLayout(grilla);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = new JTextField();
				listenerMatrix.associate(matrix[i][j], controllerMatrix, i, j);
				Font f = new Font("TimesRoman", Font.BOLD, 36);
				matrix[i][j]
						.setHorizontalAlignment((int) frame.CENTER_ALIGNMENT);
				matrix[i][j].setFont(f);
				matrix[i][j].setCaretColor(Color.WHITE);// color del cursor
				matrix[i][j].setForeground(Color.WHITE);
				matrix[i][j].setEditable(false);
				matrix[i][j].setEnabled(false);
				panel.add(matrix[i][j]);
			}
		}
		pintar_matrix();
		// fin matriz

		// botonera
		JPanel botonera = new JPanel();
		botonera.setLayout(new FlowLayout());

		JButton btnResolver = new JButton("Resolver");
		botonera.add(btnResolver);
		buttonListener.associate(btnResolver, controller);

		JButton btnBorrar = new JButton("Borrar");
		botonera.add(btnBorrar);
		buttonListener.associate(btnBorrar, controller);

		JButton btnPista = new JButton("Pista");
		botonera.add(btnPista);
		buttonListener.associate(btnPista, controller);

		contentPane.add(botonera, BorderLayout.SOUTH);

		lblTiempo = new JLabel();
		botonera.add(lblTiempo);
		// fin botonera

		// dibujarNumeros(matrix);
	}

	private void pintar_matrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].setBorder(BorderFactory
						.createLineBorder(Color.gray));
				matrix[i][j].setBackground(Color.LIGHT_GRAY);
			}
		}
		pintarCudrado(3, 0, this.ALTO);
		pintarCudrado(0, 3, this.ALTO);
		pintarCudrado(6, 3, this.ALTO);
		pintarCudrado(3, 6, this.ALTO);
	}

	private void pintarCudrado(int x, int y, int altura) {
		for (int i = x; i < x + 3; i++) {
			for (int j = y; j < y + 3; j++) {
				if (altura == this.ALTO) {
					matrix[i][j].setBorder(BorderFactory
							.createLineBorder(Color.LIGHT_GRAY));
					matrix[i][j].setBackground(Color.GRAY);
				} else
					matrix[i][j].setBorder(BorderFactory.createEtchedBorder());
			}
		}
	}

	/**
	 * @param m
	 *            Matriz con numeros Dibuja la matriz del sudoku Si se preciono
	 *            detener se le pasa null y resetea la matriz y el tiempo
	 */
	public void dibujarNumeros(int[][] m) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (m == null || m[i][j] == 0) {
					matrix[i][j].setText("");
					matrix[i][j].setEnabled(true);
				} else {
					if (m[i][j] != 0) {
						matrix[i][j].setText(m[i][j] + "");// fill sudoku
						matrix[i][j].setEnabled(false);
						matrix[i][j].setDisabledTextColor(Color.BLACK);
						// para cuando se presiona detener
					}
				}
			}
		}
		//aca nunca entra 
		if (m == null) {
			lblTiempo.setText("");
			
		}
	}

	public void setTextinMatrix(int posI, int posJ, int number, int constant) {
		if (constant == Constants.DIBUJARPISTA) {
			matrix[posI][posJ].setText(number + "");
			matrix[posI][posJ].setEnabled(false);
			matrix[posI][posJ].setDisabledTextColor(Color.BLACK);
		} else if (constant == Constants.TECLA_INGRESADA)
			matrix[posI][posJ].setText(number + "");
	}

	@Override
	public void dispose() {
		frame.dispose();
	}

	@Override
	public void setTime(String time) {
		lblTiempo.setText(time);
	}

}
