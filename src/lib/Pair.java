package lib;

public class Pair<E,T> {
	
	/* ############################
	  
	  Classe permettant de creer des
	  paire d'objets
	  
	   ############################*/
	
	private E e ; 
	private T t ;
	
	public Pair(E e,T t) {
		this.e=e ;
		this.t=t ; 
	}
	
	/**
	 * Return the first value of a pair
	 * @return
	 */
	public E getE() {
		return e ; 
	}
	
	/**
	 * Return the second value of a pair 
	 * @return
	 */
	public T getT() {
		return t ; 
	}
	
}