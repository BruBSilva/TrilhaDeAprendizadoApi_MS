package br.edu.ifg.trilhadeaprendizagemapims.trilha.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.CategoriaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

public class CategoriaControllerTests {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNome("Categoria Teste");
    }

    @Test
    void listarCategorias_deveRetornarListaPaginada() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoriaDTO> page = new PageImpl<>(List.of(categoriaDTO));

        when(categoriaService.listarCategorias(pageable)).thenReturn(page);

        Object resultado = categoriaController.listarCategorias(pageable);

        assertEquals(page, resultado);
        verify(categoriaService).listarCategorias(pageable);
    }

    @Test
    void detalharCategoria_existente_deveRetornarOk() {
        when(categoriaService.obterCategoriaPorId(1L)).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.detalharCategoria(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoriaDTO, response.getBody());
    }

    @Test
    void detalharCategoria_inexistente_deveLancarNotFound() {
        when(categoriaService.obterCategoriaPorId(1L)).thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> categoriaController.detalharCategoria(1L));
    }

    @Test
    void cadastrarCategoria_deveRetornarCreated() {
        when(categoriaService.cadastrarCategoria(any())).thenReturn(categoriaDTO);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<CategoriaDTO> response = categoriaController.cadastrarCategoria(categoriaDTO, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/categorias/1"));
    }

    @Test
    void cadastrarCategoriasEmLote_deveRetornarCreatedComLista() {
        List<CategoriaDTO> entrada = List.of(categoriaDTO);
        when(categoriaService.cadastrarCategoria(any())).thenReturn(categoriaDTO);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<List<CategoriaDTO>> response = categoriaController.cadastrarCategoriasEmLote(entrada, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(entrada, response.getBody());
    }

    @Test
    void atualizarCategoria_deveRetornarOk() {
        when(categoriaService.atualizarCategoria(eq(1L), any())).thenReturn(categoriaDTO);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<CategoriaDTO> response = categoriaController.atualizarCategoria(1L, categoriaDTO, uriBuilder);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoriaDTO, response.getBody());
    }

    @Test
    void atualizarCategoria_inexistente_deveLancarNotFound() {
        when(categoriaService.atualizarCategoria(eq(1L), any())).thenThrow(new EntityNotFoundException());
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        assertThrows(ResponseStatusException.class, () -> categoriaController.atualizarCategoria(1L, categoriaDTO, uriBuilder));
    }

    @Test
    void deletarCategoria_deveRetornarNoContent() {
        ResponseEntity<Void> response = categoriaController.deletarCategoria(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(categoriaService).deletarCategoria(1L);
    }

    @Test
    void deletarCategoria_inexistente_deveLancarNotFound() {
        doThrow(new EntityNotFoundException()).when(categoriaService).deletarCategoria(1L);

        assertThrows(ResponseStatusException.class, () -> categoriaController.deletarCategoria(1L));
    }
}
