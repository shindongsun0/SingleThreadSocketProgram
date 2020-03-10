package client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class SingleThreadClient {
    public static void main(String[] args) {

        Socket socket = new Socket();
        OutputStream out = null;
        InputStream in = null;
        PrintWriter pw = null;
        BufferedReader br = null;

        try {
            socket.connect(new InetSocketAddress("localhost", 10002), 10000);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            out = socket.getOutputStream();
            in = socket.getInputStream();
            pw = new PrintWriter(new OutputStreamWriter(out), true);
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while((line = keyboard.readLine()) != null && !line.equals("quit")) {
                pw.println(line);
                String echo = br.readLine();
                System.out.println("서버로부터 전달받은 문자열 : " + echo);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println(String.format("%s OCCURRED", e.getClass().getSimpleName()));
            System.out.println(Arrays.asList(e.getStackTrace()));
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    pw.close();
                    br.close();
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
                System.out.println(String.format("%s OCCURRED", e.getClass().getSimpleName()));
                System.out.println(Arrays.asList(e.getStackTrace()));
            }

            System.out.println("socket 연결 종료");
        }

    }
}
