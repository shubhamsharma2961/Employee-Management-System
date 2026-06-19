package com.company.ems.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "AuditorAwareImpl")
public class JpaConfig {
	
	@Bean
    public AuditorAware<UUID> AuditorAwareImpl() {
        return new AuditorAwareImpl();
    }

}
