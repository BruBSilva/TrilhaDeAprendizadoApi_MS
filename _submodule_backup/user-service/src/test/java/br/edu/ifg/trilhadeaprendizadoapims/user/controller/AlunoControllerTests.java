package br.edu.ifg.trilhadeaprendizadoapims.user.controller;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.AlunoDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    private ObjectMapper objectMapper;

    private AlunoDto alunoDto;
    private UsuarioCreateDto createDto;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        alunoDto = new AlunoDto();
        alunoDto.setId(1L);
        alunoDto.setNome("João");
        alunoDto.setEmail("joao@ifg.edu.br");

        createDto = new UsuarioCreateDto();
        createDto.setNome("Maria");
        createDto.setEmail("maria@gmail.com");
        createDto.setSenha("senhahash");
    }

    @Test
    void deveRetornarListaDeAlunos() throws Exception {
        Page<AlunoDto> pagina = new PageImpl<>(List.of(alunoDto));

        when(alunoService.buscarTodos(any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/api/aluno")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].nome", is("João")))
                .andExpect(jsonPath("$.content[0].email", is("joao@ifg.edu.br")));
    }

    @Test
    void deveInserirAlunoComSucesso() throws Exception {
        AlunoDto responseDto = new AlunoDto();
        responseDto.setId(1L);
        responseDto.setNome("Maria");
        responseDto.setEmail("maria@gmail.com");

        when(alunoService.inserir(any(UsuarioCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/aluno/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Maria")))
                .andExpect(jsonPath("$.email", is("maria@gmail.com")));
    }

    @Test
    void deveBuscarAlunoPorId() throws Exception {
        when(alunoService.buscarPorId(1L)).thenReturn(alunoDto);

        mockMvc.perform(get("/api/aluno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("João")))
                .andExpect(jsonPath("$.email", is("joao@ifg.edu.br")));
    }

    @Test
    void deveRetornar404AoTentarBuscarAlunoInexistente() throws Exception {
        when(alunoService.buscarPorId(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        mockMvc.perform(get("/api/aluno/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirAluno() throws Exception {
        doNothing().when(alunoService).excluir(1L);

        mockMvc.perform(delete("/api/aluno/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoTentarExcluirAlunoInexistente() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException()).when(alunoService).excluir(99L);

        mockMvc.perform(delete("/api/aluno/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarAlunoComSucesso() throws Exception {
        AlunoDto dtoAtualizado = new AlunoDto();
        dtoAtualizado.setId(1L);
        dtoAtualizado.setNome("Nome Atualizado");
        dtoAtualizado.setEmail("atualizado@ifg.edu.br");

        when(alunoService.atualizar(eq(1L), any(AlunoDto.class))).thenReturn(dtoAtualizado);

        mockMvc.perform(put("/api/aluno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Nome Atualizado")))
                .andExpect(jsonPath("$.email", is("atualizado@ifg.edu.br")));
    }

    @Test
    void deveRetornar404AoTentarAtualizarAlunoInexistente() throws Exception {
        AlunoDto dto = new AlunoDto();
        dto.setId(99L);
        dto.setNome("Nome");
        dto.setEmail("email@ifg.edu.br");

        when(alunoService.atualizar(eq(99L), any(AlunoDto.class))).thenThrow(new jakarta.persistence.EntityNotFoundException());

        mockMvc.perform(put("/api/aluno/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}

