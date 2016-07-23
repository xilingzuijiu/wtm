package configuration;

import com.weitaomi.systemconfig.util.DateUtils;
import common.BaseContextCase;
import org.junit.Test;

/**
 * Created by supumall on 2016/7/23.
 */
public class DateTest extends BaseContextCase{
    @Test
    public void testDemo(){
        System.out.println(DateUtils.formatYYYYMMddHHmmssSSS());
    }
}
