package app.Knowledge_engineering.controller;
import org.springframework.http.ResponseEntity;
import app.Knowledge_engineering.dto.CPUInfoDTO;
import app.Knowledge_engineering.dto.GPUInfoDTO;
import app.Knowledge_engineering.dto.MemoryModuleDTO;
import app.Knowledge_engineering.dto.SimilarityResult;
import app.Knowledge_engineering.service.SimilarityService;
import org.springframework.web.bind.annotation.*;
import app.Knowledge_engineering.dto.ComputerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/findMostSimilarComputer")
public class SimilarityController {

    private final SimilarityService similarityService;

    public SimilarityController(SimilarityService similarityService) {
        this.similarityService = similarityService;
    }

    @GetMapping
    public ResponseEntity<ComputerDTO> findMostSimilarComputer(
            @RequestParam String cpuName,
            @RequestParam Integer cpuCores,
            @RequestParam Double cpuSpeed,
            @RequestParam Integer cpuThreads,
            @RequestParam String gpuName,
            @RequestParam Integer gpuTdp,
            @RequestParam Integer gpuMemory,
            @RequestParam Double gpuSpeed,
            @RequestParam String ramName,
            @RequestParam Integer ramCapacity,
            @RequestParam Integer ramSpeed,
            @RequestParam String ramType) {

        CPUInfoDTO cpuInfo = new CPUInfoDTO(cpuName, cpuCores, cpuSpeed, cpuThreads);
        GPUInfoDTO gpuInfo = new GPUInfoDTO(gpuName, gpuTdp, gpuSpeed, gpuMemory);
        MemoryModuleDTO ramInfo = new MemoryModuleDTO(ramName, ramCapacity, ramSpeed, ramType);

        ComputerDTO targetComputer = new ComputerDTO(cpuInfo, gpuInfo, ramInfo);

        ComputerDTO mostSimilarComputer = similarityService.findMostSimilarComputer(targetComputer);

        return ResponseEntity.ok(mostSimilarComputer);
    }
}

