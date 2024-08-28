package app.Knowledge_engineering.service;

import app.Knowledge_engineering.dto.GPUInfoDTO;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GPUService {

    private static final Logger logger = LoggerFactory.getLogger(GPUService.class);
    private final OWLOntology ontology;
    private final OWLDataFactory factory;
    private final String ontologyIRI;

    public GPUService() throws OWLOntologyCreationException {
        this.ontologyIRI = "http://www.semanticweb.org/pc/ontologies/2023/3/untitled-ontology-4";
        this.factory = OWLManager.getOWLDataFactory();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        this.ontology = loadOntology(manager, "C:/Users/Mirko/Desktop/faks novi/knowledge-engineering-master/backend/Knowledge_engineering/data/Knowledge_base.ttl");

        // Ispiši sve klase
        logger.info("Classes in ontology:");
        ontology.classesInSignature().forEach(cls -> logger.info("Class: {}", cls.getIRI()));

        // Ispiši sve instance
        logger.info("Individuals in ontology:");
        ontology.individualsInSignature().forEach(ind -> logger.info("Individual: {}", ind.getIRI()));
    }

    private OWLOntology loadOntology(OWLOntologyManager manager, String ontologyFilePath) throws OWLOntologyCreationException {
        logger.info("Loading ontology from file: {}", ontologyFilePath);
        File file = new File(ontologyFilePath);
        return manager.loadOntologyFromOntologyDocument(file);
    }

    public List<GPUInfoDTO> getBetterGPUs(GPUInfoDTO gpuInfoDTO, String motherboardModel) {
        OWLClass motherboardClass = getClassByName("Motherboard");
        if (motherboardClass == null) {
            logger.error("Motherboard class not found in ontology");
            return new ArrayList<>();
        }

        OWLNamedIndividual motherboardIndividual = getIndividualByModel(motherboardModel, motherboardClass);
        if (motherboardIndividual == null) {
            logger.error("Motherboard {} not found in ontology", motherboardModel);
            return new ArrayList<>();
        }

        OWLClass gpuClass = getClassByName("GPU");
        if (gpuClass == null) {
            logger.error("GPU class not found in ontology");
            return new ArrayList<>();
        }

        List<GPUInfoDTO> betterGPUs = new ArrayList<>();
        ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(gpuClass)))
                .forEach(gpuIndividual -> {
                    Integer gpuTDP = getIntegerPropertyValue(gpuIndividual, "gpu_has_tdp");
                    Double gpuSpeed = getDoublePropertyValue(gpuIndividual, "gpu_has_speed");
                    Integer gpuMemory = getIntegerPropertyValue(gpuIndividual, "gpu_has_memory");

                    if (gpuTDP != null && gpuSpeed != null && gpuMemory != null) {
                        boolean isBetter = gpuTDP > gpuInfoDTO.getTDP() ||
                                gpuSpeed > gpuInfoDTO.getSpeed() ||
                                gpuMemory > gpuInfoDTO.getMemory();

                        if (isBetter) {
                            GPUInfoDTO betterGPU = new GPUInfoDTO();
                            betterGPU.setName(gpuIndividual.getIRI().getShortForm());
                            betterGPU.setTDP(gpuTDP);
                            betterGPU.setSpeed(gpuSpeed);
                            betterGPU.setMemory(gpuMemory);
                            betterGPUs.add(betterGPU);
                        }
                    } else {
                        logger.warn("Missing values for GPU properties. TDP: {}, Speed: {}, Memory: {}",
                                gpuTDP, gpuSpeed, gpuMemory);
                    }
                });

        return betterGPUs;
    }

    private OWLClass getClassByName(String className) {
        return factory.getOWLClass(IRI.create(ontologyIRI + "/" + className));
    }

    private OWLNamedIndividual getIndividualByModel(String model, OWLClass componentClass) {
        IRI individualIRI = IRI.create(ontologyIRI + "/" + model);
        logger.info("Searching in class: {}", componentClass.getIRI());
        logger.info("Expected IRI for model: {}", individualIRI);

        Optional<OWLNamedIndividual> individual = ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(componentClass)))
                .filter(ind -> ind.getIRI().equals(individualIRI))
                .findFirst();

        if (!individual.isPresent()) {
            logger.error("No individual found for model: {}", model);
        } else {
            logger.info("Found individual: {}", individual.get().getIRI());
        }

        return individual.orElse(null);
    }

    private Integer getIntegerPropertyValue(OWLNamedIndividual individual, String propertyName) {
        return getPropertyValue(individual, propertyName, Integer::parseInt);
    }

    private Double getDoublePropertyValue(OWLNamedIndividual individual, String propertyName) {
        return getPropertyValue(individual, propertyName, Double::parseDouble);
    }

    private <T> T getPropertyValue(OWLNamedIndividual individual, String propertyName, java.util.function.Function<String, T> parser) {
        String value = getStringPropertyValue(individual, propertyName);
        if (value != null) {
            try {
                logger.info("Parsing value for property '{}': {}", propertyName, value);
                return parser.apply(value);
            } catch (NumberFormatException e) {
                logger.error("Failed to parse value for property '{}': {}", propertyName, value, e);
            }
        } else {
            logger.warn("No value found for property '{}'", propertyName);
        }
        return null;
    }

    private String getStringPropertyValue(OWLNamedIndividual individual, String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "/" + propertyName);
        OWLDataProperty dataProperty = factory.getOWLDataProperty(propertyIRI);

        logger.info("Checking data property: {}", propertyIRI);

        return ontology.getDataPropertyAssertionAxioms(individual).stream()
                .filter(axiom -> axiom.getProperty().equals(dataProperty))
                .map(axiom -> axiom.getObject().getLiteral())
                .findFirst().orElse(null);
    }
}
