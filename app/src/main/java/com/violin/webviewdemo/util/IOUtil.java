package com.violin.webviewdemo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;

public class IOUtil {

    /**
     * 根据绝对路径读取文件内容
     * @param path
     * @return
     */
    public static String readFile(String path){
        FileInputStream fileInputStream = null;
        String result = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
                result = new String(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                    fileInputStream = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path){
        return new File(path).exists();
    }

    /**
     * 写文件
     * 如果路径或文件不存在，会自动创建路径和文件
     * 支持多级路径创建
     * @param path
     * @param text
     */
    public static boolean writeFile(String path, String text){
        FileWriter fileWriter = null;
        try {
            File file = new File(path);
            createFile(file);
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 写文件
     * @param path
     * @param is
     */
    public static void writeFile(String path, InputStream is) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            createFile(file);
            fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用递归方式创建多级路径
     * @param file
     */
    public static void createDir(File file){
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                createDir(file.getParentFile());
            }
            file.mkdir();
        }
    }

    /**
     * 创建文件
     * @param file
     */
    public static void createFile(File file){
        createDir(file.getParentFile());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将输入流转换为字符串 完成后输入流将被关闭
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static String inStream2String(InputStream is) {
        ByteArrayOutputStream baos = null;
        String str = null;
        try {
            baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            str = new String(baos.toByteArray());
            str = "".equals(str) ? null : str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}