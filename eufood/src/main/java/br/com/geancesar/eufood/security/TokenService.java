package br.com.geancesar.eufood.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

	@Value("${api.security.token.public.key}")
	private RSAPublicKey pub;

	@Value("${api.security.token.private.key}")
	private RSAPrivateKey priv;

	@Autowired
	private Environment env;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	public String generateToken(Usuario user) {
		try {
			Instant now = Instant.now();
			int minutosExpiracao = Integer.parseInt(env.getProperty("sessao.usuario.tempo.expiracao.minutos"));

			Algorithm algorithm = Algorithm.RSA256(pub, priv);
			String token = JWT.create().withIssuer("auth-api").withSubject(user.getUsername())
					.withExpiresAt(now.plusSeconds(minutosExpiracao * 60)).sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Erro gerando token", e);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.RSA256(pub, priv);
			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
		} catch (JWTVerificationException e) {
			return "";
		}
	}

	public String getUuidUsuario() {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return "";
		}
		String token = authHeader.replace("Bearer ", "");

		String login = validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		return usuario.get().getUuid();
	}
	
	public Optional<Usuario> getUsuario() {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return Optional.empty();
		}
		String token = authHeader.replace("Bearer ", "");

		String login = validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		return usuario;
	}

}
