package br.edu.ifg.trilhadeaprendizagemapims.trilha.service;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.ConquistaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.ConquistaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConquistaServiceTests {

    @InjectMocks
    private ConquistaService conquistaService;

    @Mock
    private ConquistaRepository conquistaRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarConquistas_deveRetornarPaginaDeConquistas() {
        EConquista conquista = new EConquista();
        ConquistaDTO dto = new ConquistaDTO();
        Pageable pageable = PageRequest.of(0, 10);

        when(conquistaRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(conquista)));
        when(modelMapper.map(conquista, ConquistaDTO.class)).thenReturn(dto);

        Page<ConquistaDTO> resultado = conquistaService.listarConquistas(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(conquistaRepository).findAll(pageable);
    }

    @Test
    void obterConquistaPorId_existente() {
        EConquista conquista = new EConquista();
        ConquistaDTO dto = new ConquistaDTO();

        when(conquistaRepository.findById(1L)).thenReturn(Optional.of(conquista));
        when(modelMapper.map(conquista, ConquistaDTO.class)).thenReturn(dto);

        ConquistaDTO resultado = conquistaService.obterConquistaPorId(1L);

        assertNotNull(resultado);
        verify(conquistaRepository).findById(1L);
    }

    @Test
    void obterConquistaPorId_naoEncontrada() {
        when(conquistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> conquistaService.obterConquistaPorId(1L));
    }

    @Test
    void obterConquistasPorTipo_existente() {
        EConquista conquista = new EConquista();
        ConquistaDTO dto = new ConquistaDTO();
        Pageable pageable = PageRequest.of(0, 10);

        when(conquistaRepository.findByTipo(TipoConquista.TRILHA)).thenReturn(Optional.of(conquista));
        when(conquistaRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(conquista)));
        when(modelMapper.map(conquista, ConquistaDTO.class)).thenReturn(dto);

        Page<ConquistaDTO> resultado = conquistaService.obterConquistasPorTipo(pageable, "TRILHA");

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(conquistaRepository).findByTipo(TipoConquista.TRILHA);
        verify(conquistaRepository).findAll(pageable);
    }

    @Test
    void obterConquistasPorTipo_naoEncontrada() {
        Pageable pageable = PageRequest.of(0, 10);
        when(conquistaRepository.findByTipo(TipoConquista.TRILHA)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> conquistaService.obterConquistasPorTipo(pageable, "TRILHA"));
        assertEquals("Conquista do tipo TRILHA não encontrada", thrown.getMessage());
    }

    @Test
    void cadastrarConquista_deveSalvarConquista() {
        ConquistaDTO dto = new ConquistaDTO();
        EConquista entidade = new EConquista();

        when(modelMapper.map(dto, EConquista.class)).thenReturn(entidade);
        when(modelMapper.map(entidade, ConquistaDTO.class)).thenReturn(dto);

        ConquistaDTO resultado = conquistaService.cadastrarConquista(dto);

        verify(conquistaRepository).save(entidade);
        assertNotNull(resultado);
    }

    @Test
    void deletarConquista_existente() {
        EConquista entidade = new EConquista();
        when(conquistaRepository.findById(1L)).thenReturn(Optional.of(entidade));

        conquistaService.deletarConquista(1L);

        verify(conquistaRepository).delete(entidade);
    }

    @Test
    void deletarConquista_naoEncontrada() {
        when(conquistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> conquistaService.deletarConquista(1L));
    }

    @Test
    void atualizarConquista_existente() {
        EConquista entidade = spy(new EConquista());
        ConquistaDTO dto = new ConquistaDTO();

        when(conquistaRepository.findById(1L)).thenReturn(Optional.of(entidade));
        doNothing().when(modelMapper).map(eq(dto), eq(entidade));
        when(modelMapper.map(entidade, ConquistaDTO.class)).thenReturn(dto);

        doAnswer(invocation -> {

            return null;
        }).when(modelMapper).map(dto, entidade);

        ConquistaDTO resultado = conquistaService.atualizarConquista(1L, dto);

        verify(conquistaRepository).save(entidade);
        assertNotNull(resultado);
    }

    @Test
    void atualizarConquista_naoEncontrada() {
        ConquistaDTO dto = new ConquistaDTO();
        when(conquistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> conquistaService.atualizarConquista(1L, dto));
    }
}

