package com.rsc.bhopal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.rsc.bhopal.service.ApplicationConstantService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TicktingSoftwareRscbApplication {

	@Autowired
	private ApplicationConstantService applicationConstantService;

	public static void main(String[] args) {
		SpringApplication.run(TicktingSoftwareRscbApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return runner -> {
			log.debug("Ticket serial: " + applicationConstantService.getTicketSerial());
			// log.debug("Print ticket fields coordinates: " + applicationConstantService.getAllCurrentPrintCoordinate());
		};
	}
}
