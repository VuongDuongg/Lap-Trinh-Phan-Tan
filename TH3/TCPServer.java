package TH3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static final int PORT = 9876;

    public static void main(String[] args) {
        System.out.println("--- SERVER TCP (JAVA) ĐANG CHẠY, CHỜ KẾT NỐI... ---");

        // Khởi tạo ServerSocket lắng nghe tại cổng 9876
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Chấp nhận kết nối từ Client (Block cho tới khi có Client kết nối)
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("\n[SERVER] Đã kết nối với Client: " + clientSocket.getRemoteSocketAddress());

                    // Tạo luồng đọc dữ liệu từ Client gửi lên
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                    // Tạo luồng ghi dữ liệu gửi trả về Client
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // 1. Đọc chuỗi mảng số nguyên từ Client
                    String clientData = in.readLine();
                    if (clientData != null) {
                        System.out.println("[SERVER] Đã nhận mảng số nguyên từ Client.");

                        // 2. Tác vụ tự định nghĩa: ĐẾM SỐ CHẴN LẺ
                        String resultResponse = countEvenOdd(clientData.trim());

                        // 3. Gửi chuỗi kết quả ngược lại cho Client
                        out.println(resultResponse);
                        System.out.println("[SERVER] Đã gửi kết quả chẵn lẻ thành công.");
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi xử lý Client: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm đếm số lượng số chẵn và số lẻ
    private static String countEvenOdd(String data) {
        if (data.isEmpty()) {
            return "Dữ liệu mảng trống!";
        }

        // Tách chuỗi bằng khoảng trắng
        String[] elements = data.split("\\s+");
        int totalElements = elements.length;
        
        int evenCount = 0;
        int oddCount = 0;

        try {
            for (String el : elements) {
                int val = Integer.parseInt(el);
                if (val % 2 == 0) {
                    evenCount++;
                } else {
                    oddCount++;
                }
            }
            
            // Trả về chuỗi kết quả (Sử dụng ký tự đặc biệt [END] để Client biết khi nào kết thúc chuỗi nhận)
            return String.format("Kết quả phân tích mảng từ TCP Server:\n" +
                                 "- Tổng số phần tử: %d\n" +
                                 "- Số lượng số CHẴN: %d\n" +
                                 "- Số lượng số LẺ: %d", 
                                 totalElements, evenCount, oddCount);
        } catch (NumberFormatException e) {
            return "Lỗi: Mảng chứa ký tự không phải số nguyên hợp lệ!";
        }
    }
}