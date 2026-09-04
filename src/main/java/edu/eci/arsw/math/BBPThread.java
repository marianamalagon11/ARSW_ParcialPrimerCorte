package edu.eci.arsw.math;

public class BBPThread extends Thread{
    public int start;
    public int count;
    public byte [] digits;


    public BBPThread{
        this.start = start;
        this.count = count;
    }

    @Override 
    public void run(){
        for (int i = start; i < count; i++){
            digits = PiDigits.getDigits(start, count);
        }
    }

    public byte[] getDigits(){
        return digits;
    }
}