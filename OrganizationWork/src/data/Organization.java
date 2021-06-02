package data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 * @author Admin
 */
public class Organization implements Serializable{
    public static long ID = 1;
    private long id; //автоматическая генерация
    private String name;//не null
    private Coordinates coordinates;//не null
    private ZonedDateTime creationDate;//не null, автоматическая генерация
    private double annualTurnover;//значение больше 0
    private OrganizationType type;//не null
    private Address postalAddress;//может быть null

    /**
     * Конструктор с параметрами
     * @param name название организации
     * @param coordinates координаты организации
     * @param annualTurnover ежегодного оборота организации
     * @param type тип организации
     */
    public Organization(String name, Coordinates coordinates, double annualTurnover, OrganizationType type) {
        if(name == null) throw new IllegalArgumentException("Значение name должно быть задано.");
        if(coordinates == null) throw new IllegalArgumentException("Значение coordinates должно быть задано.");
        if(annualTurnover <=0) throw new IllegalArgumentException("Значение annualTurnover должно быть больше 0.");
        if(type == null) throw new IllegalArgumentException("Значение type должно быть задано.");
        
        this.id = ID;
        ID++;
        this.name = name;
        this.coordinates = coordinates;
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.creationDate = ZonedDateTime.now();
    }

    /**   
     * Конструктор с параметрами
     * @param name название организации
     * @param coordinates координаты организации
     * @param annualTurnover ежегодного оборота организации
     * @param type тип организации
     * @param postalAddress адрес
     * 
     */
    public Organization(String name, Coordinates coordinates, double annualTurnover, OrganizationType type, Address postalAddress) {
        this(name, coordinates, annualTurnover, type);
        this.postalAddress = postalAddress;
    }

    /**
     * Получение идентификатора
     * @return идентификатор
     */
    public long getId() {
        return id;
    }

    /**
     * Установка идентификатора
     * @param id идентификатор
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Получение названия
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Установка названия
     * @param name название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение координат расположения
     * @return координаты расположения
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Установка координат расположения
     * @param coordinates координаты расположения
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Получение даты создания
     * @return дата создания
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Установка даты создания
     * @param creationDate дата создания
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Получение значения ежегодного капитала
     * @return значение ежегодного капитала
     */
    public double getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Установка значения ежегодного капитала
     * @param annualTurnover значение ежегодного капитала
     */
    public void setAnnualTurnover(double annualTurnover) {
        if(annualTurnover <=0) return;
        this.annualTurnover = annualTurnover;
    }

    /**
     * Получение типа организации
     * @return тип организации
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * Установка типа организации
     * @param type тип организации
     */
    public void setType(OrganizationType type) {
        this.type = type;
    }

    /**
     * Получение адреса организации
     * @return адрес организации
     */
    public Address getPostalAddress() {
        return postalAddress;
    }

    /**
     * Установка адреса организации
     * @param postalAddress адрес организации
     */
    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.coordinates);
        hash = 79 * hash + Objects.hashCode(this.creationDate);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.annualTurnover) ^ (Double.doubleToLongBits(this.annualTurnover) >>> 32));
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.postalAddress);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Organization other = (Organization) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Double.doubleToLongBits(this.annualTurnover) != Double.doubleToLongBits(other.annualTurnover)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.coordinates, other.coordinates)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.postalAddress, other.postalAddress)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Organization{" + "id=" + id + ", name=" + name + ", coordinates=" + coordinates + ", creationDate=" + creationDate + ", annualTurnover=" + annualTurnover + ", type=" + type + ", postalAddress=" + postalAddress + '}';
    }
    
}
