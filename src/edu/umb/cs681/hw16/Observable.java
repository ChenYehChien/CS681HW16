package edu.umb.cs681.hw16;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Observable<T> {
	private List<Observer<T>> observers;
	private AtomicBoolean change = new AtomicBoolean(false); //new added
	private ReentrantLock lock = new ReentrantLock();
	
	public Observable () {
		observers = new LinkedList<Observer<T>>(); 
	}
	//LE
	//it is not the same as some observer already in the set
	public void addObserver(Observer<T> o) {
		lock.lock();
		if (observers.contains(o))
			return;
		observers.add(o);
		lock.unlock();
	}
	//LE
	//Deletes an observer from the set of observers of this object
	public void deleteObserver(Observer<T> o) {
		lock.lock();
		observers.remove(o);
		lock.unlock();
	}
	protected void setChanged() {
		change.getAndSet(true);
	};
	protected void clearChanged() {
		change.getAndSet(false);
	};
	public boolean hasChanged() {
		//true if and only if the setChanged method has been called 
		//more recently than the clearChanged method on this object; 
		//false otherwise.
		if (change.get())
			return true;
		return false;
	}
	public void notifyObservers(T obj) {
		LinkedList<Observer<T>> observersLocal = new LinkedList<Observer<T>>();
		lock.lock();
		observersLocal = new LinkedList<Observer<T>>(observers);
		lock.unlock();
		if (hasChanged()) {
			for (Observer<T> o: observersLocal) {
				o.update(this, obj);
			}
		}
	}
	
}