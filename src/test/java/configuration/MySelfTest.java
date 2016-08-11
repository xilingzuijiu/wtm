package configuration;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by supumall on 2016/8/8.
 */
public class MySelfTest {
    private LinkedList<Object> linkedList=new LinkedList<Object>();
    private AtomicInteger atomicInteger=new AtomicInteger(0);
    private int minsize=0;
    private final int maxSize;
    private final Object lock=new Object();
    public MySelfTest(int maxSize) {
        this.maxSize = maxSize;
    }
    public void put(Object object) throws InterruptedException {
        synchronized (lock){
            while (atomicInteger.get()==maxSize){
                lock.wait();
            }
            linkedList.add(object);
            atomicInteger.incrementAndGet();
            System.out.println(atomicInteger.get());
            lock.notify();
            System.out.println("添加完毕：  "+object);
        }
    }
    public Object take() throws InterruptedException {
        Object ret =null;
      synchronized (lock){
          while (atomicInteger.get()==minsize){
              lock.wait();
          }
          ret=linkedList.removeFirst();
          atomicInteger.decrementAndGet();
          lock.notify();
          System.out.println("移除完毕：  "+ret);
      }
      return null;
    }

    public static void main(String[] args) throws InterruptedException {
        final MySelfTest mySelfTest=new MySelfTest(6);
         ThreadLocal threadLocal=new ThreadLocal();
        mySelfTest.put("a");
        mySelfTest.put("b");
        mySelfTest.put("c");
        mySelfTest.put("d");
        mySelfTest.put("e");
        System.out.println(mySelfTest.linkedList.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mySelfTest.put("f");
                    System.out.println("put a word 'f'");
                    TimeUnit.SECONDS.sleep(2);
                    mySelfTest.put("g");
                    System.out.println("put a word 'g'");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(mySelfTest.take());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
