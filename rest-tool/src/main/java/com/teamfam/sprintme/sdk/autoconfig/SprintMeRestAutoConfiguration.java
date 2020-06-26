package com.teamfam.sprintme.sdk.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configure the rest template.
 */
@Configuration
@ComponentScan("com.teamfam.sprintme.rest")
@ConditionalOnClass(RestTemplateBuilder.class)
public class SprintMeRestAutoConfiguration {
    
}