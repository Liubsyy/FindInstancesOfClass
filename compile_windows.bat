set JHOME=%JAVA_HOME%
g++ src\main\jni\com_liubs_findinstances_jvmti_InstancesOfClass.cpp -shared -o src\main\resources\findins.dll -I%JHOME%\include -I%JHOME%\include\win32 -static