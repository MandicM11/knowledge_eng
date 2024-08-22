package app.Knowledge_engineering.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class GlobalString {
    public static String baseIRI;

    @Value("${myapp.global-string}")
    public void setGlobalString(String globalString) {
        baseIRI = "http://www.semanticweb.org/pc/ontologies/2023/3/untitled-ontology-4";
    }
}