package br.edu.ifg.trilhadeaprendizagemapims.trilha.controller;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.ConquistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ConquistaControllerTests {

    @Mock
    private ConquistaService conquistaService;

    @InjectMocks
    private ConquistaController conquistaController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void listarConquistas_deveRetornarPaginaDeConquistas() {
        ConquistaDTO dto = new ConquistaDTO();
        Page<ConquistaDTO> page = new PageImpl<>(List.of(dto));

        when(conquistaService.listarConquistas(any())).thenReturn(page);

        Object resposta = conquistaController.listarConquistas(PageRequest.of(0, 10));
        assertNotNull(resposta);
        assertTrue(resposta instanceof Page);
        assertEquals(1, ((Page<?>) resposta).getTotalElements());
    }

    @Test
    void obterConquistasPorTipo_deveRetornarPaginaComConquistasFiltradas() {
        ConquistaDTO dto = new ConquistaDTO();
        Page<ConquistaDTO> page = new PageImpl<>(List.of(dto));

        when(conquistaService.obterConquistasPorTipo(any(), eq("tipo"))).thenReturn(page);

        Object resposta = conquistaController.obterConquistasPorTipo(PageRequest.of(0, 10), "tipo");
        assertNotNull(resposta);
        assertTrue(resposta instanceof Page);
    }

    @Test
    void obterConquistasPorTipo_deveLancar404QuandoTipoNaoEncontrado() {
        when(conquistaService.obterConquistasPorTipo(any(), eq("invalido")))
                .thenThrow(new EntityNotFoundException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                conquistaController.obterConquistasPorTipo(PageRequest.of(0, 10), "invalido"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Conquista não encontrada"));
    }

    @Test
    void obterConquistaPorId_deveRetornarConquistaQuandoEncontrada() {
        ConquistaDTO dto = new ConquistaDTO();
        when(conquistaService.obterConquistaPorId(1L)).thenReturn(dto);

        ResponseEntity<ConquistaDTO> resposta = conquistaController.obterConquistaPorId(1L);
        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(dto, resposta.getBody());
    }

    @Test
    void obterConquistaPorId_deveLancar404QuandoNaoEncontrada() {
        when(conquistaService.obterConquistaPorId(99L)).thenThrow(new EntityNotFoundException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                conquistaController.obterConquistaPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Conquista não encontrada"));
    }
}
