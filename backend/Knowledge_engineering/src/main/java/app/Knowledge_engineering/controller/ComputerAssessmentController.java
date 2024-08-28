package app.Knowledge_engineering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import app.Knowledge_engineering.dto.ComputerAssessmentDTO;
import app.Knowledge_engineering.service.FuzzyLogicService;
import java.util.HashMap;
import java.util.Map;



@RestController
public class ComputerAssessmentController {

    private final FuzzyLogicService fuzzyLogicService;

    @Autowired
    public ComputerAssessmentController(FuzzyLogicService fuzzyLogicService) {
        this.fuzzyLogicService = fuzzyLogicService;
    }

    @GetMapping("/assess")
    public Map<String, Object> assess(
            @RequestParam double gpuCoreClockMhz,
            @RequestParam double gpuVideoMemoryGb,
            @RequestParam double cpuThreads,
            @RequestParam double cpuCores,
            @RequestParam double cpuClockSpeedGhz,
            @RequestParam double ramCapacityGb) {

        // Call assess method
        Map<String, Double> results = fuzzyLogicService.assess(
                gpuCoreClockMhz, gpuVideoMemoryGb, cpuThreads, cpuCores, cpuClockSpeedGhz, ramCapacityGb);

        // Call getBestUseCase method
        String bestUseCase = fuzzyLogicService.getBestUseCase(results);

        // Return the results including the best use case
        Map<String, Object> response = new HashMap<>(results);
        response.put("BestUseCase", bestUseCase); // Adding the best use case as a result
        return response;
    }
}
