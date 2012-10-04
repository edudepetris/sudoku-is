package sudoku.model;

import java.util.Calendar;
import java.util.Observable;

import utils.Pair;

/**
 * Clase dedicada al manejo del tiempo en el juego.
 * 
 */
public class Time extends Observable implements Runnable
{
	private long				nInitialTime_;
	private long				nFinalTime_;
	private Calendar		calendar_;
	private Thread			thread_;
	private boolean			isRunnig_;
	private boolean			isCicle_;
	private static Time	intance_	= null;

	public static Time getIntance()
	{
		if (intance_ == null)
			intance_ = new Time();
		return intance_;
	}

	private Time() {
		isRunnig_ = false;
		nFinalTime_ = 0;
		nInitialTime_ = 0;
	}

	/**
	 * Inicia el tiempo
	 */
	public void timeGo()
	{
		nInitialTime_ = System.currentTimeMillis();
		thread_ = new Thread((Runnable) this);
		isRunnig_ = true;
		thread_.start();
	}

	/**
	 * Corta el tiempo.
	 */
	public void timeStop()
	{
		nFinalTime_ = System.currentTimeMillis() - nInitialTime_;
		isCicle_ = false;
		isRunnig_ = false;
	}

	@Override
	public void run()
	{
		calendar_ = Calendar.getInstance();
		isCicle_ = true;
		long tiempo_actual = 0;
		while (isCicle_) {
			calendar_.setTimeInMillis(System.currentTimeMillis() - nInitialTime_);
			if ((calendar_.getTimeInMillis() / 1000) != tiempo_actual) {
				setChanged();
				Pair<Integer, Object> par = new Pair<Integer, Object>();
				par.setFirstElem(0);
				par.setSecondElem(calendar_.get(Calendar.MINUTE) + ":"
						+ calendar_.get(Calendar.SECOND));
				notifyObservers(par);
				tiempo_actual = calendar_.getTimeInMillis() / 1000;
			}
		}
	}

	/**
	 * @return {@link Long} Tiempo del usuario.
	 */
	public long getFinalTime()
	{
		return nFinalTime_;
	}

	/**
	 * @return {@link Boolean} True ssi esta corriendo el tiempo.
	 */
	public boolean isRunnig()
	{
		return isRunnig_;
	}

}
