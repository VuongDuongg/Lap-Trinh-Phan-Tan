package TH3_ChatBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatbotClient {
    private static final String SERVER_HOST = "localhost"; // Thay bang IP may Server khi test 2 may khac nhau
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        System.out.println("--- DANG KET NOI DEN SERVER CHATBOT ---");

        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Ket noi thanh cong! Nhap cau hoi cua ban duoi day.");
            System.out.println("(Go 'exit' hoac 'bye' de ket thuc cuoc tro chuyen)\n");

            String userInput;
            
            // Vong lap chat lien tuc
            while (true) {
                System.out.print("Ban: ");
                userInput = stdIn.readLine();

                if (userInput == null || userInput.trim().isEmpty()) {
                    continue;
                }

                // Gui cau hoi cho Server
                out.println(userInput);

                // Nhan va hien thi ket qua tu Server
                String serverReply = in.readLine();
                System.out.println("Chatbot: " + serverReply + "\n");

                // Kiem tra dieu kien ket thuc
                if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("bye")) {
                    System.out.println("--- DA KET THUC CHUONG TRINH CHAT ---");
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("Loi ket noi tu phia Client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}