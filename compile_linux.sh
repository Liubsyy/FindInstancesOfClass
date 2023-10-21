JHOME=$JAVA_HOME
g++ src/main/jni/com_liubs_findinstances_jvmti_InstancesOfClass.cpp -shared -o src/main/resources/findins.so -fPIC -I$JHOME/include -I$JHOME/include/linux -static-libgcc -static-libstdc++
