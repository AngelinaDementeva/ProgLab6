package serviceClient;

import dataNet.Runner;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Клиент
 *
 * @author Alien
 */
public class ClientSocket {

    private final int port;
    private final int timeOut;

    public ClientSocket(int port, int timeOut) {
        this.port = port;
        this.timeOut = timeOut;
    }

    /**
     * Отправка объекта на сервер
     *
     * @param runner объект для передачи
     */
    public void send(Runner runner) {

        try {
            SocketAddress sockaddr = new InetSocketAddress("localhost", port);
            Socket socket = new Socket();
            socket.connect(sockaddr, timeOut);
            //создания потоков ввода/вывода
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);
            oos.writeObject(runner);
            oos.flush();
            //объект парсинда объекта runner
            RunnerParserClient rpc = new RunnerParserClient();
            //получение объекта с сервера
            runner = (Runner) ois.readObject();
            //парсинг
            rpc.parse(runner);
            oos.close();
            ois.close();
            os.close();
            is.close();
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Ошибка работы с сокетами");
        }
    }

}
