package com.beautyline.polimi.configuration.security;

import com.beautyline.polimi.configuration.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfiguration {
	private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
		new AntPathRequestMatcher("/consumer/**"),
		new AntPathRequestMatcher("/admin/**")
	);

	@Bean
	public AuthenticationManager authenticationManager(TokenAuthenticationProvider authenticationProvider) {
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, TokenAuthenticationProvider authenticationProvider, AuthenticationManager authenticationManager) throws Exception {
		return http
			.cors() // enable CORS
			.and()
			.sessionManagement().sessionCreationPolicy(STATELESS) // disable session cookies bc of token JWT
			.and()
			.exceptionHandling() // enable exception handling
			.defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), PROTECTED_URLS)
			// path list that requires the authenticator validation
			.and()
			.authenticationProvider(authenticationProvider) // authenticator object
			.addFilterBefore(restAuthenticationFilter(authenticationManager), AnonymousAuthenticationFilter.class)
			// if not authenticated return this (1st parameter)
			.authorizeRequests() // require validation for following paths
			.antMatchers("/consumer/**", "/admin/**")
			.authenticated() // require authentication for them ^
			.and()
			.csrf().disable() // disable iFrame HTML
			.formLogin().disable() // disable default spring login
			.httpBasic().disable() // disable default spring pages
			.logout().disable() // disable default spring logout methods
			.build();
	}

	TokenAuthenticationFilter restAuthenticationFilter(AuthenticationManager authenticationManager) {
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
		filter.setAuthenticationManager(authenticationManager);
		filter.setAuthenticationSuccessHandler(successHandler());
		filter.setAuthenticationFailureHandler((request, response, authException) -> {
			response.setContentType("application/json");
			response.setStatus(FORBIDDEN.value());
			new ObjectMapper().findAndRegisterModules().writeValue(
				response.getOutputStream(),
				new ErrorResponse(
					FORBIDDEN.value(),
					"NOT_LOGGED"
				)
			);
		});
		return filter;
	}

	@Bean
	SimpleUrlAuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
		successHandler.setRedirectStrategy(new NoRedirectStrategy());
		return successHandler;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	public static class NoRedirectStrategy implements RedirectStrategy {
		@Override
		public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
		}
	}

}