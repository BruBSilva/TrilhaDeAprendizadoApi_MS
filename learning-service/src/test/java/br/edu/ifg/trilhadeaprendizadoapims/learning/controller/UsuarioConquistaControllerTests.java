package br.edu.ifg.trilhadeaprendizadoapims.learning.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.UsuarioConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.service.UsuarioConquistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

class UsuarioConquistaControllerTests {

    @Mock
    private UsuarioConquistaService service;

    @InjectMocks
    private UsuarioConquistaController controller;

    private UsuarioConquistaDto dto;
    private Pageable pageable;
    private Page<UsuarioConquistaDto> page;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new UsuarioConquistaDto();
        dto.setId(1L);
        dto.setUsuarioId(1L);
        dto.setConquistaId(1L);

        pageable = PageRequest.of(0, 10, Sort.by("dataConquista").descending());
        page = new PageImpl<>(List.of(dto));
    }

    @Test
    void listarConquistas_deveRetornarOk() {
        when(service.listarConquistasDoUsuario(1L, pageable)).thenReturn(page);

        ResponseEntity<Page<UsuarioConquistaDto>> response = controller.listarConquistas(1L, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(service).listarConquistasDoUsuario(1L, pageable);
    }

    @Test
    void listarConquistas_erroDeveLancarInternalServerError() {
        when(service.listarConquistasDoUsuario(anyLong(), any())).thenThrow(new RuntimeException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.listarConquistas(1L, pageable);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erro ao listar conquistas"));
    }

    @Test
    void criarConquista_deveRetornarCreated() {
        when(service.gerarConquista(dto)).thenReturn(dto);

        ResponseEntity<UsuarioConquistaDto> response = controller.criarConquista(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service).gerarConquista(dto);
    }

    @Test
    void criarConquista_erroDeveLancarBadRequest() {
        when(service.gerarConquista(any())).thenThrow(new RuntimeException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.criarConquista(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erro ao criar conquista"));
    }

    @Test
    void atualizarConquista_existente_deveRetornarOk() {
        when(service.atualizarConquista(dto, 1L)).thenReturn(dto);

        ResponseEntity<UsuarioConquistaDto> response = controller.atualizarConquista(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service).atualizarConquista(dto, 1L);
    }

    @Test
    void atualizarConquista_naoEncontrada_deveRetornarNotFound() {
        when(service.atualizarConquista(dto, 1L)).thenThrow(new EntityNotFoundException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.atualizarConquista(1L, dto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Conquista não encontrada"));
    }

    @Test
    void atualizarConquista_erroDeveRetornarBadRequest() {
        when(service.atualizarConquista(dto, 1L)).thenThrow(new RuntimeException());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.atualizarConquista(1L, dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erro ao atualizar"));
    }

    @Test
    void excluirConquista_existente_deveRetornarNoContent() {
        doNothing().when(service).excluirConquista(1L);

        ResponseEntity<Void> response = controller.excluirConquista(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).excluirConquista(1L);
    }

    @Test
    void excluirConquista_naoEncontrada_deveRetornarNotFound() {
        doThrow(new EntityNotFoundException()).when(service).excluirConquista(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.excluirConquista(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Conquista não encontrada"));
    }

    @Test
    void excluirConquista_erroDeveRetornarBadRequest() {
        doThrow(new RuntimeException()).when(service).excluirConquista(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.excluirConquista(1L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erro ao excluir"));
    }
}
