package br.com.geancesar.eufood.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.geancesar.eufood.login.model.UsuarioRole;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	SecurityFilter securityFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) {
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/usuario_login/login").permitAll()
						.requestMatchers("/usuario/cadastrar").permitAll()
						.requestMatchers("/usuario/cadastrar/consultar_telefone").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuario/cadastrar/consultar_email").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuario/cadastrar_usuario_restaurante").hasAuthority(UsuarioRole.ADMIN.getRole())						
						.requestMatchers(HttpMethod.POST, "/restaurante/cadastrar").hasAnyAuthority(UsuarioRole.ADMIN.getRole(), UsuarioRole.RESTAURANTE.getRole())
						.requestMatchers(HttpMethod.POST, "/restaurante/upload/imagem_perfil").hasAnyAuthority(UsuarioRole.ADMIN.getRole(), UsuarioRole.RESTAURANTE.getRole())
						.requestMatchers(HttpMethod.POST, "/restaurante/upload/imagem_capa").hasAnyAuthority(UsuarioRole.ADMIN.getRole(), UsuarioRole.RESTAURANTE.getRole())
						.requestMatchers(HttpMethod.POST, "/restaurante/item_cardapio/cadastrar").hasAnyAuthority(UsuarioRole.ADMIN.getRole(), UsuarioRole.RESTAURANTE.getRole())
						.requestMatchers(HttpMethod.POST, "/restaurante/item_cardapio/upload/imagem_perfil").hasAnyAuthority(UsuarioRole.ADMIN.getRole(), UsuarioRole.RESTAURANTE.getRole())
						.requestMatchers(HttpMethod.POST, "/pedido").hasAnyAuthority(UsuarioRole.USUARIO.getRole())
						.anyRequest().authenticated())
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
