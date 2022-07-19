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
	static
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/**/login").permitAll()
					//.antMatchers(HttpMethod.POST, "/sistema/administrador/poblate").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/habitante/**").hasAuthority("HABITANTE")
					.antMatchers(HttpMethod.POST, "/habitante/**").hasAuthority("HABITANTE")
					.antMatchers(HttpMethod.POST, "/perfil/**").hasAuthority("HABITANTE")
					.antMatchers(HttpMethod.GET, "/estadisticas/**").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/operaciones/**").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.POST, "/operaciones/**").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/sistema/**").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.POST, "/sistema/**").hasAuthority("ADMINISTRADOR")
					.antMatchers(HttpMethod.GET, "/solicitud/**").authenticated()
					.antMatchers(HttpMethod.POST, "/solicitud/**").authenticated();
		}

		@Bean
		CorsConfigurationSource corsConfigurationSource() {
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(Collections.singletonList("*")); // Añadir origen de netlify
			configuration.setAllowedHeaders(Collections.singletonList("*")); // Headers permitidos
			configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE")); // Métodos permitidos
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	}
}
