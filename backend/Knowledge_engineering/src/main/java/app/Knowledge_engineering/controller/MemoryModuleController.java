package app.Knowledge_engineering.controller;

import app.Knowledge_engineering.dto.MemoryModuleDTO;
import app.Knowledge_engineering.service.MemoryModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryModuleController {

    private final MemoryModuleService memoryModuleService;

    @Autowired
    public MemoryModuleController(MemoryModuleService memoryModuleService) {
        this.memoryModuleService = memoryModuleService;
    }

    @GetMapping("/api/compatibility/memory")
    public String checkMemoryCompatibility(
            @RequestParam String memoryType,
            @RequestParam int memoryCapacity,
            @RequestParam String memoryName,
            @RequestParam String motherboardModel) {

        // Kreiramo MemoryModuleDTO sa podacima iz request parametara
        MemoryModuleDTO memoryModuleDTO = new MemoryModuleDTO();
        memoryModuleDTO.setType(memoryType);
        memoryModuleDTO.setCapacity(memoryCapacity);
        memoryModuleDTO.setName(memoryName);

        // Pozivamo servis da proverimo kompatibilnost sa matičnom pločom
        boolean isCompatible = memoryModuleService.isCompatibleWithComponent(memoryModuleDTO, motherboardModel, memoryName);

        // Vraćamo odgovor zasnovan na rezultatu
        return isCompatible ?
                "Memory module of type " + memoryType + " is compatible with motherboard " + motherboardModel :
                "Memory module of type " + memoryType + " is not compatible with motherboard " + motherboardModel;
    }
}
