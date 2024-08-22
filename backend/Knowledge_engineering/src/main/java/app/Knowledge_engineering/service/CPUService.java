package app.Knowledge_engineering.service;

import app.Knowledge_engineering.dto.CPUInfoDTO;
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
public class CPUService {

    private static final Logger logger = LoggerFactory.getLogger(CPUService.class);
    private final OWLOntology ontology;
    private final OWLDataFactory factory;
    private final String ontologyIRI;

    public CPUService() throws OWLOntologyCreationException {
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

    public List<CPUInfoDTO> getBetterCPUs(CPUInfoDTO cpuInfoDTO, String motherboardModel) {
        // Proverite matičnu ploču
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

        // Proverite CPU
        OWLClass cpuClass = getClassByName("CPU");
        if (cpuClass == null) {
            logger.error("CPU class not found in ontology");
            return new ArrayList<>();
        }

        List<CPUInfoDTO> betterCPUs = new ArrayList<>();

        // Pronađi sve CPU instance u ontologiji
        ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(cpuClass)))
                .forEach(cpuIndividual -> {
                    // Proverite karakteristike CPU-a
                    Integer cpuCores = getIntegerPropertyValue(cpuIndividual, "cpu_has_cores");
                    Double cpuSpeed = getDoublePropertyValue(cpuIndividual, "cpu_has_speed");
                    Integer cpuThreads = getIntegerPropertyValue(cpuIndividual, "cpu_has_threads");

                    // Proveri da li su pronađene vrednosti validne
                    if (cpuCores != null && cpuSpeed != null && cpuThreads != null) {
                        // Proveri kompatibilnost sa trenutnim CPU
                        boolean isBetter = cpuCores > cpuInfoDTO.getCores() ||
                                //cpuSpeed >= cpuInfoDTO.getSpeed() ||
                                cpuThreads > cpuInfoDTO.getThreads();

                        if (isBetter) {
                            // Dodaj u listu bolji CPU
                            CPUInfoDTO betterCPU = new CPUInfoDTO();
                            betterCPU.setName(cpuIndividual.getIRI().getShortForm());
                            betterCPU.setCores(cpuCores);
                            betterCPU.setSpeed(cpuSpeed);
                            betterCPU.setThreads(cpuThreads);
                            betterCPUs.add(betterCPU);
                        }
                    } else {
                        logger.warn("Missing values for CPU properties. Cores: {}, Speed: {}, Threads: {}",
                                cpuCores, cpuSpeed, cpuThreads);
                    }
                });

        return betterCPUs;
    }

    private OWLClass getClassByName(String className) {
        return factory.getOWLClass(IRI.create(ontologyIRI + "/" + className));
    }

    private OWLNamedIndividual getIndividualByModel(String model, OWLClass componentClass) {
        IRI individualIRI = IRI.create(ontologyIRI + "/" + model);
        logger.info("Searching in class: {}", componentClass.getIRI());
        logger.info("Expected URI for model: {}", individualIRI);

        Optional<OWLNamedIndividual> individual = ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(componentClass)))
                .filter(ind -> ind.getIRI().equals(individualIRI))
                .findFirst();

        if (individual.isPresent()) {
            logger.error("No individual found for model: {}", model);
        } else {
            logger.info("Found individual: {}", individual.get().getIRI());
        }

        return individual.orElse(null);
    }

    private Integer getIntegerPropertyValue(OWLNamedIndividual individual, String propertyName) {
        String value = getStringPropertyValue(individual, propertyName);
        if (value != null) {
            try {
                // Logovanje dobijene vrednosti pre konverzije
                logger.info("Parsing integer value for property '{}': {}", propertyName, value);
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Logovanje greške prilikom parsiranja
                logger.error("Failed to parse integer value for property '{}': {}", propertyName, value, e);
            }
        } else {
            // Logovanje kada vrednost nije pronađena
            logger.warn("No value found for property '{}'", propertyName);
        }
        // Vraća null ako vrednost nije pronađena ili ako dođe do greške prilikom parsiranja
        return null;
    }

    private Double getDoublePropertyValue(OWLNamedIndividual individual, String propertyName) {
        String value = getStringPropertyValue(individual, propertyName);
        if (value != null) {
            try {
                // Logovanje dobijene vrednosti pre konverzije
                logger.info("Parsing double value for property '{}': {}", propertyName, value);
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                // Logovanje greške prilikom parsiranja
                logger.error("Failed to parse double value for property '{}': {}", propertyName, value, e);
            }
        } else {
            // Logovanje kada vrednost nije pronađena
            logger.warn("No value found for property '{}'", propertyName);
        }
        // Vraća null ako vrednost nije pronađena ili ako dođe do greške prilikom parsiranja
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
