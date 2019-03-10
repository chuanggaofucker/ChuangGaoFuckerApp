package net.crigh.api.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.util.Log;

public class AES {
    public static String Encrypt(String sSrc) throws Exception {
        return Encrypt(sSrc, "6d3121b650e42855"); // chuang gao key
    }

    private static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            Log.e("AES", "String is null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            Log.e("AES", "Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }
}
