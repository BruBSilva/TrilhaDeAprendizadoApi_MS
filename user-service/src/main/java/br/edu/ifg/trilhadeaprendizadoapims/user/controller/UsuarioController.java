package br.edu.ifg.trilhadeaprendizadoapims.user.controller;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.UsuarioAbstractService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

public abstract class UsuarioController<T extends UsuarioDto, K extends UsuarioCreateDto> {

    protected abstract UsuarioAbstractService<T,K> getService();

    @GetMapping("/{id}")
    public ResponseEntity<T> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getService().buscarPorId(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<K> buscarPorEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(getService().buscarPorEmail(email));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            getService().excluir(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> atualizar(@PathVariable Long id, @RequestBody T dto) {
        try {
            return ResponseEntity.ok(getService().atualizar(id, dto));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }
}
