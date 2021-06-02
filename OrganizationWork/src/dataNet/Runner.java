package dataNet;

import java.io.Serializable;
import java.util.Objects;

/**
 * Объект для передачи по сети
 * @author Admin
 */
public class Runner implements Serializable{
    
    Command com; //команда
    Object object; //объект

    public Runner() {
    }

    public Runner(Command com, Object object) {
        this.com = com;
        this.object = object;
    }

    /**
     * Получение команды
     * @return команда
     */
    public Command getCom() {
        return com;
    }

    /**
     * Установка команды
     * @param com команда
     */
    public void setCom(Command com) {
        this.com = com;
    }

    /**
     * Получение объекта
     * @return объект
     */
    public Object getObject() {
        return object;
    }

    /**
     * Установка объекта
     * @param object объект
     */
    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.com);
        hash = 67 * hash + Objects.hashCode(this.object);
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
        final Runner other = (Runner) obj;
        if (this.com != other.com) {
            return false;
        }
        if (!Objects.equals(this.object, other.object)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Runner " + "com= " + com + " object= " + object;
    }
    
    
    
}
