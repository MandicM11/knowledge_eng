package app.Knowledge_engineering.controller;

import app.Knowledge_engineering.dto.CPUInfoDTO;
import app.Knowledge_engineering.service.CPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CPUController {

    private final CPUService cpuService;

    @Autowired
    public CPUController(CPUService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping("/api/compatibility/cpu")
    public List<CPUInfoDTO> checkCPUCompatibility(
            @RequestParam String cpuName,
            @RequestParam int cpuCores,
            @RequestParam double cpuSpeed,
            @RequestParam int cpuThreads,
            @RequestParam String motherboardModel) {

        // Kreiramo CPUInfoDTO sa podacima iz request parametara
        CPUInfoDTO cpuInfoDTO = new CPUInfoDTO();
        cpuInfoDTO.setName(cpuName);
        cpuInfoDTO.setCores(cpuCores);
        cpuInfoDTO.setSpeed(cpuSpeed);
        cpuInfoDTO.setThreads(cpuThreads);

        // Pozivamo servis da proverimo CPU-ove koji su bolji od navedenog
        List<CPUInfoDTO> betterCPUs = cpuService.getBetterCPUs(cpuInfoDTO, motherboardModel);

        // Vraćamo listu boljih CPU-ova
        return betterCPUs;
    }
}
