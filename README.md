# 获取一个类的所有对象实例

运行时根据Java类获取这个类所有实例化的对象，基于JVMTI，通过JNI调用C++编写的native函数实现<br>

### 使用说明
先引用maven依赖
```
<dependency>
   <groupId>io.github.liubsyy</groupId>
  <artifactId>FindInstancesOfClass</artifactId>
   <version>1.0.3</version>
</dependency>
```

然后直接调用函数 **InstancesOfClass.getInstances(Class<?> targetClass)** 即可获取一个类的所有对象实例
<br>InstancesOfClass所有函数如下:
```java
public class InstancesOfClass {


    /**
     * native方法 : 返回类的实例对象
     * @param targetClass 需要查询实例的Class
     * @param limitNum 数量
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass,int limitNum);

    /**
     * 获取类的所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @return
     */
    public static Object[] getInstances(Class<?> targetClass);

    /**
     * 返回类的实例对象List
     * @param targetClass 需要查询实例的Class
     * @param limitNum 数量
     * @return
     * @param <T>
     */
    public static <T> List<T> getInstanceList(Class<T> targetClass,int limitNum);

    /**
     * 获取类的所有实例对象List
     * @param targetClass 需要查询实例的Class
     * @return
     * @param <T>
     */
    public static <T> List<T> getInstanceList(Class<T> targetClass);
}

```

详见 [InstancesOfClass.java](./src/main/java/com/liubs/findinstances/jvmti/InstancesOfClass.java)

<br>



### 测试用例
[TestInstancesOfClass.java](./src/test/java/TestInstancesOfClass.java)

测试结果: 
```
A所有创建实例:[A@511d50c0, A@60e53b93, A@5e2de80c, A@1d44bcfa, A@266474c2, A@6f94fa3e, A@5e481248, A@66d3c617, A@63947c6b, A@2b193f2d]
A所有查询出来的实例:[A@511d50c0, A@60e53b93, A@5e2de80c, A@1d44bcfa, A@266474c2, A@6f94fa3e, A@5e481248, A@66d3c617, A@63947c6b, A@2b193f2d]
A的所有对象实例是否一致：true
B所有创建实例:[B@b1bc7ed, B@7cd84586, B@30dae81, B@1b2c6ec2, B@4edde6e5, B@70177ecd, B@1e80bfe8, B@66a29884, B@4769b07b, B@cc34f4d]
B所有查询出来的实例:[B@b1bc7ed, B@7cd84586, B@30dae81, B@1b2c6ec2, B@4edde6e5, B@70177ecd, B@1e80bfe8, B@66a29884, B@4769b07b, B@cc34f4d]
B的所有对象实例是否一致：true
A取其中3个,是否所有A实例的子集:true 
```


### 编译打包
一般情况下直接使用jar包即可，里面打包了动态链接库findins.so, findins.dylib和findins.dll文件，分别是基于linux64, macos10.12和win10-64编译的，如果jar包中的链接库和操作系统不兼容或者需要自己编译，可通过compile_* 脚本编译链接库
- macos: compile_mac.sh
- linux: compile_linux.sh
- windows: compile_windows.bat

由于native函数 **InstancesOfClass.getInstances(Class<?> targetClass,int limitNum)**  是JNI实现的，语言是C++，需要安装gcc和g++环境，然后执行脚本生成链接库文件，生成的目标文件在resources目录下
<br>

启动程序后， **InstancesOfClass** 的static方法会读取生成的链接库文件<br>


