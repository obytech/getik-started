package com.tikal.gettingstarted.config.swagger;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tikal.gettingstarted.controllers.SwagMe;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket employeeApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.host("tikalk.com:8080")
				.protocols(Collections.singleton("http"))
				.select()
				.apis(handler -> handler.findControllerAnnotation(SwagMe.class).isPresent())
				.paths(PathSelectors.regex("/Employee.*"))
				.build()
				.apiInfo(getMetadata());
	}

	private ApiInfo getMetadata() {
		ApiInfo apiInfo = new ApiInfo("Getting started: SpringBoot and more", 
				"Get to know spring boot, actuator, mongo and Swagger", 
				"0.0.1", 
				"termsOfService", 
				new Contact("Oren", "http://www.tikalk.com", "orenb@tikalk.com"), 
				"Free to use", 
				"licenseUrl", 
				new ArrayList<>());

		return apiInfo;
	}
}