package sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import sudoku.controller.GuiController;
import sudoku.controller.IController;
import utils.Constants;

/**
 * @author interfaz de usuario con menu al costado del tablero, incluye iconos
 * 
 */
public class SecondGui extends sudoku.view.AbstractView {

	private JFrame frame = new JFrame();

	private final Color CELESTE = new Color(155, 170, 190);
	private final Color CELESTE_BOTON = new Color(0, 0, 255);

	private JPanel contentPane;
	JTextField matrix[][] = new JTextField[9][9];
	private final int ALTO = 1;
	private JLabel lblTiempo;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("static-access")
	public SecondGui() {
		frame.setTitle("Sudoku-ELSE");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		ListenerGui buttonListener = new ListenerGui();
		IController controller = GuiController.getIntance();
		controller.setView((IView) this);

		ListenerMatrix listenerMatrix = new ListenerMatrix();
		IController controllerMatrix = GuiController.getIntance();
		controllerMatrix.setView((IView) this);

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
				matrix[i][j].setHorizontalAlignment((int) frame.CENTER_ALIGNMENT);
				matrix[i][j].setFont(f);
				matrix[i][j].setCaretColor(Color.WHITE);// color del cursor
				matrix[i][j].setForeground(Color.WHITE);
				matrix[i][j].setEnabled(false);
				matrix[i][j].setEditable(false);
				panel.add(matrix[i][j]);
			}
		}
		pintar_matrix();

		// barra izquierda
		JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);

		JButton btnNuevoJuego = new JButton("Nuevo Juego   ", new ImageIcon(
				"src/sudoku/view/imagenes/newGame.png"));
		btnNuevoJuego.setForeground(CELESTE_BOTON);
		btnNuevoJuego.setBorder(null);
		toolBar.add(btnNuevoJuego);
		buttonListener.associate(btnNuevoJuego, controller);

		toolBar.addSeparator();

		JButton btnDetener = new JButton("Detener               ",
				new ImageIcon("src/sudoku/view/imagenes/detener.png"));
		btnDetener.setForeground(CELESTE_BOTON);
		btnDetener.setBorder(null);
		toolBar.add(btnDetener);
		buttonListener.associate(btnDetener, controller);

		toolBar.addSeparator();

		JButton btnResolver = new JButton("Resolver          ", new ImageIcon(
				"src/sudoku/view/imagenes/resolver.png"));
		btnResolver.setBorder(null);
		btnResolver.setForeground(CELESTE_BOTON);
		toolBar.add(btnResolver);
		buttonListener.associate(btnResolver, controller);

		toolBar.addSeparator();

		JButton btnBorrar = new JButton("Borrar              ", new ImageIcon(
				"src/sudoku/view/imagenes/borrar.png"));
		btnBorrar.setBorder(null);
		btnBorrar.setForeground(CELESTE_BOTON);
		toolBar.add(btnBorrar);
		buttonListener.associate(btnBorrar, controller);

		JButton btnPista = new JButton("Pista                ", new ImageIcon(
				"src/sudoku/view/imagenes/pista.png"));
		toolBar.add(btnPista);
		btnPista.setBorder(null);
		btnPista.setForeground(CELESTE_BOTON);
		buttonListener.associate(btnPista, controller);

		toolBar.addSeparator();

		JButton btnAyuda = new JButton("Acerca de         ", new ImageIcon(
				"src/sudoku/view/imagenes/AcercaDe.png"));
		btnAyuda.setBorder(null);
		btnAyuda.setForeground(CELESTE_BOTON);
		toolBar.add(btnAyuda);
		buttonListener.associate(btnAyuda, controller);

		toolBar.addSeparator();
		// fin botonera izquierda

		// inicio botonera derecha
		JToolBar toolBar2 = new JToolBar(JToolBar.VERTICAL);
		contentPane.add(toolBar2, BorderLayout.EAST);

		JButton btnCambVista = new JButton("Cambiar vista    ", new ImageIcon(
				"src/sudoku/view/imagenes/swapGui.png"));
		btnCambVista.setBorder(null);
		btnCambVista.setForeground(CELESTE_BOTON);
		toolBar2.add(btnCambVista);
		buttonListener.associate(btnCambVista, controller);

		toolBar2.addSeparator();

		JButton btnPreferencias = new JButton("Preferencias      ",
				new ImageIcon("src/sudoku/view/imagenes/preferencias.png"));
		btnPreferencias.setBorder(null);
		btnPreferencias.setForeground(CELESTE_BOTON);
		toolBar2.add(btnPreferencias);
		buttonListener.associate(btnPreferencias, controller);

		toolBar2.addSeparator();

		JButton btnTopTen = new JButton("Top-Ten               ",
				new ImageIcon("src/sudoku/view/imagenes/topten.png"));
		toolBar2.add(btnTopTen);
		btnTopTen.setBorder(null);
		btnTopTen.setForeground(CELESTE_BOTON);
		buttonListener.associate(btnTopTen, controller);

		JButton btnSalir = new JButton("Salir                    ",
				new ImageIcon("src/sudoku/view/imagenes/salir.png"));
		btnSalir.setBorder(null);
		btnSalir.setForeground(CELESTE_BOTON);
		toolBar2.add(btnSalir);
		buttonListener.associate(btnSalir, controller);

		lblTiempo = new JLabel();
		toolBar2.add(lblTiempo);
		// fin botonera
	}

	private void pintar_matrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].setBorder(BorderFactory
						.createLineBorder(Color.LIGHT_GRAY));
				matrix[i][j].setBackground(CELESTE);
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
							.createLineBorder(Color.WHITE));
					matrix[i][j].setBackground(Color.LIGHT_GRAY);
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
						// para cuando se preciona detener
					}
				}
			}
		}
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
