package com.liubs.findinstances.jvmti;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

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
            throw new UnsupportedOperationException("不支持当前操作系统");
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

        //[jar路径] file:/Users/liubs/.m2/repository/io/github/liubsyy/FindInstancesOfClass/1.0.1/FindInstancesOfClass-1.0.2.jar!/findins.dylib
        //[SpringBoot嵌套jar] file:/Users/liubs/IdeaProjects/TestSpring/bin/TestSpring.jar!/BOOT-INF/lib/FindInstancesOfClass-1.0.2.jar!/findins.dylib
        String jarPath = path.substring("file:".length(), path.indexOf("jar!")+"jar".length());

        File curDir = new File(jarPath);
        curDir = curDir.getParentFile();
        
        File nativeLibFile = new File(curDir.getAbsolutePath()+"/"+nativeLib);
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
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass);

}
