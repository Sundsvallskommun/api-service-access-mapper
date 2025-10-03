package se.sundsvall.accessmapper.integration.activedirectory.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration.active-directory")
public record ActiveDirectoryProperties(int connectTimeout, int readTimeout) {
}
