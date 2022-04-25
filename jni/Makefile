OBJ = MSpringJNIController.cpp
CFLAGS = -shared -fPIC
CC = gcc
TARGET = jni.so

all:
	$(CC) -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" $(OBJ) $(CFLAGS) -o $(TARGET)

clean:
	rm *.o *.so