package com.itsdf07.nf877ble.blelib.common;

import java.util.concurrent.ConcurrentLinkedQueue;


public class BLEOperationQueue<T> {
    private ConcurrentLinkedQueue<T> bleOperations=new ConcurrentLinkedQueue<>();


    synchronized public void add(T operation){

        bleOperations.add(operation);
    }

    synchronized public T removeFirst(){
        return  bleOperations.poll();
    }

    public void  clear(){
        bleOperations.clear();
    }

    public int getOperationSize(){
        return  bleOperations.size();
    }

    public T getFirst(){
        return bleOperations.peek();
    }

    public boolean isEmpty(){
        return bleOperations.isEmpty();
    }
}
