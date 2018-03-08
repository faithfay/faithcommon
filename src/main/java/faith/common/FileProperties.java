package faith.common;

import javax.net.ssl.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

    //抓內部參數檔
    private FileProperties() {

        rb = ResourceBundle.getBundle("config", new Locale("zh","TW"));
    }

    public static FileProperties getInstance() {
        if (fileProperties == null) {
            fileProperties = new FileProperties();
        }
        return fileProperties;
    }

    /*
    * 抓內部參數檔
    * 會抓class load裡面的config_zh_TW.properties
    * */
    public String getKey(String key) {

        return rb.getString(key);
    }

//  抓外部參數檔
    public String getExtKey(String key) {

        return getExternalKeys().getString(key);
    }

    /*
    * 抓外部參數檔
    * 檔案固定放在C:/Project/config_zh_TW.properties
    * */
    public static ResourceBundle getExternalKeys(){
        File file = new File("C:/Project");
        ResourceBundle bundle = null;
        try{
            URL urls[] = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            bundle = ResourceBundle.getBundle("config", Locale.TAIWAN, loader);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
