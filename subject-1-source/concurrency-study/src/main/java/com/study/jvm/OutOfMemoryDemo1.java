package com.study.jvm;

import java.util.ArrayList;

// 资源占用过多或者资源未释放，内存溢出
// -Xmx512m -server -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump2.hprof
public class OutOfMemoryDemo1 {
    static ArrayList<Object> space = new ArrayList<Object>();

    public static void main(String[] args) throws Exception {
        // 内存泄漏 最终会导致  内存溢出
        for (int i = 0; i < 1000; i++) {
            space.add(new byte[1024 * 1024 * 64]); // 64兆
            Thread.sleep(500L);
        }
    }
}
