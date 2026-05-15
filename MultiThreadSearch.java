import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiThreadSearch {

    // 1. Khai báo cấu trúc dữ liệu dùng chung A
    private static final List<Integer> A = new ArrayList<>();
    // private static final int N; // Số lượng phần tử (> 100 theo yêu cầu)
    // private static final int K;   // Số lượng luồng (threads) sẽ chạy song song
    
    // Danh sách an toàn (thread-safe) để tổng hợp kết quả cuối cùng từ nhiều luồng
    private static final List<Integer> finalResults = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        // 1. Khởi tạo Scanner để nhập dữ liệu
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Nhap so luong phan tu N: ");
        int N = sc.nextInt();

        System.out.print("Nhap so luong luong K: ");
        int K = sc.nextInt();

        // Đóng scanner sau khi dùng xong
        sc.close();
        
        // Khởi tạo dữ liệu ngẫu nhiên cho danh sách A
        Random rand = new Random();
        for (int i = 0; i < N - 1; i++) {
            A.add(rand.nextInt(1000)); // Sinh số ngẫu nhiên từ 0-999
        }

        A.add(199); // Thêm một phần tử ngẫu nhiên nữa để đủ N phần tử

        Thread[] threads = new Thread[K];
        int chunkSize = N / K; // Chia nhỏ danh sách A cho mỗi luồng xử lý

        // 2 & 3. Tạo k luồng và phân chia nhiệm vụ trên các đoạn không giao nhau
        for (int i = 0; i < K; i++) {
            final int threadId = i;
            final int start = i * chunkSize; // Vị trí bắt đầu của luồng i
            final int end = (i == K - 1) ? N : (i + 1) * chunkSize; 

            // Lay ra danh sach cac phan tu ma luong nay se xu ly
            List<Integer> subList = A.subList(start, end);
            
            // In ra toan bo cac phan tu cua luong nay
            System.out.println(subList);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    int val = A.get(j);
                    // Kiểm tra nếu là số nguyên tố
                    if (isPrime(val)) {
                        // In ra màn hình theo đúng cú pháp yêu cầu trong ảnh
                        // T_i: <kết quả> : <thời điểm tìm thấy>
                        System.out.println("T" + (threadId + 1) + ": <" + val + "> : <" + System.currentTimeMillis() + ">");
                        
                        // Thêm vào danh sách tổng hợp
                        finalResults.add(val);
                    }
                }
            });
            threads[i].start(); // Kích hoạt luồng chạy
        }

        // 4. Luồng chính (main) đợi tất cả các luồng con hoàn thành và tổng hợp
        try {
            for (Thread t : threads) {
                t.join(); // Đợi cho đến khi luồng t kết thúc
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // In kết quả tổng hợp ra màn hình
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