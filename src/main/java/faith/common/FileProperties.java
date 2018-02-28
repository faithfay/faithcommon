package faith.common;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.ResourceBundle;


public class FileProperties {

    private static FileProperties fileProperties;
    private ResourceBundle rb;

    //PASS掉SSL
    static {
        try{
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //放在最外面所以只剩下檔名名稱config_zh_TW.properties
    private FileProperties() {

        rb = ResourceBundle.getBundle("config", new Locale("zh","TW"));
    }

    public static FileProperties getInstance() {
        if (fileProperties == null) {
            fileProperties = new FileProperties();
        }
        return fileProperties;
    }

    //抓設定檔參數
    public String getKey(String key) {

        return rb.getString(key);
    }
}
