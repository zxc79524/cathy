package idv.blake.cathy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import idv.blake.cathy.lib.restful_engine.RestfulEngine;
import idv.blake.cathy.lib.restful_engine.RestfulEngine.RunnerType;

@SpringBootApplication
public class CathyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CathyApplication.class, args);
		RestfulEngine.init(RunnerType.JavaSync, null);
	}

}
