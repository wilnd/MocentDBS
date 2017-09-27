package decode;
import java.io.File;

/**
 * 测试用于读取图片的EXIF信息
 *
 * @author Winter Lau
 */
public class Iptc {
    private static ExNewsData exNewsData;
    public static void main(String[] args) throws Exception {
        exNewsData = ExNewsData.decodeJpeg("E:\\image\\2017\\9\\19\\kx1505788412353.jpg");
        exNewsData.toString();
    }
}