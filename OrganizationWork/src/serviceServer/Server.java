package serviceServer;

import data.Address;
import data.Coordinates;
import data.Organization;
import data.OrganizationType;
import dataNet.Runner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class Server {

    private int port;
    private String textPath;
    private Set<Organization> set;
    private static Logger logger;

    public Server(int port, String textPath) {
        this.port = port;
        this.textPath = textPath;
        set = new LinkedHashSet<>();
        logger = LoggerFactory.getLogger(Server.class);
    }

    /**
     * Запуск сервера
     *
     */
    public void runServer() {
        try {
            logger.info("Чтение коллекции из файла");
            readFile();
            logger.info("Чтение коллекции из файла успешно выполнено");
        } catch (IOException ex) {
            logger.error("Ошибка чтения коллекции из файла");
            return;
        }
        //в отдельном потоке создаем сокет для ожидания подключения клиентов
        logger.info("Запуск севера в отдельном потоке");
        Thread th = new Thread(() -> {
            //создание соединения - подключение к порту
            ServerSocket ss;
            try {
                ss = new ServerSocket(port);
                logger.info("Сервер запущен");
                while (!ss.isClosed()) {
                    //получение соединения с пользователем
                    Socket socket1 = ss.accept();
                    logger.info("Установлено соединение с клиентом");
                    try (Socket socket = socket1) {
                        //создание входного и выходного потоков  
                        InputStream is = socket.getInputStream();
                        OutputStream os = socket.getOutputStream();
                        ObjectInputStream ois = new ObjectInputStream(is);
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        Object object = ois.readObject();
                        //чтение данных
                        //создание объекта для парсинга
                        RunnerParserServer rps = new RunnerParserServer(set, (Runner) object, oos);
                        //парсинг данных
                        rps.parse();
                        ois.close();
                        oos.close();
                        is.close();
                        os.close();

                    } catch (Exception exc) {
                        System.out.println(exc);
                    }
                }
            } catch (IOException ex) {
                logger.error("Ошибка работы сервера");
            }
        });
        th.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            if (next.equals("exit")) {
                write();
                logger.info("Завершение работы программы");
                System.exit(0);
            }
        }

    }

    /**
     * Считывание данных из файла
     *
     * @param path путь к файлу
     */
    private void readFile() throws IOException {
        Path path = Paths.get(textPath);
        //объект для считывания данных из файла
        Scanner scanner = new Scanner(path);
        scanner.useDelimiter("\\n");
        //построчное считывание данных
        while (scanner.hasNext()) {
            String line = scanner.next();
            //разбор каждой строки
            Scanner scannerLine = new Scanner(line);
            scannerLine.useDelimiter(";");
            long id = scannerLine.nextLong();
            //переприсваивание значение статического идентификатора
            Organization.ID = id;
            String name = scannerLine.next();

            int x = scannerLine.nextInt();
            int y = scannerLine.nextInt();
            Coordinates coordinates = new Coordinates(x, y);

            ZonedDateTime creationDate = ZonedDateTime.parse(scannerLine.next());

            double annualTurnover = Double.parseDouble(scannerLine.next());

            OrganizationType type = OrganizationType.valueOf(scannerLine.next());
            String street = scannerLine.next();
            String zipCode = scannerLine.next();
            Address postalAddress = null;
            if (!(street == null || zipCode == null || street.isEmpty() || zipCode.isEmpty())) {
                postalAddress = new Address(street, zipCode);
            }

            Organization organization = new Organization(name, coordinates, annualTurnover, type, postalAddress);
            organization.setCreationDate(creationDate);
            set.add(organization);
            scannerLine.close();
        }
        scanner.close();
        //установка значения для статического идентификатора как большего на 1 чем максимально существующий
        for (Organization o : set) {
            if (o.getId() > Organization.ID) {
                Organization.ID = o.getId() + 1;
            }
        }
    }

    /**
     * Запись данных в файл
     *
     * @throws FileNotFoundException ошибка работы с файлами
     * @throws IOException ошибка создания выходного потока
     */
    private void write() {
        FileOutputStream fos;
        logger.info("Запись коллекции в файл");
        try {
            fos = new FileOutputStream(textPath);
            StringBuilder sb = new StringBuilder();
            //преобразование всех объектов в строку с разделителем ";" и переносом строк
            for (Organization organization : set) {
                sb.append(organization.getId());
                sb.append(";");
                sb.append(organization.getName());
                sb.append(";");
                sb.append(organization.getCoordinates().getX());
                sb.append(";");
                sb.append(organization.getCoordinates().getY());
                sb.append(";");
                sb.append(organization.getCreationDate());
                sb.append(";");
                sb.append(organization.getAnnualTurnover());
                sb.append(";");
                sb.append(organization.getType());
                sb.append(";");
                if (organization.getPostalAddress() == null) {
                    sb.append("");
                    sb.append(";");
                    sb.append(" ");
                } else {
                    sb.append(organization.getPostalAddress().getStreet());
                    sb.append(";");
                    sb.append(organization.getPostalAddress().getZipCode());
                }
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            //запись в файл
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            logger.error("Ошибка записи коллекции в файл - файл не найден");
        } catch (IOException ex) {
            logger.error("Ошибка записи коллекции в файл");
        }

    }
}
