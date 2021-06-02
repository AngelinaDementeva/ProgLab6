package data;

import java.io.Serializable;

/**
 * Координаты
 * @author Admin
 */
public class Coordinates  implements Serializable{
    private int x;
    private int y; //значение больше чем -425

    /**
     * Констаруктор с параметрами
     * @param x координата x
     * @param y координата y
     */
    public Coordinates(int x, int y) {
        if(y < -425) throw new IllegalArgumentException("Значение 'y' должно быть больше -425. Заданное: " + y);
        this.x = x;
        this.y = y;
    }

    /**
     * Получение координаты x
     * @return координата х
     */
    public int getX() {
        return x;
    }

    /**
     * Установка координаты х
     * @param x координата х
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Получение координаты y
     * @return координата y
     */
    public int getY() {
        return y;
    }

    /**
     * Установка координаты y
     * @param y координата y
     */
    public void setY(int y) {
        if(y < -425) return;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
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
        final Coordinates other = (Coordinates) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }
    
}
