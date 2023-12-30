#include <jni.h>
#include <jvmti.h>
#include "com_liubs_findinstances_jvmti_InstancesOfClass.h"

typedef struct CallbackParam {
    int instance_count;
    int max_instances;
} CallbackParam;

static jvmtiIterationControl JNICALL objectInstanceCallback(jlong class_tag, jlong size, jlong* tag_ptr, void* user_data) {

    CallbackParam* data = (CallbackParam*)user_data;
    if(data->max_instances <= 0) {
        return JVMTI_ITERATION_ABORT;
    }

    data->instance_count++;
    *tag_ptr = 1;

    // 检查是否达到了限定数量
    if (data->instance_count >= data->max_instances) {
        return JVMTI_ITERATION_ABORT; // 停止迭代
    }

    return JVMTI_ITERATION_CONTINUE;
}

JNIEXPORT jobjectArray JNICALL Java_com_liubs_findinstances_jvmti_InstancesOfClass_getInstances(JNIEnv* env, jclass clazz, jclass targetClazz,jint limitNum) {
    JavaVM* vm;
    env->GetJavaVM(&vm);

    jvmtiEnv* jvmti;
    vm->GetEnv((void**)&jvmti, JVMTI_VERSION_1_0);

    jvmtiCapabilities capabilities = {0};
    capabilities.can_tag_objects = 1;
    jvmti->AddCapabilities(&capabilities);

    CallbackParam user_data;
    user_data.instance_count = 0;
    user_data.max_instances = limitNum;
    jvmti->IterateOverInstancesOfClass(targetClazz, JVMTI_HEAP_OBJECT_EITHER,
                                       objectInstanceCallback, &user_data);

    jlong tag = 1;
    jint count;
    jobject* instances;
    jvmti->GetObjectsWithTags(1, &tag, &count, &instances, NULL);

    //printf("Found %d objects with tag\n", count);

    // 转换jobject* 为 jobjectArray 并返回
    jobjectArray result = env->NewObjectArray(count, targetClazz, NULL);
    for (int i = 0; i < count; i++) {
        env->SetObjectArrayElement(result, i, instances[i]);
    }

    jvmti->Deallocate((unsigned char*)instances);
    return result;
}
