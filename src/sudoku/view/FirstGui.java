package sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

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

import utils.Constants;

/**
 * @author ELSE Una de las vista grafica del juego
 * 
 */
public class FirstGui extends sudoku.view.AbstractView
{

	private JFrame		frame_			= new JFrame();
	private JPanel		contentPane_;
	JTextField				matrix[][]	= new JTextField[9][9];
	private final int	ALTO				= 1;
	private JLabel		lblTime_;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("static-access")
	public FirstGui() {
		frame_.setTitle("Sudoku-ELSE");
		((JFrame) frame_).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame_.setBounds(100, 100, 450, 300);
		frame_.setSize(480, 640);
		frame_.setLocationRelativeTo(null);
		frame_.setVisible(true);

		controller_.setView((IView) this);

		controllerMatrix_.setView((IView) this);

		JMenuBar menuBar = new JMenuBar();
		frame_.setJMenuBar(menuBar);

		// menu archivo
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmNuevo = new JMenuItem("Nuevo Juego");
		mntmNuevo.setMnemonic('N');
		mnArchivo.add(mntmNuevo);
		mnArchivo.addSeparator();
		buttonListener_.associate(mntmNuevo, controller_);

		JMenuItem mntmDetener = new JMenuItem("Detener");
		mnArchivo.add(mntmDetener);
		mntmDetener.setMnemonic('D');
		mnArchivo.addSeparator();
		buttonListener_.associate(mntmDetener, controller_);

		JMenuItem mntmTopTen = new JMenuItem("Top-Ten");
		mnArchivo.add(mntmTopTen);
		mnArchivo.addSeparator();
		buttonListener_.associate(mntmTopTen, controller_);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);
		mntmSalir.setMnemonic('S');
		buttonListener_.associate(mntmSalir, controller_);
		// fin menu archivo

		// menu editar
		JMenu mnEditar = new JMenu("Editar");

		mnArchivo.setMnemonic('E');
		menuBar.add(mnEditar);

		JMenuItem mntmPreferencias = new JMenuItem("Preferencias");
		mntmPreferencias.setMnemonic('P');
		mnEditar.add(mntmPreferencias);
		buttonListener_.associate(mntmPreferencias, controller_);

		JMenuItem mntmCambiarVista = new JMenuItem("Cambiar vista");
		mntmCambiarVista.setMnemonic('P');
		mnEditar.add(mntmCambiarVista);
		buttonListener_.associate(mntmCambiarVista, controller_);
		// fin menu editar

		// menu ayuda
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);

		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mntmAcercaDe.setMnemonic('A');
		mnAyuda.add(mntmAcercaDe);
		buttonListener_.associate(mntmAcercaDe, controller_);
		// fin menu ayuda

		contentPane_ = new JPanel();
		contentPane_.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane_.setLayout(new BorderLayout(0, 0));
		frame_.setContentPane(contentPane_);

		// matriz
		JPanel panel = new JPanel();
		contentPane_.add(panel, BorderLayout.CENTER);
		GridLayout grilla = new GridLayout(9, 9);
		panel.setLayout(grilla);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = new JTextField();
				listenerMatrix_.associate(matrix[i][j], controllerMatrix_, i, j);
				Font f = new Font("TimesRoman", Font.BOLD, 36);
				matrix[i][j].setHorizontalAlignment((int) frame_.CENTER_ALIGNMENT);
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
		buttonListener_.associate(btnResolver, controller_);

		JButton btnBorrar = new JButton("Borrar");
		botonera.add(btnBorrar);
		buttonListener_.associate(btnBorrar, controller_);

		JButton btnPista = new JButton("Pista");
		botonera.add(btnPista);
		buttonListener_.associate(btnPista, controller_);

		contentPane_.add(botonera, BorderLayout.SOUTH);

		lblTime_ = new JLabel();
		botonera.add(lblTime_);
		// fin botonera

		// dibujarNumeros(matrix);
	}

	private void pintar_matrix()
	{
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
				matrix[i][j].setBackground(Color.LIGHT_GRAY);
			}
		}
		pintarCudrado(3, 0, this.ALTO);
		pintarCudrado(0, 3, this.ALTO);
		pintarCudrado(6, 3, this.ALTO);
		pintarCudrado(3, 6, this.ALTO);
	}

	private void pintarCudrado(int x, int y, int altura)
	{
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
	 *          Matriz con numeros Dibuja la matriz del sudoku Si se preciono
	 *          detener se le pasa null y resetea la matriz y el tiempo
	 */
	public void drawingBoard(int[][] m)
	{
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
		// aca nunca entra
		if (m == null) {
			lblTime_.setText("");

		}
	}

	public void setNumberInSudoku(int posI, int posJ, int number, int constant)
	{
		if (constant == Constants.DIBUJARPISTA) {
			matrix[posI][posJ].setText(number + "");
			matrix[posI][posJ].setEnabled(false);
			matrix[posI][posJ].setDisabledTextColor(Color.BLACK);
		} else if (constant == Constants.TECLA_INGRESADA)
			matrix[posI][posJ].setText(number + "");
	}

	@Override
	public void dispose()
	{
		frame_.dispose();
	}

	@Override
	public void setTime(String time)
	{
		lblTime_.setText(time);
	}

}
