package ru.muchnik.yota.mobileservices.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Common configuration for aspectj
 * @apiNote needed to exclude autoconfiguration in test environment
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectJConfiguration {
}
