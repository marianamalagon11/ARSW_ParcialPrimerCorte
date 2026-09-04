package edu.eci.arsw.math;

public class BBPThread extends Thread{
    private int start;
    private int count;
    private byte [] digits;

    private static final Object lock = new Object();
    private static boolean paused = false;


    public BBPThread(int start, int count){
        this.start = start;
        this.count = count;
    }

    @Override
    public void run(){
        digits = new byte[count];
        double sum = 0;
        int localStart = start;
        long lastCheck = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            if (i % PiDigits.DigitsPerSum == 0) {
                sum = 4 * PiDigits.sum(1, localStart)
                        - 2 * PiDigits.sum(4, localStart)
                        - PiDigits.sum(5, localStart)
                        - PiDigits.sum(6, localStart);
                localStart += PiDigits.DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;

            if (System.currentTimeMillis() - lastCheck >= 5000) {
                System.out.println(Thread.currentThread().getName()
                    + " ha procesado " + (i + 1) + " dígitos");

                BBPThread.pauseAll();
                
                synchronized (lock) {
                    while (paused) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
                lastCheck = System.currentTimeMillis();
            }
        }
    }

    public byte[] getDigits(){
        return digits;
    }

    public static void pauseAll(){
        synchronized (lock) {
            paused = true;
        }
    }

    public static void resumeAll(){
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

}