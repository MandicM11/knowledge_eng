package app.Knowledge_engineering.dto;

import javax.persistence.criteria.CriteriaBuilder;

public class MemoryModuleDTO {
    private String name;
    private Integer capacity; // у GB
    private Integer speed; // у MHz
    private String type; // DDR4, DDR3, итд.

    public MemoryModuleDTO(String name, Integer capacity, int speed, String type) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
        this.type = type;
    }

    public MemoryModuleDTO(){

    }

    // Get и set методи
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
