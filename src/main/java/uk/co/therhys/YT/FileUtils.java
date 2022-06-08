package uk.co.therhys.YT;

import java.io.*;

public class FileUtils {

    static String readFile(File file){
        String out;

        try {
            FileInputStream is = new FileInputStream(file);

            byte bytes[] = new byte[(int) file.length()];

            is.read(bytes);

            out = new String(bytes, "UTF-8");

            is.close();
        } catch (Exception e) {
            e.printStackTrace();

            out = "";
        }

        return out;
    }

    static void writeFile(File file, String data){
        try {
            FileOutputStream writer = new FileOutputStream(file.getAbsolutePath());

            writer.write(data.getBytes());

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
