package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;

import java.util.Date;
import java.util.List;

public record NovoUsuarioDTO( String username, String password, List<PerfilDTO> perfis) {

}
