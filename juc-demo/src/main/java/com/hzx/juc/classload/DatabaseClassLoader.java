package com.hzx.juc.classload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Scanner;
import org.springframework.util.StringUtils;

/**
 * @author: bocai.huang
 * @create: 2019-12-20 15:03
 **/
public class DatabaseClassLoader extends ClassLoader {

    public DatabaseClassLoader() {
        super(ClassLoader.getSystemClassLoader());
    }

    public static final String driver = "/Users/huangbocai/";
    public static final String fileTyep = ".class";

    @Override
    public Class findClass(String name) {
        byte[] data = loadClassData(name);
        return defineClass(null, data, 0, data.length, null);
    }

    public byte[] loadClassData(String name) {
        FileInputStream fis = null;
        byte[] data = null;
        try {
            File file = new File(driver + name + fileTyep);
            System.out.println(file.getAbsolutePath());
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = fis.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("loadClassData-IOException");
        }
        return data;
    }

    public static void main(String[] args)
        throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        String c = sc.nextLine();



        while (!StringUtils.isEmpty(c)) {
            System.out.println(1);

            DatabaseClassLoader classLoader = new DatabaseClassLoader();
            Class clazz = classLoader.findClass("TestClass");

            System.out.println(clazz == null);

            Method method = clazz.getMethod("ttt",null);

            Object o = clazz.newInstance();

            method.invoke(o,null);



            c = sc.nextLine();
        }
    }

}
