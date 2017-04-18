package controller;

import java.util.LinkedList;
import java.util.Queue;

import backend.util.ReadonlyGameplayState;
/**
 * This is a Buffer designed to hold information/updates from frontend/backend.
 * We designed this way so that it's possible to save the update in the
 * communication controller without truly informing the updates
 * 
 * @author ncp14
 *
 * @param <T>
 */
//TODO: Dude it's the year 2017, why do you still have a LinkedList? ArrayDeque is where its at man.
public class MyBuffer<T> implements Buffer<T> {
	private Queue<T> myBuffer;
	private CommunicationController myListener;
	public MyBuffer() {
		this.myBuffer = new LinkedList<T>();
	}
	@Override
	public void addToBuffer(ReadonlyGameplayState gameState) {
		this.myBuffer.add((T) gameState);
	}
	@Override
	public T getBufferHead() throws ParsingErrorException {
		return this.myBuffer.poll();
	}
	@Override
	public boolean isBufferEmpty() {
		return this.myBuffer.isEmpty();
	}
	@Override
	public void clear() {
		this.myBuffer.clear();
	}
	@Override
	public void addToBuffer(T t) {
		// TODO Auto-generated method stub
		
	}
}