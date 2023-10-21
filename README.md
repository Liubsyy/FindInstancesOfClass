# 获取一个类的所有对象实例

运行时根据一个类获取这个类创建的所有实例，基于jvmti实现<br>

### 使用方式
引用maven依赖
```
<dependency>
   <groupId>io.github.liubsyy</groupId>
  <artifactId>FindInstancesOfClass</artifactId>
   <version>1.0.0</version>
</dependency>
```
里面打包的动态链接库是基于win64, linux64, macos10.12

再调用 InstancesOfClass.getInstances(Class<?> targetClass)方法即可
```java
package com.liubs.findinstances.jvmti;

public class InstancesOfClass {

    /**
     * native方法 : 返回所有的实例对象
     * @param targetClass 需要查询实例的Class
     * @return
     */
    public static native Object[] getInstances(Class<?> targetClass);

}

```

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
Disconnected from the target VM, address: '127.0.0.1:56516', transport: 'socket'
Found 10 objects with tag
Found 10 objects with tag
```

### 编译打包
一般情况下直接使用jar包即可，如果操作系统不符合或者需要自己编译，可通过compile脚本编译依赖库
首先安装gcc和g++环境，然后按操作系统执行脚本，执行完生成的链接库在resources目录下

- macos: compile_mac.sh
- linux: compile_linux.sh
- windows: compile_windows.bat

写过jni的朋友应该都懂，这里就不详细介绍了



