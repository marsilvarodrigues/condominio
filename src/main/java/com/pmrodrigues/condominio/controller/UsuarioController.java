package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.NovoUsuarioDTO;
import com.pmrodrigues.condominio.dto.ReservaRequestDTO;
import com.pmrodrigues.condominio.dto.ReservaResponseDTO;
import com.pmrodrigues.condominio.dto.UsuarioDTO;
import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.services.UsuarioService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/usuarios")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping(value="/{guid}")
    public ResponseEntity<UsuarioDTO> salvarUsuario(@PathVariable("guid") @NonNull String guid, @RequestBody UsuarioDTO usuario) {

        log.info("efetuando uma alternacao no usuario {} - {}", guid, usuario);
        usuario = new UsuarioDTO(usuario.id(), guid, usuario.username(), usuario.dataCriacao(), usuario.perfis());
        val response = usuarioService.alterarUsuario(usuario);
        log.info("usuario {} alterado com sucesso", response);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping()
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody NovoUsuarioDTO usuario) {

        log.info("efetuando a criacao do usuario {} - {}", usuario);
        val response = usuarioService.criarNovoUsuario(usuario);
        log.info("usuario {} criado com sucesso", response);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios(@ModelAttribute UsuarioDTO usuario) {

        log.info("Listando usuarios: {}", usuario);

        val usuarios = usuarioService.pesquisarUsuarios(usuario);
        log.info("Usuarios encontrados com sucesso: {}", usuarios);

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping(value="/{guid}")
    public ResponseEntity<UsuarioDTO> recuperarUsuario(@PathVariable String guid) {
        log.info("pesquisando usuario: {}", guid);
        val usuario = usuarioService.recuperarUsuario(guid);

        log.info("Usuarios encontrados com sucesso: {}", usuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }
}
