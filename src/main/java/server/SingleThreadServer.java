package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SingleThreadServer {
    private ServerSocket serverSocket;
    private int port;

    SingleThreadServer() {
        System.out.println("A new instance (" + this + ") has been created!");
    }

    public void openPort(int p) {
        this.port = p;
    }

    public boolean serverSetting(ServerSocket serverSocket) {
        InetSocketAddress inetSocketAddress;
        try {
            inetSocketAddress = new InetSocketAddress(10002);
            serverSocket.bind(inetSocketAddress);
            this.serverSocket = serverSocket;
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
            System.out.println("Port error " + this.port);
            return false;
        }
    }

    public void readDataAndEcho() {
        try {
            if (this.serverSetting(new ServerSocket())) {
                while (true) {
                    Socket clientSocket = this.serverSocket.accept();
                    InetAddress inetAddress = clientSocket.getInetAddress();
                    System.out.println(inetAddress.getHostAddress() + " 로부터 접속했습니다.");
                    OutputStream out = clientSocket.getOutputStream();
                    InputStream in = clientSocket.getInputStream();
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line = null;

                    while(true){
                        try{
                            line = br.readLine();
                            if(line == null){
                                System.out.println(clientSocket.getLocalAddress() + "가 Null을 보내서 아무짓도 안 했음");
                                break;
                            }
                        }catch (SocketException e){
                            System.out.println(clientSocket.getLocalAddress() + "와의 연결이 끊어졌습니다.");
                            pw.close();
                            br.close();
                            clientSocket.close();
                            break;
                        }

                        System.out.println("Client로부터 받은 message : " + line);
                        pw.println(line);
                    }
                }
            }
            System.out.println("server socket cannot created");
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println(String.format("%s OCCURRED", e.getClass().getSimpleName()));
            System.out.println(Arrays.asList(e.getStackTrace()));
        }
    }

    public static void main(String[] args) {
        SingleThreadServer server = new SingleThreadServer();
        server.openPort(args.length > 0 ? Integer.parseInt(args[0]) : 10002);

        try {
            server.readDataAndEcho();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
        }

    }
}

