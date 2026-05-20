package br.edu.ifg.trilhadeaprendizadoapims.user.service;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioAbstractService<T extends UsuarioDto, K extends UsuarioCreateDto> {

    T buscarPorId(Long id);
    K buscarPorEmail(String email);
    T atualizar(Long id, T dto);
    void excluir(Long id);
    Page<T> buscarTodos(Pageable pageable);
    T inserir(K dto);
}
