package br.edu.ifg.trilhadeaprendizadoapims.learning.controller;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.service.ProgressoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressoController.class)
public class ProgressoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void iniciarProgresso_deveRetornarCreated() throws Exception {
        ProgressoCreateDto dto = new ProgressoCreateDto();
        dto.setUsuarioId(1L);
        dto.setTrilhaId(2L);
        ProgressoDto retorno = new ProgressoDto();

        when(service.iniciarProgresso(any(), anyString())).thenReturn(retorno);

        mockMvc.perform(post("/api/progresso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void obterProgresso_existente_deveRetornarOk() throws Exception {
        ProgressoDto dto = new ProgressoDto();

        when(service.obterProgresso(1L, 2L)).thenReturn(dto);

        mockMvc.perform(get("/api/progresso/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void obterProgresso_naoEncontrado_deveRetornar404() throws Exception {
        when(service.obterProgresso(1L, 2L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/progresso/1/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void concluirTrilha_deveRetornarOk() throws Exception {
        ProgressoDto dto = new ProgressoDto();

        when(service.concluirTrilha(1L, anyString())).thenReturn(dto);

        mockMvc.perform(put("/api/progresso/1/concluir-trilha")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void concluirTrilha_naoEncontrado_deveRetornar404() throws Exception {

        when(service.concluirTrilha(1L, anyString())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/api/progresso/1/concluir-trilha")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void concluirModulo_deveRetornarOk() throws Exception {
        ProgressoDto dto = new ProgressoDto();

        when(service.concluirModulo(1L, anyString())).thenReturn(dto);

        mockMvc.perform(put("/api/progresso/1/concluir-modulo")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void concluirModulo_naoEncontrado_deveRetornar404() throws Exception {

        when(service.concluirModulo(1L, anyString())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/api/progresso/1/concluir-modulo")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluirProgresso_deveRetornarNoContent() throws Exception {
        doNothing().when(service).excluirProgresso(1L);

        mockMvc.perform(delete("/api/progresso/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluirProgresso_naoEncontrado_deveRetornar404() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(service).excluirProgresso(1L);

        mockMvc.perform(delete("/api/progresso/1"))
                .andExpect(status().isNotFound());
    }
}

