package faith.common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Faith {

    private static Faith faith;

    /*
    初始化
     */
    public static Faith getCommon(){
        if(faith == null){
            faith = new Faith();
        }
        return faith;
    }

    /**
     * 判斷現在時間是不是超過了未來時間,輸入的格式: yyyy-MM-dd HH:mm:ss
     * 2017.12.23
     * @param nowDate
     * @param nextDate
     * @return 超過就回傳true
     */
    public static boolean compareDate(String nowDate,String nextDate){
        boolean tag = false;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sf.parse(nowDate);
            Date d2 = sf.parse(nextDate);
            if(d1.after(d2) || d1.compareTo(d2) == 0){
                tag = true;
            }
        } catch (ParseException e) {
            System.err.println(e);
        }catch (Exception e) {
            System.err.println(e);
        }
        return tag;
    }

    /**
     * 判斷數字
     * 2017.12.23
     * @param strInt
     * @return 是就回傳true
     */
    public static boolean isInt(String strInt){
        boolean chkInt = false;
        try{
            Integer.parseInt(strInt);
            chkInt = true;
        } catch(Exception e){
            chkInt = false;
            System.err.println(e);
        }
        return chkInt;
    }

    /**
     * 傳回昨天日期
     *  2017.12.23
     * @return 得到昨天日期,格式: yyyy/MM/dd
     */
    public String yestDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.DATE,-1);
        return new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
    }

    /**
     * 得到現在日期
     *  2017.12.23
     * @return 得到現在日期,格式:  yyyy-MM-dd HH:mm:ss
     */
    public static String nowDateTime(){

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 得到現在日期
     * 2017.12.23
     * @return 得到現在日期,格式:  yyyy-MM-dd
     */
    public static String nowDate(){

        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 得到現在時間
     * 2017.12.23
     * @return 得到現在時間,格式:  HH:mm:ss
     */
    public static String nowTime(){

        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 輸入API網址得到JSON字串
     * 2017.12.23
     * @param url
     * @return JSON字串
     */
    public static String jsonString(String url){
        String json = null;
        try {
            Connection.Response resp = Jsoup.connect(url)
                    .timeout(0)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .execute();
            json = resp.body();
        } catch (Exception e) {
            System.err.println(e);
        }
        return json;
    }

    /**
     * 得到一個網頁錯誤代碼
     * 2017.12.23
     * @param url
     * @return 錯誤代碼
     */
    public static int httpCode(String url){
        int statcode = 0;
        try {
            Connection.Response resp = Jsoup.connect(url)
                    .validateTLSCertificates(false)
                    .ignoreHttpErrors(true)
                    .execute();
            statcode = resp.statusCode();
        } catch (Exception e) {
            statcode = 999;
        }
        return statcode;
    }

    /**
     * 回傳一個未來時間 格式: yyyy-MM-dd HH:mm:ss
     * 2017.12.23
     * @param min
     * @return 得到未來時間
     */
    public static String futureTime(int min){
        if(min < 1){
            min = 1;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, min);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    /**
     * 執行API(REST風格網址)
     * 2017.12.23
     * @param apiURL
     */
    public void callAPI(String apiURL){
        try {
            Jsoup.connect(apiURL)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .validateTLSCertificates(false)
                    .execute();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * 執行API(傳JSON給SERVER端)
     * @param apiURL
     * @param requestBody
     */
    public void callAPI(String apiURL,String requestBody){
        try {
            Jsoup.connect(apiURL)
                    .requestBody(requestBody)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Accept", "application/json")
                    .header("Connection","keep-alive")
                    .ignoreContentType(true)
                    .post()
                    .body();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * 使用JSOUP爬網頁內容
     * 2017.12.23
     * @param url
     * @return 回傳JSOUP的Document物件內容
     */
    public Document parseHtml(String url){
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla").get();
        } catch (Exception e) {
            System.err.println(e);
        }
        return document;
    }

    /**
     * 20180125
     * 指定編碼的方式爬網頁
     * 主要是因為原本big5會碰到一些難字無法解析,改成Big5_HKSCS後正常
     * @param url
     * @param encodeing
     * @return
     */
    public Document parseHtml(String url,String encodeing){
        if(encodeing == null || "".equals(encodeing)){
            //預設香港增補字符集Big5_HKSCS
            encodeing = "Big5_HKSCS";
        }
        URL siteURL = null;
        HttpURLConnection connection = null;
        Document  doc = null;
        try {
            siteURL = new URL(url);
            connection = (HttpURLConnection)siteURL.openConnection();
            doc = Jsoup.parse(connection.getInputStream(),encodeing,url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 過濾字串
     * 2017.12.23
     * @param str
     * @return 過濾後的字串
     */
    public String regStr(String str){
        String queryStr;
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        queryStr = matcher.replaceAll("").replaceAll(" ","");
        System.out.println("字元過濾結果: "+queryStr);
        return queryStr;
    }
}
