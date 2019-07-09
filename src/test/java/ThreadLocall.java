public class ThreadLocall {
    public int i;

    public static Integer count = 1;

    public ThreadLocal<Long> threadLocal;

    public ThreadLocall(int i, ThreadLocal<Long> threadLocal) {
        this.i = i;
        this.threadLocal = threadLocal;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public static Integer getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        ThreadLocall.count = count;
    }

    public ThreadLocal<Long> getThreadLocal() {
        return threadLocal;
    }

    public void setThreadLocal(ThreadLocal<Long> threadLocal) {
        this.threadLocal = threadLocal;
    }

}
