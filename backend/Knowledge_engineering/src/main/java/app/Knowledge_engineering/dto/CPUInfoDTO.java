package app.Knowledge_engineering.dto;

public class CPUInfoDTO {
    private String name;
    private int cores;
    private double speed;
    private int threads;

    // Getter i setter za 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter i setter za 'cores'
    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    // Getter i setter za 'speed'
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Getter i setter za 'threads'
    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
