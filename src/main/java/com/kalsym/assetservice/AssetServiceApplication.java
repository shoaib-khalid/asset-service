package com.kalsym.assetservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

// import com.kalsym.assetservice.repository.CustomRepositoryImpl;

import org.springframework.http.converter.HttpMessageConverter;
import java.awt.image.BufferedImage;
import java.io.File;

import org.springframework.http.converter.BufferedImageHttpMessageConverter;


@SpringBootApplication
// @EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)

public class AssetServiceApplication {

	public static String VERSION;

    static {
        System.setProperty("spring.jpa.hibernate.naming.physical-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
    }

	public static void main(String[] args) {
		SpringApplication.run(AssetServiceApplication.class, args);
		System.out.println("ASSET SERVICE IS RUNNING ::::::::::::::::::::::::::::::");

        // File directoryPath = new File("C:/Users/Nur Iman/Desktop/Iman New/file-listing");

        // //List of all files and directories
        // String contents[] = directoryPath.list();
        // System.out.println("List of files and directories in the specified directory:");
        // for(int i=0; i<contents.length; i++) {
        //     System.out.println(contents[i]);
        // }

	}

	@Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}
