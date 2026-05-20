package br.edu.ifg.trilhadeaprendizadoapims.learning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.UsuarioConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.model.EUsuarioConquista;
import br.edu.ifg.trilhadeaprendizadoapims.learning.repository.UsuarioConquistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

class UsuarioConquistaServiceTests {

    @Mock
    private UsuarioConquistaRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioConquistaService service;

    private EUsuarioConquista entidade;
    private UsuarioConquistaDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        entidade = new EUsuarioConquista();
        entidade.setId(1L);
        // Inicialize outros campos se necessário

        dto = new UsuarioConquistaDto();
        dto.setId(1L);
        // Inicialize outros campos se necessário
    }

    @Test
    void listarConquistasDoUsuario_deveRetornarPaginaDeDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EUsuarioConquista> pageEntidade = new PageImpl<>(List.of(entidade));
        when(repository.findByUsuarioId(1L, pageable)).thenReturn(pageEntidade);
        when(modelMapper.map(entidade, UsuarioConquistaDto.class)).thenReturn(dto);

        Page<UsuarioConquistaDto> resultado = service.listarConquistasDoUsuario(1L, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }

    @Test
    void gerarConquista_deveSalvarEDevolverDto() {
        when(modelMapper.map(dto, EUsuarioConquista.class)).thenReturn(entidade);
        when(modelMapper.map(entidade, UsuarioConquistaDto.class)).thenReturn(dto);

        UsuarioConquistaDto resultado = service.gerarConquista(dto);

        assertNotNull(resultado);
        verify(repository).save(entidade);
        assertEquals(dto, resultado);
    }

    @Test
    void atualizarConquista_existente_deveAtualizarERetornarDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidade));
        doNothing().when(modelMapper).map(dto, entidade);
        when(modelMapper.map(entidade, UsuarioConquistaDto.class)).thenReturn(dto);

        UsuarioConquistaDto resultado = service.atualizarConquista(dto, 1L);

        assertNotNull(resultado);
        verify(repository).save(entidade);
        assertEquals(dto, resultado);
        assertEquals(1L, entidade.getId());
    }

    @Test
    void atualizarConquista_inexistente_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.atualizarConquista(dto, 1L));
        assertEquals("Conquista não encontrada para esse usuário", exception.getMessage());
    }

    @Test
    void excluirConquista_existente_deveChamarDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidade));

        service.excluirConquista(1L);

        verify(repository).delete(entidade);
    }

    @Test
    void excluirConquista_inexistente_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.excluirConquista(1L));
        assertEquals("Conquista não encontrada para esse usuário", exception.getMessage());
    }
}
