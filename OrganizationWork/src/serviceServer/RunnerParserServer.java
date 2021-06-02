package serviceServer;

import data.Address;
import data.Organization;
import data.OrganizationType;
import dataNet.Command;
import dataNet.Runner;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class RunnerParserServer {

    private Runner runner;
    private ObjectOutputStream oos;
    private Set<Organization> set;
    private static org.slf4j.Logger logger;

    public RunnerParserServer() {
        logger = LoggerFactory.getLogger(Server.class);
    }

    public RunnerParserServer(Set<Organization> set, Runner runner, ObjectOutputStream oos) {
        this.runner = runner;
        this.oos = oos;
        this.set = set;
        logger = LoggerFactory.getLogger(Server.class);
    }

    /**
     * Получение коллекции элементов
     *
     * @return коллекция элементов
     */
    public Set<Organization> getSet() {
        return set;
    }

    /**
     * Установка коллекции элементов
     *
     * @param set коллекция элементов
     */
    public void setSet(Set<Organization> set) {
        this.set = set;
    }

    /**
     * Полученеи объекта для передачи по сети
     *
     * @return объект для передачи по сети
     */
    public Runner getRunner() {
        return runner;
    }

    /**
     * Установка объекта для передачи по сети
     *
     * @param runner объект для передачи по сети
     */
    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    /**
     * Получение выходного потока
     *
     * @return выходной поток
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * Установка выходного потока
     *
     * @param oos выходной поток
     */
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /**
     * Разбор объекта
     */
    public void parse() {
        //получение команды
        Command com = runner.getCom();
        logger.info("Обработка запроса клиента: " + com);
        switch (com) {
            case INFO:               
                info();
                break;
            case SHOW:
                show();
                break;
            case ADD:
                add();
                break;
            case UPDATE_BY_ID:
                update();
                break;
            case REMOVE_BY_ID:
                removeById();
                break;
            case CLEAR:
                clear();
                break;
            case ADD_IF_MAX:
                addIfMax();
                break;
            case ADD_IF_MIN:
                addIfMin();
                break;
            case FILTER_BY_TYPE:
                filterByType();
                break;
            case FILTER_STARTS_WITH_NAME:
                filterStartsWithName();
                break;
            case FILTER_GREATER_THAN_POSTAL_ADDRESS:
                filterGreaterThanPostalAddress();
                break;
        }
    }

    /**
     * Отправка данных клиенту
     *
     * @param r объект для отправки
     */
    private void writeObject(Runner r) {
        try {
            oos.writeObject(r);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnerParserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Вывод информации о коллекции
     */
    private void info() {
        String text = "Тип коллекции: " + set.getClass().getName() + "\n"
                + "Количество элементов в коллекции: " + set.size();
        Runner r = new Runner(Command.INFO, text);
        writeObject(r);
    }

    /**
     * Вывод всех элементов коллекции
     */
    private void show() {
        Runner r = new Runner(Command.SHOW, set);
        writeObject(r);
    }

    /**
     * Добавление нового элемента в коллекцию
     */
    private void add() {
        Organization organization = (Organization) runner.getObject();
        Organization organizationAdd = new Organization(organization.getName(), organization.getCoordinates(), organization.getAnnualTurnover(), organization.getType());
        set.add(organizationAdd);
        String text = organization + " успешно добавлена в коллекцию";
        Runner r = new Runner(Command.ADD, text);
        writeObject(r);
    }

    /**
     * Обновление значение элемента коллекции по его id
     *
     */
    private void update() {
        Organization organization = (Organization) runner.getObject();
        Optional<Organization> optional = set.stream()
                .filter(o -> o.getId() == organization.getId())
                .findFirst();

        String text;
        if (optional.isPresent()) {
            Organization get = optional.get();
            get.setName(organization.getName());
            get.setCoordinates(organization.getCoordinates());
            get.setAnnualTurnover(organization.getAnnualTurnover());
            get.setType(organization.getType());
            get.setPostalAddress(organization.getPostalAddress());
            text = "Организация с id = " + organization.getId() + " успешно обновлена";
        } else {
            text = "Организация с id = " + organization.getId() + " не найдена в коллекции.";
        }

        Runner r = new Runner(Command.UPDATE_BY_ID, text);
        writeObject(r);
    }

    /**
     * Удаление элемента коллекции по его id
     *
     */
    private void removeById() {
        Long id = (Long) runner.getObject();
        Optional<Organization> optional = set.stream()
                .filter(o -> o.getId() == id)
                .findFirst();
        String text;
        if (optional.isPresent()) {
            set.remove(optional.get());
            text = "Организация с id = " + id + " успешно удалена";
        } else {
            text = "Организация с id = " + id + " не найдена в коллекции.";
        }
        Runner r = new Runner(Command.REMOVE_BY_ID, text);
        writeObject(r);
    }

    /**
     * Очистка коллекции
     */
    private void clear() {
        set.clear();
        String text = "Коллекция очищена.";
        Runner r = new Runner(Command.CLEAR, text);
        writeObject(r);
    }

    /**
     * Добавление новой организации, если его значение капитала больше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMax() {

        Organization organizationMax = set.stream().max((Organization o1, Organization o2) -> {
            return Double.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover());
        }).get();
        Organization organization = (Organization) runner.getObject();
        String text = null;
        if (organization.getAnnualTurnover() > organizationMax.getAnnualTurnover()) {
            Organization organizationAdd = new Organization(organization.getName(), organization.getCoordinates(), organization.getAnnualTurnover(), organization.getType());
            set.add(organizationAdd);
            text = "Организация добавлена!";
        } else {
            text = "Организация не добавлена!";
        }
        Runner r = new Runner(Command.ADD_IF_MAX, text);
        writeObject(r);
    }

    /**
     * Добавление новой организации, если его значение капитала меньше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMin() {
        Organization organizationMin = set.stream().min((Organization o1, Organization o2) -> {
            return Double.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover());
        }).get();
        Organization organization = (Organization) runner.getObject();
        String text = null;
        if (organization.getAnnualTurnover() < organizationMin.getAnnualTurnover()) {
            Organization organizationAdd = new Organization(organization.getName(), organization.getCoordinates(), organization.getAnnualTurnover(), organization.getType());
            set.add(organizationAdd);
            text = "Организация добавлена!";
        } else {
            text = "Организация не добавлена!";
        }
        Runner r = new Runner(Command.ADD_IF_MIN, text);
        writeObject(r);
    }

    /**
     * Вывод элементов коллекции по заданному типу
     *
     */
    private void filterByType() {
        OrganizationType type = (OrganizationType) runner.getObject();
        Set<Organization> setCollect = set.stream().filter((Organization o) -> {
            return o.getType() == type;
        }).collect(Collectors.toSet());
        Runner r = new Runner(Command.FILTER_BY_TYPE, setCollect);
        writeObject(r);
    }

    /**
     * Вывод элементов коллекции, значение поля name начинаются с заданной
     * подстроки
     *
     */
    private void filterStartsWithName() {
        String start = (String) runner.getObject();
        Set<Organization> setCollect = set.stream().filter((Organization o) -> {
            return o.getName().startsWith(start);
        }).collect(Collectors.toSet());
        Runner r = new Runner(Command.FILTER_STARTS_WITH_NAME, setCollect);
        writeObject(r);
    }

    /**
     * Вывод элементов коллекции, значение поля address больше заданного
     * (сравниваем по zipCode)
     *
     */
    private void filterGreaterThanPostalAddress() {
        Address address = (Address) runner.getObject();

        Set<Organization> setCollect = set.stream().filter((Organization o) -> {
            if (o.getPostalAddress() == null) {
                return false;
            }
            return o.getPostalAddress().compareTo(address) > 0;
        }).collect(Collectors.toSet());

        Runner r = new Runner(Command.FILTER_GREATER_THAN_POSTAL_ADDRESS, setCollect);
        writeObject(r);
    }
}
