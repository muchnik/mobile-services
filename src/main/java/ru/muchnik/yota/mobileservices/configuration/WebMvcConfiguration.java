package ru.muchnik.yota.mobileservices.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Common configuration for web mvc
 *
 * @apiNote needed to exclude autoconfiguration in test environment
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration {
}
