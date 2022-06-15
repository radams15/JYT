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
    private String urlGet(String requestURL){
        try {
            URL url = new URL(requestURL);
            URLConnection conn = url.openConnection();
            // fake request coming from browser
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuffer out = new StringBuffer();
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
            StringBuffer response = new StringBuffer();
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
