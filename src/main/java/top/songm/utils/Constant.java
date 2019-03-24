package top.songm.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 常数
 * @author songm
 * @datetime 2019/2/12 16:13
 */
public class Constant {

    // AES密钥
    public static final String secretKey = "qazwsxedcrfvtgby";

    public static final String appid = "wx545267dffd88ff64";
    public static final String secret = "2eb40a3134487ce040f7bd5eb63f6223";
    public static final String grant_type = "authorization_code";

    // session储存时间(秒)
    public static final int SESSION_SAVE_TIME = 60 * 60;

    /**
     * 获取保存文件的位置,jar所在目录的路径
     *
     * @return
     */
    public static String getUplodFilePath() {
        String path = Constant.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1, path.length());
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/upload/";
    }
}
