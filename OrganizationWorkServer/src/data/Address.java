package data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Адрес
 * @author Admin
 */
public class Address implements Comparable<Address>, Serializable{
    private String street;
    private String zipCode;

    /**
     * Конструктор с параметрами
     * @param street улица
     * @param zipCode почтовый индекс
     */
    public Address(String street, String zipCode) {
        this.street = street;
        this.zipCode = zipCode;
    }

    /**
     * Получение названия улицы
     * @return название улицы
     */
    public String getStreet() {
        return street;
    }

    /**
     * Установка названия улицы
     * @param street название улицы
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Получение почтового индекса
     * @return почтовый индекса
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Установка почтового индекса
     * @param zipCode почтовый индекс
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.street);
        hash = 71 * hash + Objects.hashCode(this.zipCode);
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
        final Address other = (Address) obj;
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.zipCode, other.zipCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "street=" + street + ", zipCode=" + zipCode;
    }

    @Override
    public int compareTo(Address o) {
        return this.zipCode.compareTo(o.getZipCode());
    }    
    
}
