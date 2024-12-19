package com.lec.project.human_resources.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
	@Bean 
	public ModelMapper getModel() {
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.getConfiguration()
				   .setFieldMatchingEnabled(true)
				   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
				   .setMatchingStrategy(MatchingStrategies.LOOSE);
		
		return modelMapper;
	}
}
