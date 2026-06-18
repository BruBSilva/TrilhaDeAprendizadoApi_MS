package br.edu.ifg.trilhadeaprendizadoapims.learning.controller;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.service.ProgressoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/progresso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressoController {

    @Autowired
    private ProgressoService service;

    @PostMapping
    public ResponseEntity<ProgressoDto> iniciarProgresso(@RequestBody @Valid ProgressoCreateDto dto, HttpServletRequest request) {
        String jwt = extrairJwtDaRequisicao(request);
        ProgressoDto criado = service.iniciarProgresso(dto, jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{usuario_id}/{trilha_id}")
    public ResponseEntity<ProgressoDto> obterProgresso(@PathVariable("usuario_id") Long usuario_id,
                                                       @PathVariable("trilha_id") Long trilha_id) {
        try {
            return ResponseEntity.ok(service.obterProgresso(usuario_id, trilha_id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Progresso não encontrado.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao buscar progresso: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/concluir-trilha")
    public ResponseEntity<ProgressoDto> concluirTrilha(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = extrairJwtDaRequisicao(request);
            ProgressoDto atualizado = service.concluirTrilha(id, jwt);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Progresso não encontrado.");
        }
    }

    @PutMapping("/{id}/concluir-modulo")
    public ResponseEntity<ProgressoDto> concluirModulo(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = extrairJwtDaRequisicao(request);
            ProgressoDto atualizado = service.concluirModulo(id, jwt);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Progresso não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProgresso(@PathVariable Long id) {
        try {
            service.excluirProgresso(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Progresso não encontrado.");
        }
    }
    
    private String extrairJwtDaRequisicao(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
