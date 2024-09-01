package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("pesquisando o usuario pelo nome {}", username);
        val user =  usuarioRepository.findByUsername(username);
        if( user.isPresent() ) return user.get();

        throw new UsernameNotFoundException("Usuário não encontrado");


    }
}
