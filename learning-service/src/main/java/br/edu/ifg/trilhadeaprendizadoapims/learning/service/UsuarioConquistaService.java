package br.edu.ifg.trilhadeaprendizadoapims.learning.service;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.UsuarioConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.model.EUsuarioConquista;
import br.edu.ifg.trilhadeaprendizadoapims.learning.repository.UsuarioConquistaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioConquistaService {

    @Autowired
    private UsuarioConquistaRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<UsuarioConquistaDto> listarConquistasDoUsuario(Long usuarioId, Pageable paginacao) {
        return repository.findByUsuarioId(usuarioId, paginacao)
                .map(entidade -> modelMapper.map(entidade, UsuarioConquistaDto.class));
    }

    public UsuarioConquistaDto gerarConquista(UsuarioConquistaDto dto){
        EUsuarioConquista entidade = modelMapper.map(dto, EUsuarioConquista.class);
        repository.save(entidade);

        return modelMapper.map(entidade, UsuarioConquistaDto.class);
    }

    public UsuarioConquistaDto atualizarConquista(UsuarioConquistaDto dto, Long id){
        EUsuarioConquista entidade = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("Conquista não encontrada para esse usuário"));
        modelMapper.map(dto, entidade);
        entidade.setId(id);
        repository.save(entidade);

        return modelMapper.map(entidade, UsuarioConquistaDto.class);
    }

    public void excluirConquista(Long id){
        EUsuarioConquista entidade = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("Conquista não encontrada para esse usuário"));
        repository.delete(entidade);
    }

}
