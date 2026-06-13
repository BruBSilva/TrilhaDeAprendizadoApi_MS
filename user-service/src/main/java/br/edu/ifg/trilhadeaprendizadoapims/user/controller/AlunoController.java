package br.edu.ifg.trilhadeaprendizadoapims.user.controller;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.AlunoDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.AlunoService;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.UsuarioAbstractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuario/aluno")
public class AlunoController extends UsuarioController<AlunoDto, UsuarioCreateDto>{

    @Autowired
    private AlunoService alunoService;

    @Override
    protected UsuarioAbstractService<AlunoDto, UsuarioCreateDto> getService() {
        return alunoService;
    }

    @GetMapping
    public Page<AlunoDto> buscarTodos(@PageableDefault(size = 10) Pageable paginacao) {
        return alunoService.buscarTodos(paginacao);
    }

    @PostMapping
    public ResponseEntity<AlunoDto> inserir(@RequestBody @Valid UsuarioCreateDto dto, UriComponentsBuilder uriBuilder) {
        AlunoDto dtoCriado = alunoService.inserir(dto);
        URI endereco = uriBuilder.path("/aluno/{id}").buildAndExpand(dtoCriado.getId()).toUri();
        return ResponseEntity.created(endereco).body(dtoCriado);
    }
    
    @PutMapping("/{id}/add-xp")
    public ResponseEntity<AlunoDto> adicionarXp(@PathVariable Long id, @RequestBody XpUpdateRequest request) {
        try {
            AlunoDto aluno = alunoService.adicionarXp(id, request.getXpGanho());
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/recalcular-xp")
    public ResponseEntity<AlunoDto> recalcularXp(@PathVariable Long id) {
        try {
            AlunoDto aluno = alunoService.recalcularXpTotal(id);
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    public static class XpUpdateRequest {
        private int xpGanho;
        
        public int getXpGanho() { return xpGanho; }
        public void setXpGanho(int xpGanho) { this.xpGanho = xpGanho; }
    }
}
