package configuration;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by supumall on 2016/7/15.
 */
public class MathTest {
    @Test
    public void testBigDecimal(){
        BigDecimal a=BigDecimal.valueOf(1200);
        BigDecimal b=BigDecimal.valueOf(10000);
        BigDecimal c=a.divide(b,1,BigDecimal.ROUND_FLOOR);
        System.out.println(c);
    }
    @Test
    public void testOxMath(){
        int num=12;
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<32;i++){
           int t=(num&0x80000000>>>i)>>>(31-i);
            buffer.append(t);
            System.out.println(t);
        }
        System.out.println(buffer);
    }
}
