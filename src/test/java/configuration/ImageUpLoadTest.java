package configuration;

import com.weitaomi.systemconfig.fileManage.UpYun;
import com.weitaomi.systemconfig.util.StreamUtils;
import common.BaseContextCase;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.Test;

import java.io.*;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ImageUpLoadTest extends BaseContextCase {
    @Test
    public void testUpYun() throws IOException {

        File file=new File("C:\\Users\\Administrator\\Desktop\\7B696DBD5A9D49405D6EED7F61D0B2A9.png");
        try {
            UpYun upYun=new UpYun("weitaomi","weitaomi","Weitaomi@Woyun");
            InputStream fileInputStream=new FileInputStream(file);
            byte[] bytes= StreamUtils.InputStreamTOByte(fileInputStream);
            System.out.println(new String(bytes));
            boolean flag= upYun.writeFile("/member/showMessage/131313131312.png",bytes);
            System.out.println("=============> Is success? "+flag);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
