JHOME=$JAVA_HOME
g++ src/main/jni/com_liubs_findinstances_jvmti_InstancesOfClass.cpp -shared -o src/main/resources/findins.dylib -I$JHOME/include -I$JHOME/include/darwin -mmacosx-version-min=10.12