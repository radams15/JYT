package uk.co.therhys.YT;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.X509Certificate;

public class Net {

    private SSLSocketFactory getTrustAllFactory() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509ExtendedTrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {}
                    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {}
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        return sslContext.getSocketFactory();
    }

    private String httpUrlGet(String requestURL){
                /*try {
            System.out.println("A");
            HttpsURLConnection.setDefaultSSLSocketFactory(getTrustAllFactory());
            System.out.println("B");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String urlGet(String requestURL){
        try {
            URL url = new URL(requestURL);
            URLConnection conn = url.openConnection();
            // fake request coming from browser
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder out = new StringBuilder();
            String f;
            while(true) {
                f = in.readLine();
                if(f == null) break;
                out.append(f);
            }

            in.close();

            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error!";
    }

    private String curlGet(String requestURL){
        try {
            String[] commands = new String[] {
                "curl", "-k", "-X", "GET", requestURL
            };

            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String get(String requestURL){
        return urlGet(requestURL).replaceAll("[^\\x20-\\x7e]", "");
    }
}
