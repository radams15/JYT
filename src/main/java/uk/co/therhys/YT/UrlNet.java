package uk.co.therhys.YT;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sun.misc.*;

public class UrlNet extends Net {

    private final static int chunkSize = 1024;
    public Result post_auth(String urlStr, String data, HashMap headers, String username, String password){
        Result res = new Result();

        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            byte[] authBytes = (username+":"+password).getBytes("UTF-8");
            String authStr = "Basic " + new BASE64Encoder().encode(authBytes);
            http.setRequestProperty("Authorization", authStr);

            for(int i=0 ; i<headers.entrySet().size() ; i++){
                Map.Entry entry = (Map.Entry) headers.entrySet().toArray()[i];

                http.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }


            byte[] toSend = data.getBytes();

            http.setFixedLengthStreamingMode(toSend.length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();

            OutputStream os = null;
            try {
                os = http.getOutputStream();
                os.write(toSend);
            }finally {
                if(os != null) {
                    os.close();
                }
            }

            InputStream is =  new BufferedInputStream(http.getInputStream());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] line = new byte[chunkSize];
            while(true){
                int len = is.read(line, 0, chunkSize);
                if(len != -1) {
                    for(int i=0 ; i<len ; i++){
                        stream.write(line[i]);
                    }
                }else{
                    break;
                }
            }

            res.data = stream.toByteArray();
            res.length = res.data.length;
        }catch (Exception e){
            res.hadError = true;
            res.error = e;
            return res;
        }

        return res;
    }

    public Result get(String urlStr, HashMap headers){
        Result res = new Result();

        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("GET");
            http.setDoOutput(true);

            for(int i=0 ; i<headers.entrySet().size() ; i++){
                Map.Entry entry = (Map.Entry) headers.entrySet().toArray()[i];

                http.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();

            InputStream is =  new BufferedInputStream(http.getInputStream());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] line = new byte[chunkSize];
            while(true){
                int len = is.read(line, 0, chunkSize);
                if(len != -1) {
                    for(int i=0 ; i<len ; i++){
                        stream.write(line[i]);
                    }
                }else{
                    break;
                }
            }

            res.data = stream.toByteArray();
            res.length = res.data.length;

        }catch (Exception e){
            res.hadError = true;
            res.error = e;
            e.printStackTrace();
            return res;
        }

        return res;
    }
}
