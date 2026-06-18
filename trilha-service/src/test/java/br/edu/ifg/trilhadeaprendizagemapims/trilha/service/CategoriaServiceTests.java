package br.edu.ifg.trilhadeaprendizagemapims.trilha.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.CategoriaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.CategoriaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTests {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    private ECategoria entidade;
    private CategoriaDTO dto;

    @BeforeEach
    void setUp() {
        entidade = new ECategoria();
        entidade.setId(1L);
        entidade.setNome("Back-End");
        entidade.setDescricao("Categoria voltada para back-end");

        dto = new CategoriaDTO();
        dto.setId(1L);
        dto.setNome("Back-End");
        dto.setDescricao("Categoria voltada para back-end");
    }

    @Test
    void listarCategorias_deveRetornarPaginaDeCategoriaDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ECategoria> entidades = new PageImpl<>(List.of(entidade));
        when(categoriaRepository.findAll(pageable)).thenReturn(entidades);
        when(modelMapper.map(entidade, CategoriaDTO.class)).thenReturn(dto);

        Page<CategoriaDTO> resultado = categoriaService.listarCategorias(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Back-End", resultado.getContent().get(0).getNome());
    }

    @Test
    void obterCategoriaPorId_existente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(entidade));
        when(modelMapper.map(entidade, CategoriaDTO.class)).thenReturn(dto);

        CategoriaDTO resultado = categoriaService.obterCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Back-End", resultado.getNome());
    }

    @Test
    void obterCategoriaPorId_naoEncontrada() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoriaService.obterCategoriaPorId(1L));
    }

    @Test
    void cadastrarCategoria_sucesso() {
        when(modelMapper.map(dto, ECategoria.class)).thenReturn(entidade);
        when(modelMapper.map(entidade, CategoriaDTO.class)).thenReturn(dto);

        CategoriaDTO resultado = categoriaService.cadastrarCategoria(dto);

        assertNotNull(resultado);
        verify(categoriaRepository).save(entidade);
    }

    @Test
    void deletarCategoria_existente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(entidade));

        categoriaService.deletarCategoria(1L);

        verify(categoriaRepository).delete(entidade);
    }

    @Test
    void deletarCategoria_naoEncontrada() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoriaService.deletarCategoria(1L));
    }

    @Test
    void atualizarCategoria_existente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(entidade));
        doNothing().when(modelMapper).map(eq(dto), eq(entidade));
        when(modelMapper.map(entidade, CategoriaDTO.class)).thenReturn(dto);

        CategoriaDTO resultado = categoriaService.atualizarCategoria(1L, dto);

        assertNotNull(resultado);
        assertEquals("Back-End", resultado.getNome());
        verify(categoriaRepository).save(entidade);
    }

    @Test
    void atualizarCategoria_naoEncontrada() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoriaService.atualizarCategoria(1L, dto));
    }
}