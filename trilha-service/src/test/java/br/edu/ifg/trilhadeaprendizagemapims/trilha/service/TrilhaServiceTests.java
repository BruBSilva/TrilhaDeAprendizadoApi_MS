package br.edu.ifg.trilhadeaprendizagemapims.trilha.service;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.*;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EModulo;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ETrilha;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.CategoriaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.ConquistaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.ModuloRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.TrilhaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.services.TrilhaService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrilhaServiceTests {

    @InjectMocks
    private TrilhaService trilhaService;

    @Mock
    private TrilhaRepository trilhaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private ConquistaRepository conquistaRepository;
    @Mock
    private ModuloRepository moduloRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTrilhas_deveRetornarPaginaDeTrilhas() {
        ETrilha trilha = new ETrilha();
        TrilhaDTO trilhaDTO = new TrilhaDTO();
        Pageable pageable = PageRequest.of(0, 10);

        when(trilhaRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(trilha)));
        when(modelMapper.map(trilha, TrilhaDTO.class)).thenReturn(trilhaDTO);

        Page<TrilhaDTO> resultado = trilhaService.listarTrilhas(pageable);

        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void obterTrilhaPorId_existente() {
        ETrilha trilha = new ETrilha();
        TrilhaDTO trilhaDTO = new TrilhaDTO();

        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilha));
        when(modelMapper.map(trilha, TrilhaDTO.class)).thenReturn(trilhaDTO);

        TrilhaDTO resultado = trilhaService.obterTrilhaPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void obterTrilhaPorId_naoEncontrada() {
        when(trilhaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trilhaService.obterTrilhaPorId(1L));
    }

    @Test
    void deletarTrilha_existente() {
        ETrilha trilha = new ETrilha();
        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilha));
        trilhaService.deletarTrilha(1L);
        verify(trilhaRepository).delete(trilha);
    }

    @Test
    void deletarTrilha_naoEncontrada() {
        when(trilhaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trilhaService.deletarTrilha(1L));
    }

    @Test
    void getModulosIds_deveRetornarListaDeIds() {
        EModulo modulo = new EModulo();
        modulo.setId(10L);
        ETrilha trilha = new ETrilha();
        trilha.setModulos(List.of(modulo));

        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilha));

        List<Long> ids = trilhaService.getModulosIds(1L);

        assertEquals(List.of(10L), ids);
    }

    @Test
    void obterConquistaPorTrilhaId_deveRetornarDTO() {
        EConquista conquista = new EConquista();
        conquista.setNome("Conquista");
        ETrilha trilha = new ETrilha();
        trilha.setNome("Trilha Teste");
        trilha.setConquista(conquista);

        ConquistaDetalhadaDTO dto = new ConquistaDetalhadaDTO();
        dto.setNome("Conquista");
        dto.setTrilha_nome("Trilha Teste");

        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilha));
        when(modelMapper.map(conquista, ConquistaDetalhadaDTO.class)).thenReturn(dto);

        ConquistaDetalhadaDTO resultado = trilhaService.obterConquistaPorTrilhaId(1L);

        assertEquals("Trilha Teste", resultado.getTrilha_nome());
    }

    @Test
    void obterConquistaPorModuloId_deveRetornarDTO() {
        EConquista conquista = new EConquista();
        conquista.setNome("Conquista Módulo");
        EModulo modulo = new EModulo();
        modulo.setTitulo("Módulo 1");
        modulo.setConquista(conquista);

        ConquistaDetalhadaDTO dto = new ConquistaDetalhadaDTO();
        dto.setNome("Conquista Módulo");
        dto.setModulo_nome("Módulo 1");

        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));
        when(modelMapper.map(conquista, ConquistaDetalhadaDTO.class)).thenReturn(dto);

        ConquistaDetalhadaDTO resultado = trilhaService.obterConquistaPorModuloId(1L);

        assertEquals("Módulo 1", resultado.getModulo_nome());
    }

    @Test
    void cadastrarTrilha_deveSalvarTrilhaComDadosCorretos() {

        // DTOs
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNome("Back-End");
        categoriaDTO.setDescricao("Categoria de back-end");

        ConquistaDTO conquistaDTO = new ConquistaDTO();
        conquistaDTO.setId(1L);
        conquistaDTO.setNome("Primeira Trilha");
        conquistaDTO.setDescricao("Conquista por completar a trilha");
        conquistaDTO.setTipo(TipoConquista.TRILHA);
        conquistaDTO.setXpGanho(100);

        ModuloDTO moduloDTO = new ModuloDTO();
        moduloDTO.setId(1L);
        moduloDTO.setTitulo("Introdução");
        moduloDTO.setTrilhaId(1L);
        moduloDTO.setOrdem(1);
        moduloDTO.setConteudo("Conteúdo inicial");
        moduloDTO.setConquista(conquistaDTO);

        TrilhaDTO trilhaDTO = new TrilhaDTO();
        trilhaDTO.setId(1L);
        trilhaDTO.setNome("Trilha Java");
        trilhaDTO.setDescricao("Aprendizado Java");
        trilhaDTO.setCategoria(categoriaDTO);
        trilhaDTO.setConquista(conquistaDTO);
        trilhaDTO.setModulos(new ArrayList<>(List.of(moduloDTO)));

        // ENTIDADES simuladas para mock
        ECategoria categoria = new ECategoria();
        categoria.setId(1L);
        categoria.setNome("Back-End");
        categoria.setDescricao("Categoria de back-end");

        EConquista conquista = new EConquista();
        conquista.setId(1L);
        conquista.setNome("Primeira Trilha");
        conquista.setDescricao("Conquista por completar a trilha");
        conquista.setTipo(TipoConquista.TRILHA);
        conquista.setXpGanho(100);

        EModulo modulo = new EModulo();
        modulo.setId(1L);
        modulo.setTitulo("Introdução");
        modulo.setOrdem(1);
        modulo.setConteudo("Conteúdo inicial");
        modulo.setConquista(conquista);

        ETrilha trilhaEntity = new ETrilha();
        trilhaEntity.setId(1L);
        trilhaEntity.setNome("Trilha Java");
        trilhaEntity.setDescricao("Aprendizado Java");
        trilhaEntity.setCategoria(categoria);
        trilhaEntity.setConquista(conquista);
        trilhaEntity.setModulos(List.of(modulo));

        // mocks
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(conquistaRepository.findById(1L)).thenReturn(Optional.of(conquista));
        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));

        when(modelMapper.map(trilhaDTO, ETrilha.class)).thenReturn(trilhaEntity);
        when(modelMapper.map(trilhaEntity, TrilhaDTO.class)).thenReturn(trilhaDTO);

        // execução
        TrilhaDTO resultado = trilhaService.cadastrarTrilha(trilhaDTO);

        // verificação
        assertNotNull(resultado);
        verify(trilhaRepository).save(trilhaEntity);
    }

    @Test
    void cadastrarTrilha_deveLancarEntityNotFoundException_quandoCategoriaNaoExistir() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(999L);  // inexistente
        TrilhaDTO trilhaDTO = new TrilhaDTO();
        trilhaDTO.setCategoria(categoriaDTO);

        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trilhaService.cadastrarTrilha(trilhaDTO));
    }

    @Test
    void atualizarTrilha_deveAtualizarTrilhaExistente() {
        TrilhaDTO trilhaDTO = new TrilhaDTO();
        trilhaDTO.setNome("Nova Trilha");
        trilhaDTO.setDescricao("Nova descrição");

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        trilhaDTO.setCategoria(categoriaDTO);

        ConquistaDTO conquistaDTO = new ConquistaDTO();
        conquistaDTO.setId(2L);
        trilhaDTO.setConquista(conquistaDTO);

        ModuloDTO moduloDTO = new ModuloDTO();
        moduloDTO.setId(3L);
        trilhaDTO.setModulos(new ArrayList<>(List.of(moduloDTO)));

        ETrilha trilhaExistente = new ETrilha();
        ECategoria categoria = new ECategoria();
        EConquista conquista = new EConquista();
        EModulo modulo = new EModulo();

        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilhaExistente));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(conquistaRepository.findById(2L)).thenReturn(Optional.of(conquista));
        when(moduloRepository.findById(3L)).thenReturn(Optional.of(modulo));

        when(modelMapper.map(trilhaDTO, ETrilha.class)).thenReturn(trilhaExistente);
        when(modelMapper.map(trilhaExistente, TrilhaDTO.class)).thenReturn(trilhaDTO);

        TrilhaDTO resultado = trilhaService.atualizarTrilha(1L, trilhaDTO);

        assertNotNull(resultado);
        verify(trilhaRepository).save(trilhaExistente);
    }
}
