package br.com.geancesar.eufood.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;

@Service
public class UserDatailsServiceImpl implements UserDetailsService {

	LoginUsuarioRepository repository;

	public UserDatailsServiceImpl(LoginUsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByTelefone(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

}
