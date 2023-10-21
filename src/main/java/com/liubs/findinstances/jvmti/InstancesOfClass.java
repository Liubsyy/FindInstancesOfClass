package com.liubs.findinstances.jvmti;

import java.net.URL;

/**
 * 根据类获取所有实例：基于JVMTI实现
 * @author Liubsyy
 * @date 2023/10/20 10:08 PM
 **/
public class InstancesOfClass {
    static {
        String bootLib = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("mac")) {
            bootLib = "findins.dylib";
        }else if(osName.contains("linux")) {
            bootLib = "findins.so";
        }else if(osName.contains("windows")) {
            bootLib = "findins.dll";
        }
        if(null == bootLib) {
            throw new UnsupportedOperationException("不支持当前操作系统");
        }
        URL bootLibURL = InstancesOfClass.class.getClassLoader().getResource(bootLib);
        System.load(bootLibURL.getPath());
    }

    /**
     * 返回所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass);

}
