package TH3_ChatBot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatbotServer {
    private static final int PORT = 8888;
    private static final Map<String, String> botBrain = new HashMap<>();

    // Khoi tao tap cau tra loi san co bang Tieng Viet khong dau
    static {
        botBrain.put("hello", "Xin chao! Minh co the giup gi cho ban?");
        botBrain.put("hi", "Chao ban! Ngay moi tot lanh nhe.");
        botBrain.put("ban ten la gi", "Minh la Chatbot thong minh phuc vu bai thuc hanh so 3.");
        botBrain.put("thoi tiet hom nay the nao", "Hom nay thoi tiet rat thich hop de ngoi code lap trinh mang do!");
        botBrain.put("chuc ngu ngon", "Ngu ngon nhe! Dung thuc khuya fix bug nha.");
    }

    public static void main(String[] args) {
        System.out.println("--- SERVER CHATBOT DANG KHOI CHAY (TCP)... ---");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server dang lang nghe tai cong " + PORT);

            while (true) {
                // Cho Client ket noi
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n[SERVER] Co ket noi moi tu: " + clientSocket.getRemoteSocketAddress());

                // Xu ly Client trong mot ham rieng
                handleClient(clientSocket);
            }
        } catch (Exception e) {
            System.err.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String clientMessage;
            
            // Doc lien tuc cac cau hoi tu Client gui len
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("[CLIENT GUI]: " + clientMessage);

                // Neu Client gui tin hieu thoat, ket thuc vong lap
                if (clientMessage.equalsIgnoreCase("exit") || clientMessage.equalsIgnoreCase("bye")) {
                    out.println("Tam biet! Hen gap lai ban sau.");
                    break;
                }

                // Tim kiem cau tra loi phu hop
                String reply = getBotReply(clientMessage);
                
                // Gui cau tra loi ve cho Client
                out.println(reply);
                System.out.println("[SERVER DAP]: " + reply);
            }
        } catch (Exception e) {
            System.out.println("[SERVER] Ket noi voi client bi ngat dot ngot.");
        } finally {
            try {
                socket.close();
                System.out.println("[SERVER] Da dong ket noi voi Client.");
            } catch (Exception e) {
                System.err.println("Khong the dong socket: " + e.getMessage());
            }
        }
    }

    // Ham chuan hoa cau hoi de tim kiem
    private static String getBotReply(String question) {
        // Chuyen ve chu thuong, bo khoang trang va dau cham hoi
        String cleanQuestion = question.toLowerCase()
                                       .replace("?", "")
                                       .trim();

        // Neu khong tim thay cau tra loi khop, tra ve cau mac dinh khong dau
        return botBrain.getOrDefault(cleanQuestion, 
            "Xin loi, cau hoi nay nam ngoai pham vi hieu viet cua minh. Ban co the hoi cau khac khong?");
    }
}
