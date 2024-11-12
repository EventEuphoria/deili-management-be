package com.deili.deilimanagement;

import com.deili.deilimanagement.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableConfigurationProperties({
		RsaKeyProperties.class,
})
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class DeilimanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeilimanagementApplication.class, args);
	}
}
