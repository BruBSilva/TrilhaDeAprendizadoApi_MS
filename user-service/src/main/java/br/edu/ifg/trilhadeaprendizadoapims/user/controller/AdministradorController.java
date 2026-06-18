package br.edu.ifg.trilhadeaprendizadoapims.user.controller;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.AdministradorDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.AdministradorService;
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
@RequestMapping("/usuario/admin")
public class AdministradorController extends UsuarioController<AdministradorDto, UsuarioCreateDto>{

    @Autowired
    private AdministradorService adminService;

    @Override
    protected UsuarioAbstractService<AdministradorDto, UsuarioCreateDto> getService() {
        return adminService;
    }

    @GetMapping
    public Page<AdministradorDto> buscarTodos(@PageableDefault(size = 10) Pageable paginacao) {
        return adminService.buscarTodos(paginacao);
    }

    @PostMapping
    public ResponseEntity<AdministradorDto> inserir(@RequestBody @Valid UsuarioCreateDto dto, UriComponentsBuilder uriBuilder) {
        AdministradorDto dtoCriado = adminService.inserir(dto);
        URI endereco = uriBuilder.path("/admin/{id}").buildAndExpand(dtoCriado.getId()).toUri();
        return ResponseEntity.created(endereco).body(dtoCriado);
    }
}
