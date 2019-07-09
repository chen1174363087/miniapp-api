import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.apache.ibatis.jdbc.SelectBuilder.FROM;
import static org.apache.ibatis.jdbc.SelectBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

public class ThreadLocalTest {


    @Test
    public void threadLocalTest() {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ThreadLocal<Long> threadLocal = new ThreadLocal<>();
                    threadLocal.set(System.currentTimeMillis());
                    ThreadLocall threadLocalTest = new ThreadLocall(1, threadLocal);
                    synchronized (ThreadLocall.count) {
                        ThreadLocall.count++;
                    }
                    System.out.println(threadLocalTest.getThreadLocal().get() + "==" + ThreadLocall.count);

                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void PriorityBlockingQueueTest() {
        PriorityBlockingQueue<PriorityBlockingBean> PriorityBlockingQueue = new PriorityBlockingQueue<PriorityBlockingBean>();
        for (int i = 0; i < 10; i++) {
            PriorityBlockingBean blockingBean = new PriorityBlockingBean(i, "bean" + i);
            PriorityBlockingQueue.put(blockingBean);
            System.out.println(PriorityBlockingQueue.remove().toString() + "==");
        }
        Iterator<PriorityBlockingBean> blockingBeans = PriorityBlockingQueue.iterator();
        while (blockingBeans.hasNext()) {
            System.out.println(blockingBeans.next().toString());
        }
    }

    @Test
    public void syncronousBlockingQueueTest() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom
                    .current()
                    .nextInt();
            try {
                queue.add(producedElement);
                System.out.println(queue.size());
                countDownLatch.countDown();
                Thread.sleep(5000);
                System.out.println("put");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                countDownLatch.countDown();
                System.out.println("take" + consumedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(producer);
        //executor.execute(producer);没有存储值得功能
        executor.execute(consumer);

        try {
            countDownLatch.await();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

    }

    @Test
    public void blockingDequeTest() {
        BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<Integer>();
        blockingDeque.add(1);
    }

    @Test
    public void lockTest() {
        ReentrantLock lock = new ReentrantLock();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int i = 0;
        while (i < 10) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        lock.lock();//
                        System.out.println("concurrentNumber " + Num.num++);
                        countDownLatch.countDown();
                    } finally {
                        lock.unlock();
                    }
                }
            });
            i++;
        }

        try {
            countDownLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void ConcurrentMapTest() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        ConcurrentMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        int i = 0;
        do {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    hashMap.put("key" + Num.num, Num.num);
                    hashtable.put("key" + Num.num, Num.num);
                    concurrentMap.put("key" + Num.num, Num.num);
                    countDownLatch.countDown();
                }
            });

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("hashMap" + hashMap.get("key" + Num.num));
                    System.out.println("hashtable" + hashtable.get("key" + Num.num));
                    System.out.println("concurrentMap" + concurrentMap.get("key" + Num.num));
                }
            });
            i++;
        } while (i < 11);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }


    @Test
    public void ConcurrentNavigableMapTest() {
        ConcurrentNavigableMap<String, Integer> concurrentNavigableMap = new ConcurrentSkipListMap<>();
        int i = 0;
        while (i < 10) {
            concurrentNavigableMap.put("key" + i, i);
            i++;
        }
        while (i > 1) {
            System.out.println(concurrentNavigableMap.get("key" + i));
            i--;
        }
        ConcurrentNavigableMap<String, Integer> concurrentNavigableMap1 = concurrentNavigableMap.headMap("key5");
        Set<String> entry = concurrentNavigableMap1.keySet();
        Iterator iterator = entry.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }


    /**
     * 屏障线程的运行时机：等待的线程数量=parties之后，CyclicBarrier打开屏障之前。
     */
    class BarrierAction implements Runnable {
        private BarrierActionBean num;

        public BarrierAction(BarrierActionBean num) {
            this.num = num;
        }

        @Override
        public void run() {
            num.count3 = num.getCount1() + num.getCount2();
            System.out.println(num.hashCode() + "count3 is " + num.count3);
            System.out.println("BarrierAction done" + num.getCount3());
        }
    }

    @Test
    public void CyclicBarrierTest() {
        BarrierActionBean num = new BarrierActionBean(1, 2, 0);
        BarrierAction barrierAction = new BarrierAction(num);


        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, barrierAction);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int count = 0;
        while (count < 2) {
            int i = 0;
            //重置cyclicBarrier.isBroken(),达到重用的效果
            if (cyclicBarrier.isBroken()) {
                cyclicBarrier.reset();
            }
            while (i < 2) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + "init");
                        try {
                            System.out.println(Thread.currentThread().getName() + "await");
                            cyclicBarrier.await();
                            //屏障线程的运行时机：等待的线程数量=parties之后，CyclicBarrier打开屏障之前。
                            System.out.println(Thread.currentThread().getName() + "BarrierAction do" + num.getCount3());
                            Random random = new Random();
                            if (num.getCount1() == 1) {
                                num.setCount1(random.nextInt(100));
                            }
                            if (num.getCount2() == 2) {
                                num.setCount2(random.nextInt(100));
                            }

                            System.out.println(Thread.currentThread().getName() + "done" + num.hashCode());
                            System.out.println(Thread.currentThread().getName() + num.getCount3() + "==" + num.getCount1());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                });
                i++;
            }
            count++;
        }


        try {
            //为了看结果
            //Thread.sleep(10000);
            executorService.shutdown();
            executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }


    class ExchangerRroducer implements Runnable {
        Exchanger<List<ExchangerTestBean>> exchangerTestBeanExchanger;
        List<ExchangerTestBean> data;

        public ExchangerRroducer(Exchanger<List<ExchangerTestBean>> exchangerTestBeanExchanger, List<ExchangerTestBean> data) {
            this.exchangerTestBeanExchanger = exchangerTestBeanExchanger;
            this.data = data;
        }

        @Override
        public void run() {
            while (true) {
                if (this.data.size() <= 0) {
                    for (int i = 0; i < 10; i++) {
                        this.data.add(new ExchangerTestBean("hello" + i, "hello" + i));
                    }
                }
                try {
                    System.out.println("producer data.size()  before exchange" + data.size());
                    this.exchangerTestBeanExchanger.exchange(this.data);
                    System.out.println("producer exchanged" + data.toString());
                    this.data.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExchangerComsumer implements Runnable {
        Exchanger<List<ExchangerTestBean>> exchanger;
        List<ExchangerTestBean> data;

        public ExchangerComsumer(Exchanger<List<ExchangerTestBean>> exchanger, List<ExchangerTestBean> data) {
            this.exchanger = exchanger;
            this.data = data;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("Comsumer data.size()  before exchange" + data.size());
                try {
                    this.exchanger.exchange(this.data);
                    System.out.println("Comsumer exchanged" + data.toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void ExchangerTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Exchanger<List<ExchangerTestBean>> exchanger = new Exchanger<>();
        //data必须有两个
        List<ExchangerTestBean> data1 = new ArrayList<>();
        List<ExchangerTestBean> data = new ArrayList<>();
        executorService.execute(new ExchangerRroducer(exchanger, data));
        executorService.execute(new ExchangerComsumer(exchanger, data1));
        try {
            executorService.awaitTermination(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void SemaphoreTest() {
        int totalJob = 10;
        int threadPoolSize = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch countDownLatch = new CountDownLatch(totalJob);
        Semaphore semaphore = new Semaphore(threadPoolSize);
        for (int i = 0; i < totalJob; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    semaphore.tryAcquire(1);
                    System.out.println("第" + index + "个人处理中...");
                    semaphore.release(1);
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void CallableTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            final int index = i;
            try {
                System.out.println(executorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        countDownLatch.countDown();
                        return "Callable done" + index;
                    }
                }).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        try {
            countDownLatch.await();
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScheduledThreadPoolServiceTest() {
        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(5);
        try {
            executorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    System.out.println("hello o");
                }
            }, 1L, 5L, TimeUnit.SECONDS);//周期性执行

            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println("hello o");
                }
            }, 1L, 5L, TimeUnit.SECONDS);//周期性执行

            System.out.println(executorService.schedule(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return "hell0";
                }
            }, 5L, TimeUnit.SECONDS).get());//延迟执行

            Thread.sleep(

                    50000
            );
            executorService.shutdown();
            executorService.awaitTermination(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    class MyRecursiveAction extends RecursiveAction {

        private long workLoad = 0;

        public MyRecursiveAction(long workLoad) {
            this.workLoad = workLoad;
        }

        @Override
        protected void compute() {

            //if work is above threshold, break tasks up into smaller tasks
            if (this.workLoad > 16) {
                System.out.println("Splitting workLoad : " + this.workLoad);

                List<MyRecursiveAction> subtasks =
                        new ArrayList<MyRecursiveAction>();

                subtasks.addAll(createSubtasks());

                for (RecursiveAction subtask : subtasks) {
                    subtask.fork();
                }

            } else {
                System.out.println("Doing workLoad myself: " + this.workLoad);
            }
        }

        private List<MyRecursiveAction> createSubtasks() {
            List<MyRecursiveAction> subtasks =
                    new ArrayList<MyRecursiveAction>();

            MyRecursiveAction subtask1 = new MyRecursiveAction(this.workLoad / 2);
            MyRecursiveAction subtask2 = new MyRecursiveAction(this.workLoad / 2);

            subtasks.add(subtask1);
            subtasks.add(subtask2);

            return subtasks;
        }

    }

    class MyRecursiveTask extends RecursiveTask<Long> {

        private long workLoad = 0;

        public MyRecursiveTask(long workLoad) {
            this.workLoad = workLoad;
        }

        protected Long compute() {

            //if work is above threshold, break tasks up into smaller tasks
            if (this.workLoad > 16) {
                System.out.println("Splitting workLoad : " + this.workLoad);

                List<MyRecursiveTask> subtasks =
                        new ArrayList<MyRecursiveTask>();
                subtasks.addAll(createSubtasks());

                for (MyRecursiveTask subtask : subtasks) {
                    subtask.fork();
                }

                long result = 0;
                for (MyRecursiveTask subtask : subtasks) {
                    result += subtask.join();
                }
                return result;

            } else {
                System.out.println("Doing workLoad myself: " + this.workLoad);
                return workLoad * 3;
            }
        }

        private List<MyRecursiveTask> createSubtasks() {
            List<MyRecursiveTask> subtasks =
                    new ArrayList<MyRecursiveTask>();

            MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
            MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);

            subtasks.add(subtask1);
            subtasks.add(subtask2);

            return subtasks;
        }
    }

    @Test
    public void ForkJoinPoolTest() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        MyRecursiveAction myRecursiveAction = new MyRecursiveAction(24);
        forkJoinPool.invoke(myRecursiveAction);

        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);
        long mergedResult = forkJoinPool.invoke(myRecursiveTask);
        System.out.println("mergedResult = " + mergedResult);
    }

    volatile int name = 1;

    @Test
    public void AtomicTest() {
        AtomicInteger value = new AtomicInteger();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        for (int j = 0; j < 10; j++) {
//            System.out.println(value.getAndIncrement());
//        }
        System.out.println("========");
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(
                        executorService.submit(new Callable<AtomicInteger>() {
                            @Override
                            public AtomicInteger call() {
                                // readWriteLock.writeLock().lock();

                                //value.addAndGet(10);
                                value.incrementAndGet();
                                //value.getAndAdd(10);

                                //  readWriteLock.writeLock().unlock();
                                name++;
                                System.out.println("name" + name);
                                countDownLatch.countDown();
                                //System.out.println(value.getAndDecrement());
                                return value;
                            }
                        }).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }


    class AtomicBean {
        int value;

        public AtomicBean(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @Test
    public void AtomicReferenceTest() {
        AtomicBean noRef = new AtomicBean(1);
        AtomicBean yesRef = new AtomicBean(1);

        AtomicReference<AtomicBean> integerAtomicReference = new AtomicReference<>(yesRef);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            final int j = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第"+j+"次"+noRef.getValue()+"=="+integerAtomicReference.get().getValue());
                    noRef.setValue(noRef.getValue() + 1);
                    integerAtomicReference.get().setValue(integerAtomicReference.get().getValue() + 1);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void enumTest(){
        System.out.println(Person.valueOf("MAN"));
        System.out.println(Person.values().length);
        System.out.println(Person.MAN.getName());
        System.out.println(Person.MAN.getValue());
    }
}

