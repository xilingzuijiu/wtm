package com.weitaomi.systemconfig.factory;

/**
 * Created by Administrator on 2016/12/7.
 */
public class SingleJavaBeanFactory {
    private static Object object=null;
    public synchronized static<T> T newIntance(Class clazz){
        if (object==null){
            try {
                object=clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T)object;
    }
}
