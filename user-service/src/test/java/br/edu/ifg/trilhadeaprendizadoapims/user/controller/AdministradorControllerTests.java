package br.edu.ifg.trilhadeaprendizadoapims.user.controller;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.AdministradorDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.AdministradorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministradorController.class)
class AdministradorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private AdministradorDto adminDto;
    private UsuarioCreateDto createDto;

    @BeforeEach
    void setUp() {
        adminDto = new AdministradorDto();
        adminDto.setId(1L);
        adminDto.setNome("Admin");
        adminDto.setEmail("admin@ifg.edu.br");

        createDto = new UsuarioCreateDto();
        createDto.setNome("Novo Admin");
        createDto.setEmail("novo@ifg.edu.br");
        createDto.setSenha("123456");
    }

    @Test
    void deveRetornarListaDeAdministradores() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AdministradorDto> page = new PageImpl<>(List.of(adminDto), pageable, 1);

        Mockito.when(adminService.buscarTodos(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(adminDto.getId()))
                .andExpect(jsonPath("$.content[0].nome").value(adminDto.getNome()));
    }

    @Test
    void deveInserirAdministrador() throws Exception {
        AdministradorDto criado = new AdministradorDto();
        criado.setId(10L);
        criado.setNome("Novo Admin");

        Mockito.when(adminService.inserir(Mockito.any(UsuarioCreateDto.class))).thenReturn(criado);

        mockMvc.perform(post("/api/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Novo Admin"));
    }

    @Test
    void deveBuscarAdministradorPorId() throws Exception {
        Mockito.when(adminService.buscarPorId(1L)).thenReturn(adminDto);

        mockMvc.perform(get("/api/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Admin"));
    }

    @Test
    void deveAtualizarAdministrador() throws Exception {
        AdministradorDto atualizado = new AdministradorDto();
        atualizado.setId(1L);
        atualizado.setNome("Admin Atualizado");

        Mockito.when(adminService.atualizar(Mockito.eq(1L), Mockito.any(AdministradorDto.class)))
                .thenReturn(atualizado);

        mockMvc.perform(put("/api/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Admin Atualizado"));
    }

    @Test
    void deveExcluirAdministrador() throws Exception {
        Mockito.doNothing().when(adminService).excluir(1L);

        mockMvc.perform(delete("/api/admin/1"))
                .andExpect(status().isNoContent());
    }
}

