# 获取一个类的所有对象实例

运行时根据Java类获取这个类所有实例化的对象，基于JVMTI，通过JNI调用C++编写的native函数实现<br>

### 使用说明
先引用maven依赖
```
<dependency>
   <groupId>io.github.liubsyy</groupId>
  <artifactId>FindInstancesOfClass</artifactId>
   <version>1.0.1</version>
</dependency>
```

然后直接调用函数 **InstancesOfClass.getInstances(Class<?> targetClass)** 即可获取一个类的所有对象实例
```java
public class InstancesOfClass {
    /**
     * native方法 : 返回所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass);

}

```
详见[InstancesOfClass.java](./src/main/java/com/liubs/findinstances/jvmti/InstancesOfClass.java)


### 测试用例
[TestInstancesOfClass.java](./src/test/java/TestInstancesOfClass.java)

测试结果: 
```
[A@566776ad, A@6108b2d7, A@1554909b, A@6bf256fa, A@6cd8737, A@22f71333, A@13969fbe, A@6aaa5eb0, A@3498ed, A@1a407d53]
[A@566776ad, A@6108b2d7, A@1554909b, A@6bf256fa, A@6cd8737, A@22f71333, A@13969fbe, A@6aaa5eb0, A@3498ed, A@1a407d53]
A的所有对象实例是否一致：true
[B@62043840, B@5315b42e, B@2ef9b8bc, B@5d624da6, B@1e67b872, B@60addb54, B@3f2a3a5, B@4cb2c100, B@6fb554cc, B@614c5515]
[B@62043840, B@5315b42e, B@2ef9b8bc, B@5d624da6, B@1e67b872, B@60addb54, B@3f2a3a5, B@4cb2c100, B@6fb554cc, B@614c5515]
B的所有对象实例是否一致：true
```


### 编译打包
一般情况下直接使用jar包即可，里面打包了动态链接库findins.so, findins.dylib和findins.dll文件，分别是基于linux64, macos10.12和win10-64编译的，如果jar包中的链接库和操作系统不兼容或者需要自己编译，可通过compile_* 脚本编译链接库
- macos: compile_mac.sh
- linux: compile_linux.sh
- windows: compile_windows.bat

由于native函数 **InstancesOfClass.getInstances(Class<?> targetClass)**  是JNI实现的，语言是C++，需要安装gcc和g++环境，然后执行脚本生成链接库文件，生成的目标文件在resources目录下
<br>

启动程序后， **[InstancesOfClass]((./src/main/java/com/liubs/findinstances/jvmti/InstancesOfClass.java))** 的static方法会读取生成的链接库文件<br>


