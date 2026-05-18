import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiThreadSearch {

    private static final List<Integer> A = new ArrayList<>();

    private static final List<Integer> finalResults = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Nhap so luong phan tu N: ");
        int N = sc.nextInt();

        System.out.print("Nhap so luong luong K: ");
        int K = sc.nextInt();

        sc.close();
        
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            A.add(rand.nextInt(100));
        }

        //A.add(199);

        Thread[] threads = new Thread[K];
        int chunkSize = N / K;

        for (int i = 0; i < K; i++) {
            final int threadId = i;
            final int start = i * chunkSize;
            final int end = (i == K - 1) ? N : (i + 1) * chunkSize; 

            List<Integer> subList = A.subList(start, end);
            
            System.out.println(subList);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    int val = A.get(j);
                    if (isPrime(val)) {
                        System.out.println("T" + (threadId + 1) + ": <" + val + "> : <" + System.currentTimeMillis() + ">");                        
                        finalResults.add(val);
                    }
                }
            });
            threads[i].start();
        }

        try {
            for (Thread t : threads) {
                t.join(); // Đợi cho đến khi luồng t kết thúc
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- Ket qua tong hop cuoi cung ---");
        System.out.println("Tong so luong so nguyen to tim thay: " + finalResults.size());
        System.out.println("Danh sach: " + finalResults);
    }

    /**
     * Hàm kiểm tra số nguyên tố
     */
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}