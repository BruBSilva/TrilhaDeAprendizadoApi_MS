package br.edu.ifg.trilhadeaprendizagemapims.trilha.controller;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDetalhadaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.TrilhaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.TrilhaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trilha")
public class TrilhaController {
    
    @Autowired
    private TrilhaService trilhaService;

    @GetMapping
    public Object listarTrilhas(@PageableDefault(size = 50) Pageable pageable) {
        return trilhaService.listarTrilhas(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrilhaDTO> detalharTrilha(@PathVariable Long id) {
        try {
            TrilhaDTO dto = trilhaService.obterTrilhaPorId(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TrilhaDTO> cadastrarTrilha(@RequestBody @Valid TrilhaDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            TrilhaDTO trilha = trilhaService.cadastrarTrilha(dto);
            URI uri = uriBuilder.path("/api/trilhas/{id}").buildAndExpand(trilha.getId()).toUri();
            return ResponseEntity.created(uri).body(trilha);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrilhaDTO> atualizarTrilha(@PathVariable Long id, @RequestBody @Valid TrilhaDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            TrilhaDTO trilha = trilhaService.atualizarTrilha(id, dto);
            return ResponseEntity.ok(trilha);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTrilha(@PathVariable Long id) {
        try {
            trilhaService.deletarTrilha(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //Usados no serviço learning

    @GetMapping("/{trilha_id}/modulos-ids")
    public ResponseEntity<List<Long>> listarIdsDosModulos(@PathVariable Long trilha_id) {
        try {
            List<Long> modulos = trilhaService.getModulosIds(trilha_id);
            return ResponseEntity.ok(modulos);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{trilha_id}/trilha-conquista-detalhada")
    public ResponseEntity<ConquistaDetalhadaDTO> obterConquistaDaTrilha(@PathVariable Long trilha_id) {
        try {
            ConquistaDetalhadaDTO dto = trilhaService.obterConquistaPorTrilhaId(trilha_id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/modulo-conquista-detalhada/{modulo_id}")
    public ResponseEntity<ConquistaDetalhadaDTO> obterConquistaDoModulo(@PathVariable Long modulo_id) {
        try {
            ConquistaDetalhadaDTO dto = trilhaService.obterConquistaPorModuloId(modulo_id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
