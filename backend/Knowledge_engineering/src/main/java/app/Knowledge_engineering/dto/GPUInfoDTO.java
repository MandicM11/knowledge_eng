package app.Knowledge_engineering.dto;

public class GPUInfoDTO {

    private String name;
    private Integer tdp;
    private Double speed;
    private Integer memory;

    public GPUInfoDTO() {
    }

    public GPUInfoDTO(String name, Integer tdp, Double speed, Integer memory) {
        this.name = name;
        this.tdp = tdp;
        this.speed = speed;
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTDP() {
        return tdp;
    }

    public void setTDP(Integer tdp) {
        this.tdp = tdp;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "GPUInfoDTO{" +
                "name='" + name + '\'' +
                ", TDP=" + tdp +
                ", speed=" + speed +
                ", memory=" + memory +
                '}';
    }
}
