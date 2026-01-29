package br.com.geancesar.eufood.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	TokenService tokenService;

	@Autowired
	LoginUsuarioRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = this.recoverToken(request);
		if (token != null) {
			String login = tokenService.validateToken(token);
			Optional<Usuario> usuario = repository.findByTelefone(login);

			if (usuario.isPresent()) {
				UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
						usuario.get(), null, usuario.get().getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(autenticacao);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return null;
		}
		return authHeader.replace("Bearer ", "");
	}
}
