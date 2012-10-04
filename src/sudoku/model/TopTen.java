package sudoku.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import utils.Constants;

public class TopTen
{

	private LinkedList<String>	topTenListEasy_;
	private LinkedList<String>	topTenListMedium_;
	private LinkedList<String>	topTenListHard_;
	private static TopTen				instance	= null;

	public static TopTen getIntance()
	{
		if (instance == null) {
			instance = new TopTen();
		}
		return instance;
	}

	private TopTen() {
		topTenListEasy_ = new LinkedList<String>();
		topTenListMedium_ = new LinkedList<String>();
		topTenListHard_ = new LinkedList<String>();
	}

	/**
	 * @param difficulty
	 *          actualiza en el archivo TopTen correspondiente a la dificultad, la
	 *          lista de los jugadores que se encuentren en la topTenList
	 *          correspondiente
	 */
	public void setTopTen(int difficulty)
	{
		String TOP_TEN_FILE = "";
		LinkedList<String> list = null;
		if (difficulty == Constants.FACIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_EASY;
			list = topTenListEasy_;
		}
		if (difficulty == Constants.MEDIANA) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_MEDIUM;
			list = topTenListMedium_;
		}
		if (difficulty == Constants.DIFICIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_HARD;
			list = topTenListHard_;
		}

		FileWriter archivo = null;
		PrintWriter pw = null;
		try {
			archivo = new FileWriter(TOP_TEN_FILE);
			pw = new PrintWriter(archivo);

			for (int i = 0; i < list.size(); i++)
				pw.append(list.get(i) + "\n");

			pw.close();
			archivo.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("ERROR in method: ''sudoku.model.TopTen.setTopTen()''");
		}

	}

	/**
	 * @param difficulty
	 * @return lee del archivo "TopTen-Facil.txt", "TopTen-Mediana.txt" o
	 *         "TopTen-Dificil.txt" y almacena en la linkedList adecuada a la
	 *         dificultad hasta 10 mejores jugadores, en caso de que los hubiera
	 */
	public void getTopTen(int difficulty)
	{
		LinkedList<String> list = new LinkedList<String>();
		String TOP_TEN_FILE = "";
		if (difficulty == Constants.FACIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_EASY;
			topTenListEasy_ = list;
		}
		if (difficulty == Constants.MEDIANA) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_MEDIUM;
			topTenListMedium_ = list;
		}
		if (difficulty == Constants.DIFICIL) {
			TOP_TEN_FILE = Constants.TOP_TEN_FILE_HARD;
			topTenListHard_ = list;
		}
		try {
			File archivo = new File(TOP_TEN_FILE);
			if (archivo.exists()) {
				FileReader fr = new FileReader(archivo);
				BufferedReader br = new BufferedReader(fr);
				String player;
				while ((player = br.readLine()) != null)
					list.add(player);
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
	 * @param difficulty
	 * @param value
	 *          inserta en la lista de jugadores topTen correspondiente el
	 *          elemento "valor" en su posicion adecuada (ordenado en forma
	 *          creciente)
	 */
	public void insert(int difficulty, String value)
	{
		LinkedList<String> lista = null;
		if (difficulty == Constants.FACIL)
			lista = topTenListEasy_;
		if (difficulty == Constants.MEDIANA)
			lista = topTenListMedium_;
		if (difficulty == Constants.DIFICIL)
			lista = topTenListHard_;

		int indice = getCorrectIndex(lista, value);
		if (indice != -1) {
			if (lista.size() >= Constants.CAPACIDAD_MAX_TOP_TEN)
				lista.remove();
			lista.add(indice, value);
		}
		return;
	}

	/**
	 * @param list
	 * @param value
	 * @return devuelve el indice que el elemento deberia tener dentro de la
	 *         lista. i en caso de que la lista tenga menos de 10 elementos, o i-1
	 *         en caso de q tenga 10 elementos, debido a que se borrará uno y las
	 *         posiciones de todos los elementos se decrementaran en uno
	 */
	private int getCorrectIndex(LinkedList<String> list, String value)
	{
		float score = getScore(value);
		int i;
		for (i = 0; i < list.size() && (score > getScore(list.get(i))); i++)
			;
		if (list.size() < Constants.CAPACIDAD_MAX_TOP_TEN)
			return i;
		return i - 1;
	}

	/**
	 * @param string
	 * @return devuelve el float que se encuentra en el final de la cadena
	 *         pre-condicion: la cadena debe venir de la forma: "jugador 234"
	 */
	private float getScore(String string)
	{
		String numero = "";
		int i = string.length();
		char c;
		while ((--i >= 0) && ((c = string.charAt(i)) != 32))
			// 32 es el valor en ASCII d
			numero = c + numero;
		return Float.parseFloat(numero);
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListEasy
	 */
	public LinkedList<String> getListaTopTenEasy()
	{
		return topTenListEasy_;
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListMedium
	 */
	public LinkedList<String> getListaTopTenMedium()
	{
		return topTenListMedium_;
	}

	/**
	 * @return la lista de usuarios topTen almacenados en topTenListHard
	 */
	public LinkedList<String> getListaTopTenHard()
	{
		return topTenListHard_;
	}

}
