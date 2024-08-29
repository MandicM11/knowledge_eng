package app.Knowledge_engineering.dto;

public class SimilarityRequest {
    private CPUInfoDTO cpuInfo;
    private GPUInfoDTO gpuInfo;
    private MemoryModuleDTO ramInfo;
    private double cpuSimilarityThreshold;
    private double gpuSimilarityThreshold;
    private double ramSimilarityThreshold;

    // Getters and Setters

    public CPUInfoDTO getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CPUInfoDTO cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public GPUInfoDTO getGpuInfo() {
        return gpuInfo;
    }

    public void setGpuInfo(GPUInfoDTO gpuInfo) {
        this.gpuInfo = gpuInfo;
    }

    public MemoryModuleDTO getRamInfo() {
        return ramInfo;
    }

    public void setRamInfo(MemoryModuleDTO ramInfo) {
        this.ramInfo = ramInfo;
    }

    public double getCpuSimilarityThreshold() {
        return cpuSimilarityThreshold;
    }

    public void setCpuSimilarityThreshold(double cpuSimilarityThreshold) {
        this.cpuSimilarityThreshold = cpuSimilarityThreshold;
    }

    public double getGpuSimilarityThreshold() {
        return gpuSimilarityThreshold;
    }

    public void setGpuSimilarityThreshold(double gpuSimilarityThreshold) {
        this.gpuSimilarityThreshold = gpuSimilarityThreshold;
    }

    public double getRamSimilarityThreshold() {
        return ramSimilarityThreshold;
    }

    public void setRamSimilarityThreshold(double ramSimilarityThreshold) {
        this.ramSimilarityThreshold = ramSimilarityThreshold;
    }
}
