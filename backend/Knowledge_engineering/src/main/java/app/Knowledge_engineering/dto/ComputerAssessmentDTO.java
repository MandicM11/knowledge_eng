package app.Knowledge_engineering.dto;

public class ComputerAssessmentDTO {
    private double gpuCoreClockMhz;
    private double gpuVideoMemoryGb;
    private double cpuThreads;
    private double cpuCores;
    private double cpuClockSpeedGhz;
    private double ramCapacityGb;

    // Getter and Setter for gpuCoreClockMhz
    public double getGpuCoreClockMhz() {
        return gpuCoreClockMhz;
    }

    public void setGpuCoreClockMhz(double gpuCoreClockMhz) {
        this.gpuCoreClockMhz = gpuCoreClockMhz;
    }

    // Getter and Setter for gpuVideoMemoryGb
    public double getGpuVideoMemoryGb() {
        return gpuVideoMemoryGb;
    }

    public void setGpuVideoMemoryGb(double gpuVideoMemoryGb) {
        this.gpuVideoMemoryGb = gpuVideoMemoryGb;
    }

    // Getter and Setter for cpuThreads
    public double getCpuThreads() {
        return cpuThreads;
    }

    public void setCpuThreads(double cpuThreads) {
        this.cpuThreads = cpuThreads;
    }

    // Getter and Setter for cpuCores
    public double getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(double cpuCores) {
        this.cpuCores = cpuCores;
    }

    // Getter and Setter for cpuClockSpeedGhz
    public double getCpuClockSpeedGhz() {
        return cpuClockSpeedGhz;
    }

    public void setCpuClockSpeedGhz(double cpuClockSpeedGhz) {
        this.cpuClockSpeedGhz = cpuClockSpeedGhz;
    }

    // Getter and Setter for ramCapacityGb
    public double getRamCapacityGb() {
        return ramCapacityGb;
    }

    public void setRamCapacityGb(double ramCapacityGb) {
        this.ramCapacityGb = ramCapacityGb;
    }
}
