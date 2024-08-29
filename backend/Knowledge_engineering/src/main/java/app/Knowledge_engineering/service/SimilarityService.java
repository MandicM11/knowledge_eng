package app.Knowledge_engineering.service;

import app.Knowledge_engineering.dto.CPUInfoDTO;
import app.Knowledge_engineering.dto.ComputerDTO;
import app.Knowledge_engineering.dto.GPUInfoDTO;
import app.Knowledge_engineering.dto.MemoryModuleDTO;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
public class SimilarityService {

    private static final Logger logger = LoggerFactory.getLogger(SimilarityService.class);
    private final OWLOntology ontology;
    private final OWLDataFactory factory;
    private final String ontologyIRI;

    public SimilarityService() throws OWLOntologyCreationException {
        this.ontologyIRI = "http://www.semanticweb.org/pc/ontologies/2023/3/untitled-ontology-4";
        this.factory = OWLManager.getOWLDataFactory();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        this.ontology = loadOntology(manager, "C:/Users/Mirko/Desktop/faks novi/knowledge-engineering-master/backend/Knowledge_engineering/data/Knowledge_base.ttl");

        logger.info("Ontology loaded from file");
    }

    private OWLOntology loadOntology(OWLOntologyManager manager, String ontologyFilePath) throws OWLOntologyCreationException {
        logger.info("Loading ontology from file: {}", ontologyFilePath);
        File file = new File(ontologyFilePath);
        return manager.loadOntologyFromOntologyDocument(file);
    }

    public ComputerDTO findMostSimilarComputer(ComputerDTO targetComputer) {
        List<ComputerDTO> allComputers = getAllComputersFromOntology();

        ComputerDTO mostSimilarComputer = null;
        double highestSimilarity = 0.0;

        for (ComputerDTO otherComputer : allComputers) {
            double similarity = calculateComputerSimilarity(targetComputer, otherComputer);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarComputer = otherComputer;
            }
        }

        return mostSimilarComputer;
    }

    private double calculateComputerSimilarity(ComputerDTO computer1, ComputerDTO computer2) {
        if (computer1 == null || computer2 == null) {
            logger.error("One of the computers is null: computer1={}, computer2={}", computer1, computer2);
            return 0.0; // Možete prilagoditi ovaj povratni rezultat prema vašim potrebama
        }

        double cpuSimilarity = calculateCPUInfoSimilarity(computer1.getCpuInfo(), computer2.getCpuInfo());
        double gpuSimilarity = calculateGPUInfoSimilarity(computer1.getGpuInfo(), computer2.getGpuInfo());
        double ramSimilarity = calculateRAMInfoSimilarity(computer1.getRamInfo(), computer2.getRamInfo());

        return (cpuSimilarity + gpuSimilarity + ramSimilarity) / 3.0;
    }


    private double calculateCPUInfoSimilarity(CPUInfoDTO cpu1, CPUInfoDTO cpu2) {
        if (cpu2 != null) {
            int cores = cpu2.getCores();
            // Dalji kod
        } else {
            // Postavite odgovarajući odgovor ili izvršite neki drugi deo koda
            System.out.println("cpu2 je null");
        }

        double coreSimilarity = Math.abs(cpu1.getCores() - cpu2.getCores()) / (double) Math.max(cpu1.getCores(), cpu2.getCores());
        double speedSimilarity = Math.abs(cpu1.getSpeed() - cpu2.getSpeed()) / Math.max(cpu1.getSpeed(), cpu2.getSpeed());
        double threadSimilarity = Math.abs(cpu1.getThreads() - cpu2.getThreads()) / (double) Math.max(cpu1.getThreads(), cpu2.getThreads());

        return 1.0 - (coreSimilarity + speedSimilarity + threadSimilarity) / 3.0;
    }

    private double calculateGPUInfoSimilarity(GPUInfoDTO gpu1, GPUInfoDTO gpu2) {
        double memorySimilarity = Math.abs(gpu1.getMemory() - gpu2.getMemory()) / Math.max(gpu1.getMemory(), gpu2.getMemory());
        double speedSimilarity = Math.abs(gpu1.getSpeed() - gpu2.getSpeed()) / Math.max(gpu1.getSpeed(), gpu2.getSpeed());

        return 1.0 - (memorySimilarity + speedSimilarity) / 2.0;
    }

    private double calculateRAMInfoSimilarity(MemoryModuleDTO ram1, MemoryModuleDTO ram2) {
        // Proveri da li su RAM moduli istog tipa
        if (!ram1.getType().equals(ram2.getType())) {
            return 0.0;
        }

        // Proveri da li je kapacitet ram2 u prihvatljivom opsegu u odnosu na ram1
        if (ram2.getCapacity() < ram1.getCapacity() / 2 || ram2.getCapacity() > ram1.getCapacity() * 2) {
            return 0.0;
        }

        // Izračunaj sličnost kapaciteta
        double sizeSimilarity = Math.abs(ram1.getCapacity() - ram2.getCapacity()) / (double) Math.max(ram1.getCapacity(), ram2.getCapacity());

        return 1.0 - sizeSimilarity;
    }

    private List<ComputerDTO> getAllComputersFromOntology() {
        List<ComputerDTO> computers = new ArrayList<>();

        ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(getClassByName("PC"))))
                .forEach(ind -> {
                    CPUInfoDTO cpu = getCPUFromPC(ind);
                    GPUInfoDTO gpu = getGPUFromPC(ind);
                    MemoryModuleDTO ram = getRAMFromPC(ind);

                    computers.add(new ComputerDTO(cpu, gpu, ram));
                });

        return computers;
    }

    private CPUInfoDTO getCPUFromPC(OWLNamedIndividual pc) {
        OWLNamedIndividual cpuIndividual = getRelatedIndividual(pc, "has_CPU");
        if (cpuIndividual == null) return null;
        String name = getStringPropertyValue(cpuIndividual,"cpu_has_name");
        Integer cores = getIntegerPropertyValue(cpuIndividual, "cpu_has_cores");
        Double speed = getDoublePropertyValue(cpuIndividual, "cpu_has_speed");
        Integer threads = getIntegerPropertyValue(cpuIndividual, "cpu_has_threads");

        return new CPUInfoDTO(name,cores, speed, threads);
    }

    private GPUInfoDTO getGPUFromPC(OWLNamedIndividual pc) {
        OWLNamedIndividual gpuIndividual = getRelatedIndividual(pc, "has_GPU");
        if (gpuIndividual == null) return null;

        String name = getStringPropertyValue(gpuIndividual, "gpu_has_name");
        Integer tdp = getIntegerPropertyValue(gpuIndividual, "gpu_has_tdp");
        Integer memory = getIntegerPropertyValue(gpuIndividual, "gpu_has_video_memory");
        Double speed = getDoublePropertyValue(gpuIndividual, "gpu_has_speed");

        return new GPUInfoDTO(name,tdp,speed,memory);
    }

    private MemoryModuleDTO getRAMFromPC(OWLNamedIndividual pc) {
        OWLNamedIndividual ramIndividual = getRelatedIndividual(pc, "has_RAM");
        if (ramIndividual == null) return null;
        String name = getStringPropertyValue(ramIndividual, "ram_has_name");
        Integer speed = getIntegerPropertyValue(ramIndividual, "ram_has_latency");
        Integer capacity = getIntegerPropertyValue(ramIndividual, "ram_has_memory");
        String type = getStringPropertyValue(ramIndividual, "ram_has_type");

        return new MemoryModuleDTO(name,capacity,speed,type);
    }

    private OWLNamedIndividual getIndividualByName(String individualName) {
        IRI individualIRI = IRI.create(ontologyIRI + "/" + individualName);
        return ontology.individualsInSignature()
                .filter(ind -> ind.getIRI().equals(individualIRI))
                .findFirst().orElse(null);
    }

    private OWLNamedIndividual getRelatedIndividual(OWLNamedIndividual individual, String objectPropertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "/" + objectPropertyName);
        OWLObjectProperty objectProperty = factory.getOWLObjectProperty(propertyIRI);

        // Koristimo tradicionalnu for petlju za iteraciju kroz axiome
        Set<OWLObjectPropertyAssertionAxiom> axioms = ontology.objectPropertyAssertionAxioms(individual).collect(Collectors.toSet());

        for (OWLObjectPropertyAssertionAxiom axiom : axioms) {
            if (axiom.getProperty().equals(objectProperty)) {
                OWLIndividual relatedIndividual = axiom.getObject();
                if (relatedIndividual instanceof OWLNamedIndividual) {
                    return (OWLNamedIndividual) relatedIndividual;
                }
            }
        }

        return null;
    }





    private Integer getIntegerPropertyValue(OWLNamedIndividual individual, String propertyName) {
        String value = getStringPropertyValue(individual, propertyName);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Failed to parse integer value for property '{}': {}", propertyName, value, e);
            }
        }
        return null;
    }

    private Double getDoublePropertyValue(OWLNamedIndividual individual, String propertyName) {
        String value = getStringPropertyValue(individual, propertyName);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logger.error("Failed to parse double value for property '{}': {}", propertyName, value, e);
            }
        }
        return null;
    }

    private String getStringPropertyValue(OWLNamedIndividual individual, String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "/" + propertyName);
        OWLDataProperty dataProperty = factory.getOWLDataProperty(propertyIRI);

        return ontology.getDataPropertyAssertionAxioms(individual).stream()
                .filter(axiom -> axiom.getProperty().equals(dataProperty))
                .map(axiom -> axiom.getObject().getLiteral())
                .findFirst().orElse(null);
    }

    private OWLClass getClassByName(String className) {
        IRI classIRI = IRI.create(ontologyIRI + "/" + className);
        return factory.getOWLClass(classIRI);
    }
}
