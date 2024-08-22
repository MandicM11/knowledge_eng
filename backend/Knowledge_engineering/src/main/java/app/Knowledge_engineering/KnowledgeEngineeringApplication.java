package app.Knowledge_engineering;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

@SpringBootApplication
// (exclude = {DataSourceAutoConfiguration.class })
public class KnowledgeEngineeringApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	@Bean
	public ObjectMapper objectMapper() { return new ObjectMapper();}

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeEngineeringApplication.class, args);
	}




}
