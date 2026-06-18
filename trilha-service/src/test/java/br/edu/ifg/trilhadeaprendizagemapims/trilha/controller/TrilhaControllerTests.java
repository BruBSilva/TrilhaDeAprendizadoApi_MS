package br.edu.ifg.trilhadeaprendizagemapims.trilha.controller;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.*;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.TrilhaService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrilhaControllerTests {

    @Mock
    private TrilhaService trilhaService;

    @InjectMocks
    private TrilhaController trilhaController;

    private TrilhaDTO trilhaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trilhaDTO = new TrilhaDTO();
        trilhaDTO.setId(1L);
        trilhaDTO.setNome("Trilha Teste");
        trilhaDTO.setDescricao("Descricao Teste");
        trilhaDTO.setCategoria(new CategoriaDTO());
        trilhaDTO.setModulos(new ArrayList<>());
        trilhaDTO.setConquista(new ConquistaDTO());
    }

    @Test
    void listarTrilhas_deveRetornarListaPaginada() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrilhaDTO> page = new PageImpl<>(List.of(trilhaDTO));

        when(trilhaService.listarTrilhas(pageable)).thenReturn(page);

        Object resultado = trilhaController.listarTrilhas(pageable);

        assertEquals(page, resultado);
        verify(trilhaService).listarTrilhas(pageable);
    }

    @Test
    void detalharTrilha_existente_deveRetornarOk() {
        when(trilhaService.obterTrilhaPorId(1L)).thenReturn(trilhaDTO);

        ResponseEntity<TrilhaDTO> response = trilhaController.detalharTrilha(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trilhaDTO, response.getBody());
    }

    @Test
    void detalharTrilha_inexistente_deveLancarNotFound() {
        when(trilhaService.obterTrilhaPorId(1L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> trilhaController.detalharTrilha(1L));
    }

    @Test
    void cadastrarTrilha_deveRetornarCreated() {
        when(trilhaService.cadastrarTrilha(any())).thenReturn(trilhaDTO);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<TrilhaDTO> response = trilhaController.cadastrarTrilha(trilhaDTO, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/trilhas/1"));
    }

    @Test
    void atualizarTrilha_deveRetornarOk() {
        when(trilhaService.atualizarTrilha(eq(1L), any())).thenReturn(trilhaDTO);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<TrilhaDTO> response = trilhaController.atualizarTrilha(1L, trilhaDTO, uriBuilder);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trilhaDTO, response.getBody());
    }

    @Test
    void deletarTrilha_deveRetornarNoContent() {
        ResponseEntity<Void> response = trilhaController.deletarTrilha(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(trilhaService).deletarTrilha(1L);
    }

    @Test
    void listarIdsDosModulos_deveRetornarIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trilhaService.getModulosIds(1L)).thenReturn(ids);

        ResponseEntity<List<Long>> response = trilhaController.listarIdsDosModulos(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ids, response.getBody());
    }

    @Test
    void obterConquistaDaTrilha_deveRetornarDTO() {
        ConquistaDetalhadaDTO conquista = new ConquistaDetalhadaDTO();
        when(trilhaService.obterConquistaPorTrilhaId(1L)).thenReturn(conquista);

        ResponseEntity<ConquistaDetalhadaDTO> response = trilhaController.obterConquistaDaTrilha(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(conquista, response.getBody());
    }

    @Test
    void obterConquistaDoModulo_deveRetornarDTO() {
        ConquistaDetalhadaDTO conquista = new ConquistaDetalhadaDTO();
        when(trilhaService.obterConquistaPorModuloId(1L)).thenReturn(conquista);

        ResponseEntity<ConquistaDetalhadaDTO> response = trilhaController.obterConquistaDoModulo(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(conquista, response.getBody());
    }
}
