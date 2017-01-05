/**
* Classe SortedList.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SortedList<T extends Comparable<T>> implements Iterable<T> {

	List<T> l;
	
	public SortedList()
	{
		l = new ArrayList<T>();
	}
	
	public int size() {
		return l.size();
	}
	
	public void clear() {
		l.clear();
	}
	public void add(T element) {
		l.add(element);
		Collections.sort(l);
	}
	public void remove(T element)
	{
		l.remove(element);
	}

	@Override
	public Iterator<T> iterator() {
		return l.iterator();
	}
	
	public T first(){
		return l.get(0);
	}

	public boolean contains(T ev) {
		return l.contains(ev);
	}
}

