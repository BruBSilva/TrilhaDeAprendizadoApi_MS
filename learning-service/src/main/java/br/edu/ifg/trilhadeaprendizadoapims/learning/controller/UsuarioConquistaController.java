package br.edu.ifg.trilhadeaprendizadoapims.learning.controller;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.UsuarioConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.service.UsuarioConquistaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/progresso/conquista")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioConquistaController {

    @Autowired
    private  UsuarioConquistaService service;

    @GetMapping("/{usuario_id}")
    public ResponseEntity<Page<UsuarioConquistaDto>> listarConquistas(@PathVariable Long usuario_id,
            @PageableDefault(size = 10, sort = "dataConquista", direction = Sort.Direction.DESC) Pageable paginacao) {
        try {
            Page<UsuarioConquistaDto> pagina = service.listarConquistasDoUsuario(usuario_id, paginacao);
            return ResponseEntity.ok(pagina);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar conquistas do usuário.");
        }

    }

    @PostMapping
    public ResponseEntity<UsuarioConquistaDto> criarConquista(@RequestBody @Valid UsuarioConquistaDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.gerarConquista(dto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar conquista para o usuário.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioConquistaDto> atualizarConquista(@PathVariable Long id, @RequestBody @Valid UsuarioConquistaDto dto) {
        try {
            return ResponseEntity.ok(service.atualizarConquista(dto, id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conquista não encontrada para esse usuário.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao atualizar de usuário.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConquista(@PathVariable Long id) {
        try {
            service.excluirConquista(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conquista não encontrada para esse usuário.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao excluir de usuário.");
        }
    }
}
