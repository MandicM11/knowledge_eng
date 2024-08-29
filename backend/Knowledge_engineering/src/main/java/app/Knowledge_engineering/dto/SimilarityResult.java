package app.Knowledge_engineering.dto;

import java.util.List;

public class SimilarityResult {
    private List<CPUInfoDTO> similarCPUs;
    private List<GPUInfoDTO> similarGPUs;
    private List<MemoryModuleDTO> similarRAMs;

    public SimilarityResult() {
        // Default constructor
    }

    public SimilarityResult(List<CPUInfoDTO> similarCPUs, List<GPUInfoDTO> similarGPUs, List<MemoryModuleDTO> similarRAMs) {
        this.similarCPUs = similarCPUs;
        this.similarGPUs = similarGPUs;
        this.similarRAMs = similarRAMs;
    }

    // Getters and Setters

    public List<CPUInfoDTO> getSimilarCPUs() {
        return similarCPUs;
    }

    public void setSimilarCPUs(List<CPUInfoDTO> similarCPUs) {
        this.similarCPUs = similarCPUs;
    }

    public List<GPUInfoDTO> getSimilarGPUs() {
        return similarGPUs;
    }

    public void setSimilarGPUs(List<GPUInfoDTO> similarGPUs) {
        this.similarGPUs = similarGPUs;
    }

    public List<MemoryModuleDTO> getSimilarRAMs() {
        return similarRAMs;
    }

    public void setSimilarRAMs(List<MemoryModuleDTO> similarRAMs) {
        this.similarRAMs = similarRAMs;
    }
}
