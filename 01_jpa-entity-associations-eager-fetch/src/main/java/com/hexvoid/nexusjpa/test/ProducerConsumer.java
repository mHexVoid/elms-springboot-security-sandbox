package com.hexvoid.nexusjpa.test;

public class ProducerConsumer {

    boolean status = false; // Shared flag between producer and consumer

    // Called by Producer thread
    public synchronized void produce(int x){
        while(status){ // If status is true, wait until consumer consumes
            try {
                wait(); // Producer waits (releases the lock)
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        System.out.println("Produced Thread " + x);
        status = true;  // Update flag to indicate item is produced
        notify();       // Wake up consumer if it's waiting
    }

    // Called by Consumer thread
    public synchronized void consume(int x){
        while(!status){ // If status is false, wait for producer
            try {
                wait(); // Consumer waits (releases the lock)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Consumed Thread " + x);
        status = false; // Update flag to indicate item is consumed
        notify();       // Wake up producer if it's waiting
    }
}
