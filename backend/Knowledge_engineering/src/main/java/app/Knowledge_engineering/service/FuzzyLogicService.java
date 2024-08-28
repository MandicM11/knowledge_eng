package app.Knowledge_engineering.service;

import net.sourceforge.jFuzzyLogic.FIS;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FuzzyLogicService {

    private final FIS fis;

    public FuzzyLogicService() {
        // Load fuzzy logic file
        String fileName = "C:/Users/Mirko/Desktop/faks novi/knowledge-engineering-master/backend/Knowledge_engineering/data/Rules.fcl";
        fis = FIS.load(fileName, true);

        if (fis == null) {
            throw new RuntimeException("Error loading FCL file.");
        }
    }

    public Map<String, Double> assess(double gpuCoreClockMhz, double gpuVideoMemoryGb, double cpuThreads,
                                      double cpuCores, double cpuClockSpeedGhz, double ramCapacityGb) {
        // Set inputs
        fis.setVariable("gpu_core_clock_mhz", gpuCoreClockMhz);
        fis.setVariable("gpu_video_memory_gb", gpuVideoMemoryGb);
        fis.setVariable("cpu_threads", cpuThreads);
        fis.setVariable("cpu_cores", cpuCores);
        fis.setVariable("cpu_clock_speed_ghz", cpuClockSpeedGhz);
        fis.setVariable("ram_capacity_gb", ramCapacityGb);

        // Evaluate
        fis.evaluate();

        // Get output values
        Map<String, Double> results = new HashMap<>();
        results.put("HomeUse", fis.getVariable("HomeUse").getValue());
        results.put("BusinessUse", fis.getVariable("BusinessUse").getValue());
        results.put("Gaming", fis.getVariable("Gaming").getValue());
        results.put("CryptocurrencyMining", fis.getVariable("CryptocurrencyMining").getValue());
        results.put("WebHosting", fis.getVariable("WebHosting").getValue());

        return results;
    }

    public String getBestUseCase(Map<String, Double> results) {
        return results.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
}
