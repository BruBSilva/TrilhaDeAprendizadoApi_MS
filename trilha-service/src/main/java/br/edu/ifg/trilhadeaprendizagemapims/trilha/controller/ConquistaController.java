package br.edu.ifg.trilhadeaprendizagemapims.trilha.controller;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.ConquistaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/trilha/conquistas")
public class ConquistaController {

    @Autowired
    private ConquistaService conquistaService;

    @GetMapping
    public Object listarConquistas(@PageableDefault(size = 50) Pageable pageable) {
        return conquistaService.listarConquistas(pageable);
    }

    @GetMapping({"/t/{tipo}"})
    public Object obterConquistasPorTipo(@PageableDefault(size = 10) Pageable pageable, @PathVariable String tipo) {
        try {
            return conquistaService.obterConquistasPorTipo(pageable, tipo);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conquista não encontrada");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConquistaDTO> obterConquistaPorId(@PathVariable @NotNull Long id) {
        try {
            ConquistaDTO dto = conquistaService.obterConquistaPorId(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conquista não encontrada");
        }
    }
}
