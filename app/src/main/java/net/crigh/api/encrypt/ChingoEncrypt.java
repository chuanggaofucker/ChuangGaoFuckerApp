package net.crigh.api.encrypt;

public class ChingoEncrypt {
    public static native String cgapiAESEncrypt(String str);

    public static native String cgapiEnrypt(String str, String str2);

    public static native int cgapiVerify(String str, String str2, String str3, String str4);

    static {
        try {
            System.loadLibrary("AMapSDK_Location_v6_6_0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
