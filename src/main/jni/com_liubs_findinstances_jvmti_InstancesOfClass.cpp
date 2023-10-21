#include <jni.h>
#include <jvmti.h>
#include "com_liubs_findinstances_jvmti_InstancesOfClass.h"


static jvmtiIterationControl JNICALL objectInstanceCallback(jlong class_tag, jlong size, jlong* tag_ptr, void* user_data) {
    *tag_ptr = 1;
    return JVMTI_ITERATION_CONTINUE;
}

JNIEXPORT jobjectArray JNICALL Java_com_liubs_findinstances_jvmti_InstancesOfClass_getInstances(JNIEnv* env, jclass clazz, jclass targetClazz) {
    JavaVM* vm;
    env->GetJavaVM(&vm);

    jvmtiEnv* jvmti;
    vm->GetEnv((void**)&jvmti, JVMTI_VERSION_1_0);

    jvmtiCapabilities capabilities = {0};
    capabilities.can_tag_objects = 1;
    jvmti->AddCapabilities(&capabilities);

    jvmti->IterateOverInstancesOfClass(targetClazz, JVMTI_HEAP_OBJECT_EITHER,
                                       objectInstanceCallback, NULL);

    jlong tag = 1;
    jint count;
    jobject* instances;
    jvmti->GetObjectsWithTags(1, &tag, &count, &instances, NULL);

    printf("Found %d objects with tag\n", count);

    // 转换jobject* 为 jobjectArray 并返回
    jobjectArray result = env->NewObjectArray(count, targetClazz, NULL);
    for (int i = 0; i < count; i++) {
        env->SetObjectArrayElement(result, i, instances[i]);
    }

    jvmti->Deallocate((unsigned char*)instances);
    return result;
}
