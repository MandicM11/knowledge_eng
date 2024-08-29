package app.Knowledge_engineering.controller;

import app.Knowledge_engineering.dto.MemoryModuleDTO;
import app.Knowledge_engineering.service.MemoryModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MemoryModuleController {

    private final MemoryModuleService memoryModuleService;
    private static final Logger logger = LoggerFactory.getLogger(MemoryModuleController.class);
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


        logger.info("Checking compatibility with parameters: memoryType={}, memoryCapacity={}, memoryName={}, motherboardModel={}",
                memoryType, memoryCapacity, memoryName, motherboardModel);

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
