package utils;

public class Pair<T1, T2> {

	private T1 firstElem;
	private T2 secondElem;

	public Pair() {
		this.firstElem = null;
		this.secondElem = null;
	}

	public Pair(T1 firstElem, T2 secondElem) {
		this.firstElem = firstElem;
		this.secondElem = secondElem;
	}

	public T1 getFirstElem() {
		return firstElem;
	}

	public void setFirstElem(T1 firstElem) {
		this.firstElem = firstElem;
	}

	public T2 getSecondElem() {
		return secondElem;
	}

	public void setSecondElem(T2 secondElem) {
		this.secondElem = secondElem;
	}

	public String toString() {
		String res = "";
		if ((firstElem == null) & (secondElem == null)) {
			res = "(null,null)";
		} else if ((firstElem == null) & (secondElem != null)) {
			res = "(null," + secondElem.toString() + ")";
		} else if ((firstElem != null) & (secondElem == null)) {
			res = "(" + firstElem.toString() + ",null)";
		} else {
			res = "(" + firstElem.toString() + "," + secondElem.toString()
					+ ")";
		}
		return res;
	}

}
