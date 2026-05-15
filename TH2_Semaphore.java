import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class TH2_Semaphore {

    // 1. Cấu trúc dữ liệu chia sẻ
    private static final List<Integer> A = new ArrayList<>();

    // 2. Khai báo các Semaphore
    // mutex = 1: Đảm bảo loại trừ tương hỗ (Mutual Exclusion)
    private static final Semaphore mutex = new Semaphore(1);
    // itemsAvailable = 0: Để luồng Reader đợi cho đến khi có ít nhất 1 phần tử
    private static final Semaphore itemsAvailable = new Semaphore(0);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so luong luong sinh du lieu k: ");
        int k = sc.nextInt();
        System.out.print("Nhap so luong luong xu ly du lieu h: ");
        int h = sc.nextInt();

        System.out.println("--- Chuong trinh Semaphore bat dau chay vo han ---");

        // 3. Luồng ghi (Writer)
        for (int i = 0; i < k; i++) {
            final int id = i + 1;
            new Thread(() -> {
                Random rand = new Random();
                while (true) {
                    try {
                        int val = rand.nextInt(1000);

                        mutex.acquire(); // Lấy thẻ để vào khu vực tranh chấp
                        A.add(val);
                        System.out.println("W" + id + ": <" + val + "> - <" + System.currentTimeMillis() + ">");
                        mutex.release(); // Trả thẻ sau khi xong

                        itemsAvailable.release(); // Tăng số lượng thẻ để báo hiệu có "hàng" mới

                        Thread.sleep(rand.nextInt(1000) + 500);
                    } catch (InterruptedException e) {}
                }
            }).start();
        }

        // 4. Luồng xử lý (Reader)
        for (int i = 0; i < h; i++) {
            final int id = i + 1;
            new Thread(() -> {
                Random rand = new Random();
                while (true) {
                    try {
                        // Đợi cho đến khi itemsAvailable có ít nhất 1 thẻ (tức là A không rỗng)
                        itemsAvailable.acquire(); 

                        mutex.acquire(); // Lấy thẻ để vào đọc dữ liệu
                        int randomIndex = rand.nextInt(A.size());
                        int val = A.get(randomIndex);
                        String ketQua = (val % 2 == 0) ? "Chan" : "Le";
                        System.out.println("R" + id + ": <" + val + "> - <" + ketQua + "> - <" + System.currentTimeMillis() + ">");
                        mutex.release(); // Trả thẻ

                        // Theo đề bài "KHÔNG XÓA", nhưng vì Reader đã tiêu thụ 1 tin hiệu itemsAvailable,
                        // ta phải trả lại 1 thẻ để các Reader khác vẫn có thể vào đọc nếu muốn.
                        itemsAvailable.release(); 

                        Thread.sleep(rand.nextInt(1000) + 1000);
                    } catch (InterruptedException e) {}
                }
            }).start();
        }
    }
}