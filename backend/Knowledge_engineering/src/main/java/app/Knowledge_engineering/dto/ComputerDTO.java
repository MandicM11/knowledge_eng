package app.Knowledge_engineering.dto;

public class ComputerDTO {
    private CPUInfoDTO cpuInfo;
    private GPUInfoDTO gpuInfo;
    private MemoryModuleDTO ramInfo;

    // Konstruktor
    public ComputerDTO(CPUInfoDTO cpuInfo, GPUInfoDTO gpuInfo, MemoryModuleDTO ramInfo) {
        this.cpuInfo = cpuInfo;
        this.gpuInfo = gpuInfo;
        this.ramInfo = ramInfo;
    }

    // Getter i setter za CPUInfoDTO
    public CPUInfoDTO getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CPUInfoDTO cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    // Getter i setter za GPUInfoDTO
    public GPUInfoDTO getGpuInfo() {
        return gpuInfo;
    }

    public void setGpuInfo(GPUInfoDTO gpuInfo) {
        this.gpuInfo = gpuInfo;
    }

    // Getter i setter za MemoryModuleDTO
    public MemoryModuleDTO getRamInfo() {
        return ramInfo;
    }

    public void setRamInfo(MemoryModuleDTO ramInfo) {
        this.ramInfo = ramInfo;
    }
}
