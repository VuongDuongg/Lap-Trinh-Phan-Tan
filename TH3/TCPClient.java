package TH3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class TCPClient {
    private static final String SERVER_HOST = "localhost"; // Thay bằng IP thực tế nếu chạy 2 máy khác nhau
    private static final int SERVER_PORT = 9876;

    public static void main(String[] args) {
        System.out.println("--- CLIENT TCP (JAVA) ĐANG KHỞI ĐỘNG ---");

        // Khởi tạo kết nối Socket tới Server
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("[CLIENT] Kết nối đến Server thành công.");

            // Tạo các luồng đọc/ghi dữ liệu giống phía Server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            // 1. Sinh ngẫu nhiên số phần tử N > 100 (Ví dụ: từ 101 đến 130)
            Random random = new Random();
            int n = random.nextInt(30) + 101; 
            System.out.println("[CLIENT] Đang sinh ngẫu nhiên mảng với N = " + n);

            StringBuilder arrayBuilder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                int randomNumber = random.nextInt(1000); // Từ 0 -> 999
                arrayBuilder.append(randomNumber);
                if (i < n - 1) {
                    arrayBuilder.append(" ");
                }
            }
            
            String stringToSend = arrayBuilder.toString();

            // 2. Gửi chuỗi mảng sang cho Server (Sử dụng println để ghi kèm ký tự xuống dòng)
            out.println(stringToSend);
            System.out.println("[CLIENT] Đã gửi mảng thành công.");

            // 3. Chờ nhận phản hồi kết quả chẵn lẻ từ Server
            System.out.println("\n================ KẾT QUẢ ================");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("=========================================");

        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}