package app.Knowledge_engineering.service;

import app.Knowledge_engineering.dto.MemoryModuleDTO;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Service
public class MemoryModuleService {

    private static final Logger logger = LoggerFactory.getLogger(MemoryModuleService.class);
    private final OWLOntology ontology;
    private final OWLDataFactory factory;
    private final String ontologyIRI;

    public MemoryModuleService() throws OWLOntologyCreationException {
        this.ontologyIRI = "http://www.semanticweb.org/pc/ontologies/2023/3/untitled-ontology-4";
        this.factory = OWLManager.getOWLDataFactory();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        this.ontology = loadOntology(manager, "C:/Users/Mirko/Desktop/knowledge-engineering-master/Knowlege-engineering/backend/Knowledge_engineering/data/Knowledge_base.ttl");

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

    public boolean isCompatibleWithComponent(MemoryModuleDTO moduleDTO, String motherboardModel, String ramModel) {
        // Proverite matičnu ploču
        OWLClass motherboardClass = getClassByName("Motherboard");
        if (motherboardClass == null) {
            logger.error("Motherboard class not found in ontology");
            return false;
        }

        OWLNamedIndividual motherboardIndividual = getIndividualByModel(motherboardModel, motherboardClass);
        if (motherboardIndividual == null) {
            logger.error("Motherboard {} not found in ontology", motherboardModel);
            return false;
        }

        // Proverite RAM
        OWLClass ramClass = getClassByName("RAM");
        if (ramClass == null) {
            logger.error("RAM class not found in ontology");
            return false;
        }

        OWLNamedIndividual ramIndividual = getIndividualByModel(ramModel, ramClass);
        if (ramIndividual == null) {
            logger.error("RAM {} not found in ontology", ramModel);
            return false;
        }

        // Proverite kompatibilnost
        if (!isCompatibleWithMotherboard(motherboardIndividual, "enables_attachments_ram")) {
            logger.error("Motherboard {} does not support RAM attachments", motherboardModel);
            return false;
        }

        // Proverite karakteristike RAM-a
        String supportedMemoryType = getStringPropertyValue(ramIndividual, "ram_has_type");
        int maxMemoryCapacity = getIntegerPropertyValue(ramIndividual, "ram_has_memory");


        if (!moduleDTO.getType().equalsIgnoreCase(supportedMemoryType)) {
            logger.error("Memory type {} is not supported by the RAM module. Expected type: {}", moduleDTO.getType(), supportedMemoryType);
            return false;
        }



        if (moduleDTO.getCapacity() > maxMemoryCapacity) {
            logger.error("Memory capacity {} exceeds the maximum supported capacity {}", moduleDTO.getCapacity(), maxMemoryCapacity);
            return false;
        }

        return true;
    }



    private OWLClass getClassByName(String className) {
        return factory.getOWLClass(IRI.create(ontologyIRI + "/" + className));
    }

    private OWLNamedIndividual getIndividualByModel(String model, OWLClass componentClass) {
        // Kreiraj URI na osnovu modela
        IRI individualIRI = IRI.create(ontologyIRI + "/" + model);
        logger.info("Searching in class: {}", componentClass.getIRI());
        logger.info("Expected URI for model: {}", individualIRI);

        OWLNamedIndividual individual = ontology.individualsInSignature()
                .filter(ind -> ontology.getClassAssertionAxioms(ind).stream()
                        .anyMatch(axiom -> axiom.getClassesInSignature().contains(componentClass)))
                .filter(ind -> ind.getIRI().equals(individualIRI))
                .findFirst().orElse(null);

        if (individual == null) {
            logger.error("No individual found for model: {}", model);
        } else {
            logger.info("Found individual: {}", individual.getIRI());
        }

        return individual;
    }

    private boolean hasModel(OWLNamedIndividual individual, String model) {
        IRI hasModelPropertyIRI = IRI.create(ontologyIRI + "/" + model);
        OWLDataProperty hasModelProperty = factory.getOWLDataProperty(hasModelPropertyIRI);

        logger.info("Checking property: {}", hasModelPropertyIRI);

        return ontology.getDataPropertyAssertionAxioms(individual).stream()
                .filter(axiom -> axiom.getProperty().equals(hasModelProperty))
                .anyMatch(axiom -> axiom.getObject().getLiteral().equalsIgnoreCase(model));
    }

    private String getStringPropertyValue(OWLNamedIndividual individual, String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "/" + propertyName);
        OWLDataProperty dataProperty = factory.getOWLDataProperty(propertyIRI);

        return ontology.getDataPropertyAssertionAxioms(individual).stream()
                .filter(axiom -> axiom.getProperty().equals(dataProperty))
                .map(axiom -> axiom.getObject().getLiteral())
                .findFirst().orElse(null);
    }

    private int getIntegerPropertyValue(OWLNamedIndividual individual, String propertyName) {
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
        // Vraća -1 ako vrednost nije pronađena ili ako dođe do greške prilikom parsiranja
        return -1;
    }


    private boolean isCompatibleWithMotherboard(OWLNamedIndividual componentIndividual, String attachmentProperty) {
        IRI propertyIRI = IRI.create(ontologyIRI + "/" + attachmentProperty);
        OWLObjectProperty objectProperty = factory.getOWLObjectProperty(propertyIRI);

        return ontology.getObjectPropertyAssertionAxioms(componentIndividual).stream()
                .anyMatch(axiom -> axiom.getProperty().equals(objectProperty));
    }
}
