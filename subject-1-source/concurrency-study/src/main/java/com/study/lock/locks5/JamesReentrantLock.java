package com.study.lock.locks5;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class JamesReentrantLock implements Lock {

    private boolean isFair;

    public JamesReentrantLock(boolean isFair){
        this.isFair = isFair;
    }

    JamesAQS mask = new JamesAQS(){
        public boolean tryLock(int acquires){
            if (isFair){
                return tryFairLock(acquires);
            }else{
                return tryNonFairLock(acquires);
            }
        }

        //尝试获取独占锁
        public boolean tryNonFairLock(int acquires) {
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                if (writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    owner.set(Thread.currentThread());  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (owner.get() == Thread.currentThread()){
                writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }

        public boolean tryFairLock(int acquires){
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                JamesAQS.WaitNode head = waiters.peek();
                if (head!=null && head.thread == Thread.currentThread()&&
                        writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    owner.set(Thread.currentThread());  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (owner.get() == Thread.currentThread()){
                writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }


        //尝试释放独占锁
        public boolean tryUnlock(int releases) {
            //若当前线程没有 持有独占锁
            if(owner.get()!= Thread.currentThread()){
                throw new IllegalMonitorStateException();       //抛IllegalMonitorStateException
            }

            int wc= writeCount.get();
            int nextc = wc - releases;      //计算 独占锁剩余占用
            writeCount.set(nextc);      //不管是否完全释放，都更新count值

            if (nextc==0){  //是否完全释放
                owner.compareAndSet(Thread.currentThread(), null);
                return true;
            }else{
                return false;
            }

        }

    };

    public void lock(){
        mask.lock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public boolean tryLock(){
        return mask.tryLock(1);
    }

    @Override
    public void unlock(){
        mask.unlock();
    }


    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

}
