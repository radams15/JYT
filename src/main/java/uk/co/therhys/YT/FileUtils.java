package uk.co.therhys.YT;

import java.io.*;

public class FileUtils {

    public static byte[] readFileBytes(File file){
        byte[] out;

        try {
            FileInputStream is = new FileInputStream(file);

            out = new byte[(int) file.length()];

            is.read(out);

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            out = null;
        }

        return out;
    }

    public static String readFile(File file){
        String out;

        try {
            out = new String(readFileBytes(file), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            out = "";
        }

        return out;
    }

    public static void writeFile(File file, byte[] data){
        try {
            FileOutputStream writer = new FileOutputStream(file.getAbsolutePath());

            writer.write(data);

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            return fileName.substring(i+1);
        }

        return "";
    }

    public static void writeFile(File file, String data){
        writeFile(file, data.getBytes());
    }
}
