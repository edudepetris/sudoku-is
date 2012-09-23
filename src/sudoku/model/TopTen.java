package sudoku.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import utils.Constants;

public class TopTen {

	private LinkedList<String> topTenListEasy;
	private LinkedList<String> topTenListMedium;
	private LinkedList<String> topTenListHard;
	private static TopTen instance = null;

	public static TopTen getIntance() {
		if (instance == null) {
			instance = new TopTen();
		}
		return instance;
	}

	private TopTen() {
		topTenListEasy = new LinkedList<String>();
		topTenListMedium = new LinkedList<String>();
		topTenListHard = new LinkedList<String>();
	}

	/**
	 * @param dificultad
	 *            actualiza en el archivo TopTen correspondiente a la
	 *            dificultad, la lista de los jugadores que se encuentren en la
	 *            topTenList correspondiente
	 */
	public void setTopTen(int dificultad) {
		String TOP_TEN_FILE = "";
		LinkedList<String> lista = null;
		if (dificultad == Constants.FACIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_EASY;
			lista = topTenListEasy;
		}
		if (dificultad == Constants.MEDIANA) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_MEDIUM;
			lista = topTenListMedium;
		}
		if (dificultad == Constants.DIFICIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_HARD;
			lista = topTenListHard;
		}

		FileWriter archivo = null;
		PrintWriter pw = null;
		try {
			archivo = new FileWriter(TOP_TEN_FILE);
			pw = new PrintWriter(archivo);

			for (int i = 0; i < lista.size(); i++)
				pw.append(lista.get(i) + "\n");

			pw.close();
			archivo.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("ERROR in method: ''sudoku.model.TopTen.setTopTen()''");
		}

	}

	/**
	 * @param dificultad
	 * @return lee del archivo "TopTen-Facil.txt", "TopTen-Mediana.txt" o
	 *         "TopTen-Dificil.txt" y almacena en la linkedList adecuada a la
	 *         dificultad hasta 10 mejores jugadores, en caso de que los
	 *         hubiera
	 */
	public void getTopTen(int dificultad) {
		LinkedList<String> lista = new LinkedList<String>();
		String TOP_TEN_FILE = "";
		if (dificultad == Constants.FACIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_EASY;
			topTenListEasy = lista;
		}
		if (dificultad == Constants.MEDIANA) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_MEDIUM;
			topTenListMedium = lista;
		}
		if (dificultad == Constants.DIFICIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_HARD;
			topTenListHard = lista;
		}
		try {
			File archivo = new File(TOP_TEN_FILE);
			if (archivo.exists()) {
				FileReader fr = new FileReader(archivo);
				BufferedReader br = new BufferedReader(fr);
				String player;
				while ((player = br.readLine()) != null)
					lista.add(player);
				fr.close();
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("ERROR in method: ''sudoku.model.TopTen.getTopTen()''");
		}

		return;
	}

	/**
	 * @param dificultad
	 * @param valor
	 *            inserta en la lista de jugadores topTen correspondiente el
	 *            elemento "valor" en su posicion adecuada (ordenado en forma
	 *            creciente)
	 */
	public void insertar(int dificultad, String valor) {
		LinkedList<String> lista = null;
		if (dificultad == Constants.FACIL)
			lista = topTenListEasy;
		if (dificultad == Constants.MEDIANA)
			lista = topTenListMedium;
		if (dificultad == Constants.DIFICIL)
			lista = topTenListHard;

		int indice = getCorrectIndex(lista, valor);
		if (indice != -1) {
			if (lista.size() >= Constants.CAPACIDAD_MAX_TOP_TEN)
				lista.remove();
			lista.add(indice, valor);
		}
		return;
	}

	/**
	 * @param lista
	 * @param valor
	 * @return devuelve el indice que el elemento deberia tener dentro de la
	 *         lista. i en caso de que la lista tenga menos de 10 elementos, o
	 *         i-1 en caso de q tenga 10 elementos, debido a que se borrará uno
	 *         y las posiciones de todos los elementos se decrementaran en uno
	 */
	private int getCorrectIndex(LinkedList<String> lista, String valor) {
		float score = getScore(valor);
		int i;
		for (i = 0; i < lista.size() && (score > getScore(lista.get(i))); i++)
			;
		if (lista.size() < Constants.CAPACIDAD_MAX_TOP_TEN)
			return i;
		return i - 1;
	}

	/**
	 * @param cadena
	 * @return devuelve el float que se encuentra en el final de la cadena
	 *         pre-condicion: la cadena debe venir de la forma: "jugador 234"
	 */
	private float getScore(String cadena) {
		String numero = "";
		int i = cadena.length();
		char c;
		while ((--i >= 0) && ((c = cadena.charAt(i)) != 32))
			// 32 es el valor en ASCII d
			numero = c + numero;
		return Float.parseFloat(numero);
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListEasy
	 */
	public LinkedList<String> getListaTopTenEasy() {
		return topTenListEasy;
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListMedium
	 */
	public LinkedList<String> getListaTopTenMedium() {
		return topTenListMedium;
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListHard
	 */
	public LinkedList<String> getListaTopTenHard() {
		return topTenListHard;
	}

}
