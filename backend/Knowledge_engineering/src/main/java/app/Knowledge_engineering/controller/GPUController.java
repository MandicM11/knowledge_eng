package app.Knowledge_engineering.controller;

import app.Knowledge_engineering.dto.GPUInfoDTO;
import app.Knowledge_engineering.service.GPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GPUController {

    private final GPUService gpuService;

    @Autowired
    public GPUController(GPUService gpuService) {
        this.gpuService = gpuService;
    }

    @GetMapping("/api/compatibility/gpu")
    public List<GPUInfoDTO> checkGPUCompatibility(
            @RequestParam String gpuName,
            @RequestParam int gpuTDP,
            @RequestParam double gpuSpeed,
            @RequestParam int gpuMemory,
            @RequestParam String motherboardModel) {

        // Kreiramo GPUInfoDTO sa podacima iz request parametara
        GPUInfoDTO gpuInfoDTO = new GPUInfoDTO();
        gpuInfoDTO.setName(gpuName);
        gpuInfoDTO.setTDP(gpuTDP);
        gpuInfoDTO.setSpeed(gpuSpeed);
        gpuInfoDTO.setMemory(gpuMemory);

        // Pozivamo servis da proverimo GPU-ove koji su bolji od navedenog
        List<GPUInfoDTO> betterGPUs = gpuService.getBetterGPUs(gpuInfoDTO, motherboardModel);

        // Vraćamo listu boljih GPU-ova
        return betterGPUs;
    }
}
