package data;

import dataNet.Command;
import dataNet.Runner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import serviceClient.ClientSocket;

/**
 * Класс для работы с коллекцией организаций
 *
 * @author Admin
 */
public class OrganizationWork {

    private Scanner scanner;
    private ArrayList<String> listHistory;
    private ClientSocket clientSocket;

    /**
     * Конструктор с параметрами
     *
     * @param clientSocket объект для отправки запроса на сервер
     */
    public OrganizationWork(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        listHistory = new ArrayList();
        scanner = new Scanner(System.in, "CP1251");
    }

    /**
     * Запуск работы с коллекцией организацияй
     */
    public void run() {

        help();
        System.out.println("");
        while (true) {
            System.out.println("Введите команду...");
            String command = scanner.nextLine();
            parse(command);
        }
    }

    /**
     * Обработка команд
     *
     * @param command команда
     */
    private void parse(String command) {
        System.out.println("*************************************************");
        String[] split = command.split(" ");
        addToHistory(command);
        Runner runner = new Runner();
        switch (split[0]) {
            case "help":
                help();
                break;
            case "info":

                runner.setCom(Command.INFO);
                runner.setObject(null);
                clientSocket.send(runner);
                break;
            case "show":
                runner = new Runner();
                runner.setCom(Command.SHOW);
                runner.setObject(null);
                clientSocket.send(runner);
                break;
            case "add":
                Organization createOrganization = createOrganization();
                runner.setCom(Command.ADD);
                runner.setObject(createOrganization);
                clientSocket.send(runner);
                break;
            case "update":
                System.out.println(Arrays.toString(split));
                if (split.length < 2) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                long id;
                try {
                    id = Long.parseLong(split[1]);
                } catch (Exception exc) {
                    System.out.println("Не корректно введен аргумент!");
                    break;
                }
                createOrganization = createOrganization();
                createOrganization.setId(id);
                runner.setCom(Command.UPDATE_BY_ID);
                runner.setObject(createOrganization);
                clientSocket.send(runner);
                break;
            case "remove":
                if (split.length < 2) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                try {
                    id = Long.parseLong(split[1]);
                } catch (Exception exc) {
                    System.out.println("Не корректно введен аргумент!");
                    break;
                }
                runner.setCom(Command.REMOVE_BY_ID);
                runner.setObject(id);
                clientSocket.send(runner);
                break;
            case "clear":
                runner.setCom(Command.CLEAR);
                runner.setObject(null);
                clientSocket.send(runner);
                break;
            case "execute_script":
                if (split.length < 2) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                executeScript(split[1]);
                break;
            case "exit":
                exit();
                break;
            case "add_if_max":
                createOrganization = createOrganization();
                runner.setCom(Command.ADD_IF_MAX);
                runner.setObject(createOrganization);
                clientSocket.send(runner);
                break;
            case "add_if_min":
                createOrganization = createOrganization();
                runner.setCom(Command.ADD_IF_MIN);
                runner.setObject(createOrganization);
                clientSocket.send(runner);
                break;
            case "":
                break;
            case "history":
                history();
                break;
            case "filter_by_type":
                if (split.length < 2) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                OrganizationType type;
                try {
                    type = OrganizationType.valueOf(split[1]);
                } catch (Exception exc) {
                    System.out.println("Не корректно введен аргумент!");
                    break;
                }
                runner.setCom(Command.FILTER_BY_TYPE);
                runner.setObject(type);
                clientSocket.send(runner);
                break;
            case "filter_starts_with_name":
                if (split.length < 2) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                runner.setCom(Command.FILTER_STARTS_WITH_NAME);
                runner.setObject(split[1]);
                clientSocket.send(runner);
                break;
            case "filter_greater_than_postal_address":
                if (split.length < 3) {
                    System.out.println("Не введен аргумент команды!");
                    break;
                }
                Address address = new Address(split[1], split[2]);
                runner.setCom(Command.FILTER_GREATER_THAN_POSTAL_ADDRESS);
                runner.setObject(address);
                clientSocket.send(runner);
                break;
            default:
                System.out.println("Команда не найдена. Повторите ввод...");
        }
        System.out.println("");
    }

    /**
     * Вывод справки по доступным командам
     */
    private void help() {
        System.out.println("***Список всех команд:***");
        System.out.println("help");
        System.out.println("info");
        System.out.println("show");
        System.out.println("add");
        System.out.println("update id");
        System.out.println("remove id");
        System.out.println("clear");
        System.out.println("execute_script file_name");
        System.out.println("exit");
        System.out.println("add_if_max");
        System.out.println("add_if_min");
        System.out.println("history");
        System.out.println("filter_by_type type");
        System.out.println("filter_starts_with_name name");
        System.out.println("filter_greater_than_postal_address postalAddress");
    }

    /**
     * Вывод последних 13 команд
     */
    private void history() {
        System.out.println("Список последних 13 введенных команд:");
        for (String command : listHistory) {
            System.out.println(command);
        }
    }

    /**
     * Создание объекта типа Organization
     *
     * @return объект типа Organization
     */
    private Organization createOrganization() {
        //!!!обрабатываем каждый параметр по отдельности и перехватываем ошибки для повторного ввода!!!

        String name;
        while (true) {
            System.out.println("Введите название организации:");
            try {
                name = scanner.next();
                break;
            } catch (Exception exc) {
                scanner.next();
                System.out.println("Ошибка ввода данных, повторите ввод!");
            }
        }

        System.out.println("Введите координаты расположения организации:");
        int x;
        while (true) {
            System.out.println("Координата 'x':");
            try {
                x = scanner.nextInt();
                break;
            } catch (Exception exc) {
                scanner.next();
                System.out.println("Ошибка ввода данных, повторите ввод!");
            }
        }

        int y;
        while (true) {
            System.out.println("Координата 'y':");
            try {
                y = scanner.nextInt();
                if (y < -425) {
                    System.out.println("Введенное значение должно быть больше -425, повторите ввод!");
                    continue;
                }
                break;
            } catch (Exception exc) {
                scanner.next();
                System.out.println("Ошибка ввода данных, повторите ввод!");
            }
        }

        Coordinates coordinates = new Coordinates(x, y);

        double annualTurnover;
        while (true) {
            System.out.println("Введите значение ежегодного оборота организации:");
            try {
                annualTurnover = Double.parseDouble(scanner.next());
                if (annualTurnover <= 0) {
                    System.out.println("Введенное значение должно быть больше 0, повторите ввод!");
                    continue;
                }
                break;
            } catch (Exception exc) {
                scanner.next();
                System.out.println("Ошибка ввода данных, повторите ввод!");
            }
        }

        OrganizationType type;
        while (true) {
            System.out.println("Введите тип организации (PUBLIC, GOVERNMENT, TRUST, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY):");
            try {
                type = OrganizationType.valueOf(scanner.next());
                break;
            } catch (Exception exc) {
                System.out.println("Ошибка ввода данных, повторите ввод!");
            }
        }

        System.out.println("Введите адрес организации (пустая строка - без адреса):");
        System.out.println("Введите название улицы:");
        scanner.nextLine();
        String street = scanner.nextLine();
        //scanner.next();
        Address address = null;
        //if (!street.toLowerCase().equals("null")) {
        if (!street.isEmpty()) {
            System.out.println("Введите почтовый индекс организации:");
            String zipCode = scanner.next();

            if (!(street.isEmpty() || zipCode.isEmpty())) {
                address = new Address(street, zipCode);
            }
            scanner.nextLine();
        }
        return new Organization(name, coordinates, annualTurnover, type, address);
    }

    /**
     * Добавление команды без аргумента в историю
     *
     * @param command команда с аргументом
     */
    private void addToHistory(String command) {
        if (listHistory.size() == 13) {
            listHistory.remove(0);
        }
        listHistory.add(command.split(" ")[0]);
    }

    /**
     * Считывание и исполнение скрипта из файла
     *
     * @param textPath путь к файлу
     * @return список команд, null - в случае ошибки чтения файла
     */
    private void executeScript(String textPath) {
        List<String> readAllLines = null;
        try {
            //чтение всех строк файла
            readAllLines = Files.readAllLines(Paths.get(textPath));
            //поочередное выполнение команд
            for (String command : readAllLines) {
                parse(command);
            }
        } catch (IOException ex) {
            System.out.println("Ошибка чтения файла!");
            Logger.getLogger(OrganizationWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Завершение работы программы без сохранения
     */
    private void exit() {
        System.exit(0);
    }

}
