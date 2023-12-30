package com.liubs.findinstances.jvmti;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据类获取所有实例：基于JVMTI实现
 * @author Liubsyy
 * @date 2023/10/20 10:08 PM
 **/
public class InstancesOfClass {

    static {
        String nativeLib = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("mac")) {
            nativeLib = "findins.dylib";
        }else if(osName.contains("linux")) {
            nativeLib = "findins.so";
        }else if(osName.contains("windows")) {
            nativeLib = "findins.dll";
        }
        if(null == nativeLib) {
            throw new UnsupportedOperationException("不支持当前操作系统"+osName);
        }
        URL nativeLibURL = InstancesOfClass.class.getClassLoader().getResource(nativeLib);
        if(null == nativeLibURL) {
            System.out.println("找不到jar包内的"+nativeLib);
        }else if("jar".equals(nativeLibURL.getProtocol())) {

            //读取jar包里面的链接库,SpringBoot嵌套jar也支持
            loadLibraryFromJar(nativeLibURL,nativeLib);

        }else {
            System.load(nativeLibURL.getPath());
        }

    }


    private static void loadLibraryFromJar(URL nativeLibURL,String nativeLib) {

        String path = nativeLibURL.getPath();

        //处理jar包的逻辑
        //[jar路径] file:/Users/liubs/.m2/repository/io/github/liubsyy/FindInstancesOfClass/1.0.1/FindInstancesOfClass-1.0.2.jar!/findins.dylib
        //[SpringBoot嵌套jar] file:/Users/liubs/IdeaProjects/TestSpring/bin/TestSpring.jar!/BOOT-INF/lib/FindInstancesOfClass-1.0.2.jar!/findins.dylib
        String jarPath = path.substring("file:".length(), path.indexOf("jar!")+"jar".length());

        //路径包含%20替换成空格
        try {
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //在jar包的目录下生成so/dylib/dll
        File curDir = new File(jarPath);
        curDir = curDir.getParentFile();
        
        File nativeLibFile = new File(curDir.getAbsolutePath(),nativeLib);
        nativeLibFile.deleteOnExit();

        try (InputStream is = nativeLibURL.openStream(); OutputStream os = Files.newOutputStream(nativeLibFile.toPath())) {
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.load(nativeLibFile.getAbsolutePath());
    }


    /**
     * native方法 : 返回所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @param limitNum 数量
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass,int limitNum);

    /**
     * 获取所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @return
     */
    public static Object[] getInstances(Class<?> targetClass) {
        return getInstances(targetClass,Integer.MAX_VALUE);
    }


    public static <T> List<T> getInstanceList(Class<T> targetClass,int limitNum){
        List<T> result = new ArrayList<>();
        Object[] instances = getInstances(targetClass,limitNum);
        for(Object o : instances) {
            result.add((T)o);
        }
        return result;
    }
    public static <T> List<T> getInstanceList(Class<T> targetClass){
        return getInstanceList(targetClass,Integer.MAX_VALUE);
    }

}
