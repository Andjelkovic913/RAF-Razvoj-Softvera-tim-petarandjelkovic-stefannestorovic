package org.raflab.studsluzbadesktopclient;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Configuration
public class ClientAppConfig {
	@Bean
    public RestTemplate getRestTemplate() {
       RestTemplate restTemplate = new RestTemplate();
       restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter());
       restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
       return restTemplate;
    }
    
	@Bean
    public String getBaseUrl() {
       return "http://localhost:8090";
    }

	@Bean
	public WebClient getWebClient() {
		return WebClient.builder()
				.baseUrl("http://localhost:8090")
				.build();
	}
}
