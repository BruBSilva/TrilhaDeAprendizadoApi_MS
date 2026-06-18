package br.edu.ifg.trilhadeaprendizadoapims.learning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.ProgressoDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.dto.UsuarioConquistaDto;
import br.edu.ifg.trilhadeaprendizadoapims.learning.model.EProgresso;
import br.edu.ifg.trilhadeaprendizadoapims.learning.model.StatusProgresso;
import br.edu.ifg.trilhadeaprendizadoapims.learning.repository.ProgressoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ProgressoServiceTests {

    @Mock
    private ProgressoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UsuarioConquistaService usuarioConquistaService;

    @Spy
    @InjectMocks
    private ProgressoService service;

    private EProgresso progresso;
    private ProgressoCreateDto createDto;
    private ProgressoDto progressoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createDto = new ProgressoCreateDto();
        createDto.setUsuarioId(1L);
        createDto.setTrilhaId(10L);

        progresso = new EProgresso();
        progresso.setId(1L);
        progresso.setUsuarioId(1L);
        progresso.setTrilhaId(10L);
        progresso.setTrilha_modulos(Arrays.asList(100L, 101L, 102L));
        progresso.setModuloAtual_id(100L);
        progresso.setStatusProgresso(StatusProgresso.EM_PROGRESSO);

        progressoDto = new ProgressoDto();
        progressoDto.setId(1L);
        progressoDto.setUsuarioId(1L);
        progressoDto.setTrilhaId(10L);
    }

    // Testa iniciar progresso com usuário válido
    @Test
    void iniciarProgresso_usuarioValido_deveSalvarEDevolverDto() {
        when(modelMapper.map(createDto, EProgresso.class)).thenReturn(progresso);
        when(service.verificaUsuarioValido(1L, "test-token")).thenReturn(true);

        List<Long> modulos = Arrays.asList(100L, 101L, 102L);
        ResponseEntity<List<Long>> responseEntity = new ResponseEntity<>(modulos, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Long>>>any()
        )).thenReturn(responseEntity);

        when(modelMapper.map(progresso, ProgressoDto.class)).thenReturn(progressoDto);


        ProgressoDto resultado = service.iniciarProgresso(createDto, "test-token");

        assertNotNull(resultado);
        assertEquals(progressoDto, resultado);
        verify(repository).save(progresso);
        assertEquals(StatusProgresso.EM_PROGRESSO, progresso.getStatusProgresso());
        assertEquals(0, progresso.getXpGanho());
        assertEquals(100L, progresso.getModuloAtual_id());
    }


    // Testa iniciar progresso com usuário inválido
    @Test
    void iniciarProgresso_usuarioInvalido_deveLancarBadRequest() {
        when(modelMapper.map(createDto, EProgresso.class)).thenReturn(progresso);

        when(service.verificaUsuarioValido(1L, "test-token")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.iniciarProgresso(createDto, "test-token"));

        assertEquals("Usuário inválido ou inexistente.", ex.getReason());
    }

    // Testa concluir trilha existente
    @Test
    void concluirTrilha_existente_deveConcluirEAtualizarXp() {
        when(repository.findById(1L)).thenReturn(Optional.of(progresso));

        ConquistaDto conquistaDto = new ConquistaDto();
        conquistaDto.setId(999L);

        when(service.buscarConquista(progresso.getTrilhaId(), "TRILHA", "test-token")).thenReturn(conquistaDto);

        when(usuarioConquistaService.gerarConquista(any())).thenReturn(new UsuarioConquistaDto());
        when(modelMapper.map(progresso, ProgressoDto.class)).thenReturn(progressoDto);

        int xp = 500;

        doReturn(xp).when(service).geraConquista(conquistaDto, progresso.getUsuarioId(), "test-token");

        ProgressoDto resultado = service.concluirTrilha(1L, "test-token");


        assertNotNull(resultado);
        verify(repository).save(progresso);
        assertEquals(StatusProgresso.CONCLUIDO, progresso.getStatusProgresso());
    }

    // Testa concluir trilha não encontrada
    @Test
    void concluirTrilha_inexistente_deveLancarEntityNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());


        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.concluirTrilha(1L, "test-token"));

        assertEquals("Progresso não encontrado", ex.getMessage());
    }

    // Testa concluir módulo com progresso concluído (deve lançar BadRequest)
    @Test
    void concluirModulo_progressoConcluido_deveLancarBadRequest() {
        progresso.setStatusProgresso(StatusProgresso.CONCLUIDO);
        when(repository.findById(1L)).thenReturn(Optional.of(progresso));


        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.concluirModulo(1L, "test-token"));
        assertEquals("Progresso já concluído. Não é possível concluir novo módulo.", ex.getReason());
    }

    // Testa concluir módulo normalmente avançando para próximo módulo
    @Test
    void concluirModulo_avancaParaProximoModulo() {
        progresso.setTrilha_modulos(Arrays.asList(100L, 101L, 102L));
        progresso.setModuloAtual_id(100L);
        progresso.setStatusProgresso(StatusProgresso.EM_PROGRESSO);
        when(repository.findById(1L)).thenReturn(Optional.of(progresso));

        ConquistaDto conquistaModulo = new ConquistaDto();
        conquistaModulo.setId(123L);

        when(service.buscarConquista(progresso.getModuloAtual_id(), "MODULO", "test-token")).thenReturn(conquistaModulo);
        doReturn(50).when(service).geraConquista(conquistaModulo, progresso.getUsuarioId(), "test-token");

        when(modelMapper.map(progresso, ProgressoDto.class)).thenReturn(progressoDto);

        ProgressoDto resultado = service.concluirModulo(1L, "test-token");


        assertNotNull(resultado);
        verify(repository, times(1)).save(progresso);
        assertEquals(101L, progresso.getModuloAtual_id());
    }

    // Testa concluir módulo quando não há próximo módulo (deve concluir trilha)
    @Test
    void concluirModulo_semProximoModulo_deveConcluirTrilha() {
        progresso.setTrilha_modulos(Arrays.asList(100L));
        progresso.setModuloAtual_id(100L);
        progresso.setStatusProgresso(StatusProgresso.EM_PROGRESSO);
        when(repository.findById(1L)).thenReturn(Optional.of(progresso));

        ConquistaDto conquistaModulo = new ConquistaDto();
        conquistaModulo.setId(123L);

        when(service.buscarConquista(progresso.getModuloAtual_id(), "MODULO", "test-token")).thenReturn(conquistaModulo);
        doReturn(50).when(service).geraConquista(conquistaModulo, progresso.getUsuarioId(), "test-token");


        doAnswer(invocation -> {
            progresso.setStatusProgresso(StatusProgresso.CONCLUIDO);
            return new ProgressoDto();

        }).when(service).concluirTrilha(progresso.getId(), "test-token");

        ProgressoDto resultado = service.concluirModulo(1L, "test-token");

        assertNotNull(resultado);
        verify(repository, atLeastOnce()).save(progresso);
        verify(service).concluirTrilha(progresso.getId(), "test-token");

        assertEquals(StatusProgresso.CONCLUIDO, progresso.getStatusProgresso());
    }

    // Testa obter progresso existente
    @Test
    void obterProgresso_existente_deveRetornarDto() {
        when(repository.findByUsuarioIdAndTrilhaId(1L, 10L)).thenReturn(Optional.of(progresso));
        when(modelMapper.map(progresso, ProgressoDto.class)).thenReturn(progressoDto);

        ProgressoDto resultado = service.obterProgresso(1L, 10L);

        assertNotNull(resultado);
        assertEquals(progressoDto, resultado);
    }

    // Testa obter progresso inexistente
    @Test
    void obterProgresso_inexistente_deveLancarEntityNotFound() {
        when(repository.findByUsuarioIdAndTrilhaId(1L, 10L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.obterProgresso(1L, 10L));
        assertEquals("Progresso não encontrado", ex.getMessage());
    }

    // Testa excluir progresso existente
    @Test
    void excluirProgresso_existente_deveChamarDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(progresso));

        service.excluirProgresso(1L);

        verify(repository).delete(progresso);
    }

    // Testa excluir progresso inexistente
    @Test
    void excluirProgresso_inexistente_deveLancarEntityNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.excluirProgresso(1L));
        assertEquals("Progresso não encontrado", ex.getMessage());
    }

    // Testa geraConquista com dados válidos e tipo TRILHA
    @Test
    void geraConquista_tipoTrilha_deveChamarUsuarioConquista() {
        ConquistaDto conquista = new ConquistaDto();
        conquista.setId(1L);
        conquista.setNome("Conquista Trilha");
        conquista.setTipo("TRILHA");
        conquista.setDescricao("desc");
        conquista.setXpGanho(100);
        conquista.setTrilha_nome("Trilha X");

        UsuarioConquistaDto usuarioDto = new UsuarioConquistaDto();
        when(usuarioConquistaService.gerarConquista(any())).thenReturn(usuarioDto);


        int xp = service.geraConquista(conquista, 1L, "test-token");

        assertEquals(100, xp);
        verify(usuarioConquistaService).gerarConquista(any());
    }

    // Testa geraConquista com dados inválidos (null)
    @Test
    void geraConquista_conquistaNull_deveLancarBadRequest() {

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.geraConquista(null, 1L, "test-token"));

        assertEquals("Dados da conquista não encontrados", ex.getReason());
    }

    // Testa geraConquista com tipo inválido
    @Test
    void geraConquista_tipoInvalido_deveLancarBadRequest() {
        ConquistaDto conquista = new ConquistaDto();
        conquista.setTipo("INVALIDO");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.geraConquista(conquista, 1L, "test-token"));

        assertTrue(ex.getReason().contains("Tipo de conquista inválido"));
    }

    // Testa buscarConquista com tipo TRILHA e retorno OK
    @Test
    void buscarConquista_tipoTrilha_deveRetornarDto() {
        ConquistaDto conquista = new ConquistaDto();
        when(restTemplate.getForObject(anyString(), eq(ConquistaDto.class))).thenReturn(conquista);

        ConquistaDto resultado = service.buscarConquista(1L, "TRILHA", "test-token");


        assertNotNull(resultado);
    }

    // Testa buscarConquista tipo inválido
    @Test
    void buscarConquista_tipoInvalido_deveLancarBadRequest() {

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscarConquista(1L, "INVALIDO", "test-token"));

        assertTrue(ex.getReason().contains("Tipo de conquista inválido"));
    }

    // Testa buscarModulosPorTrilha com retorno válido
    @Test
    void buscarModulosPorTrilha_deveRetornarLista() {
        List<Long> modulos = Arrays.asList(100L, 101L);
        ResponseEntity<List<Long>> responseEntity = ResponseEntity.ok(modulos);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        List<Long> resultado = service.buscarModulosPorTrilha(10L, "test-token");


        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    // Testa buscarModulosPorTrilha com lista vazia (deve lançar NOT_FOUND)
    @Test
    void buscarModulosPorTrilha_listaVazia_deveLancarNotFound() {
        ResponseEntity<List<Long>> responseEntity = ResponseEntity.ok(List.of());

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);


        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscarModulosPorTrilha(10L, "test-token"));

        assertEquals("Nenhum módulo encontrado para a trilha", ex.getReason());
    }

    // Testa verificaUsuarioValido true
    @Test
    void verificaUsuarioValido_true() {
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());

        Boolean valido = service.verificaUsuarioValido(1L, "test-token");


        assertTrue(valido);
    }

    // Testa verificaUsuarioValido false
    @Test
    void verificaUsuarioValido_false() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND,
                        "Not Found",
                        HttpHeaders.EMPTY,
                        null,
                        null
                ));


        Boolean valido = service.verificaUsuarioValido(1L, "test-token");


        assertFalse(valido);
    }

}

