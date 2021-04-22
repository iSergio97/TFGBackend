package com.tfg.pmh;

import com.tfg.pmh.security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.xml.ws.Endpoint;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class PmhApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmhApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests()
					.antMatchers(HttpMethod.POST, "/habitante/login").permitAll()
					.antMatchers(HttpMethod.POST, "/sistema/administrador/login").permitAll()
					.antMatchers(HttpMethod.GET, "/habitante/**").hasRole("HABITANTE")
					.antMatchers(HttpMethod.POST, "/habitante/**").hasRole("HABITANTE")
					.antMatchers(HttpMethod.GET, "/sistema/**").hasRole("ADMINISTRADOR")
					.antMatchers(HttpMethod.POST, "/sistema/**").hasRole("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/solicitud/**").authenticated()
					.antMatchers(HttpMethod.POST, "/solicitud/**").authenticated()
					.antMatchers(HttpMethod.GET, "/**").authenticated()
					.antMatchers(HttpMethod.POST, "/**").authenticated()
					.antMatchers(HttpMethod.GET, "/").permitAll()
					.and().cors().and()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests().anyRequest().authenticated();
			/*
					.antMatchers(HttpMethod.GET, "/habitante/**").hasRole("HABITANTE")
					.antMatchers(HttpMethod.POST, "/habitante/**").hasRole("HABITANTE")
					.antMatchers(HttpMethod.GET, "/sistema/**").hasRole("ADMINISTRADOR")
					.antMatchers(HttpMethod.POST, "/sistema/**").hasRole("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/solicitud/**").permitAll()
			 */
		}

		@Bean
		CorsConfigurationSource corsConfigurationSource() {
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081")); // Añadir origen de netlify
			configuration.setAllowedHeaders(Collections.singletonList("*")); // Headers permitidos
			configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE")); // Métodos permitidos
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	}
}
