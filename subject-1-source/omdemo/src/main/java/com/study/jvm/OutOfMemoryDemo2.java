package com.study.jvm;

import java.nio.ByteBuffer;
import java.util.ArrayList;

// 堆外内存溢出
// 控制堆外内存大小：-XX:MaxDirectMemorySize=128m -XX:+HeapDumpOnOutOfMemoryError
public class OutOfMemoryDemo2 {
    static ArrayList<Object> space = new ArrayList<Object>();

    public static void main(String[] args) throws Exception {
        // 内存泄漏 最终会导致  内存溢出
        for (int i = 0; i < 1000; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 16);
            byteBuffer.put(new byte[1024 * 1024 * 5]);
            space.add(byteBuffer);
            Thread.sleep(2000L);
        }
    }
}
