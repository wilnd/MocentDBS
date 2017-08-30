import com.mocent.shiro.PasswordHash;
import com.mocent.shiro.ShiroByteSource;
import com.mocent.util.DigestUtils;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

/**
 * Created by hadoop on 2017/8/16.
 */
public class Degist {
    public static void main(String[] args) {
        ByteSource byteSource = ShiroByteSource.of("admin");
        System.out.println(byteSource);
        String pwd =  DigestUtils.hashByShiro("md5", byteSource, "test", 1);
        System.out.println(pwd);
    }
}
