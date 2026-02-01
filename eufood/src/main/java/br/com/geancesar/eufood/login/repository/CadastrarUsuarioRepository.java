package br.com.geancesar.eufood.login.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.login.model.Usuario;

public interface CadastrarUsuarioRepository extends CrudRepository<Usuario, String> {

	Optional<Usuario> findByTelefone(String telefone);

}
