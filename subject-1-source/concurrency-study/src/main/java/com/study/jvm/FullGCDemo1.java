package com.study.jvm;

// 频繁调用system.gc导致fullgc次数过多
// 使用server模式运行 开启GC日志
// -Xmx512m -server -verbose:gc -XX:+PrintGCDetails
public class FullGCDemo1 {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            byte[] tmp = new byte[1024 * 1024 * 256]; // 256兆

            System.out.println("running...");
            Thread.sleep(2000L);
        }
    }
}


// 变量的作用域来判断