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
}
