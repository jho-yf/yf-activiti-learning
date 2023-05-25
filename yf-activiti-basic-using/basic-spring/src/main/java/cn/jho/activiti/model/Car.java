package cn.jho.activiti.model;

import cn.jho.activiti.variable.Json;
import java.util.Objects;

/**
 * <p>Car</p>
 *
 * @author JHO xu-jihong@qq.com
 */
@Json
public class Car {

    private String id;

    private String name;

    public Car() {
    }

    public Car(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
