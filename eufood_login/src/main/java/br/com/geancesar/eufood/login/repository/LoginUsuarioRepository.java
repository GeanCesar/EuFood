package br.com.geancesar.eufood.login.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.login.model.Usuario;

public interface LoginUsuarioRepository extends CrudRepository<Usuario, String> {

	Optional<Usuario> findByTelefone(String telefone);
	Optional<Usuario> findByEmailAndSenha(String email, String senha);	
	Optional<Usuario> findByTelefoneAndSenha(String telefone, String senha);

}
