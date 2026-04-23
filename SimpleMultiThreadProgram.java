public class SimpleMultiThreadProgram {
    
    public static void main(String[] args) {
        // Tạo hai luồng đơn giản
        Thread thread1 = new Thread(new MyRunnable("Thread 1"));
        Thread thread2 = new Thread(new MyRunnable("Thread 2"));
        
        // Khởi động các luồng
        thread1.start();
        thread2.start();
        
        // Chờ các luồng hoàn thành
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Tat ca cac luong da hoan thanh.");
    }
    
    static class MyRunnable implements Runnable {
        private String threadName;
        
        public MyRunnable(String name) {
            this.threadName = name;
        }
        
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println(threadName + " dang chay: " + i);
                try {
                    Thread.sleep(100); // Ngủ 100ms để thấy sự xen kẽ
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
