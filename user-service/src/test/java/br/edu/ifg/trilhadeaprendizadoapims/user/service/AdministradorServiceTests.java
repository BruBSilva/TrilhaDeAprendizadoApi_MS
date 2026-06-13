package br.edu.ifg.trilhadeaprendizadoapims.user.service;

import br.edu.ifg.trilhadeaprendizadoapims.user.dto.AdministradorDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.dto.UsuarioCreateDto;
import br.edu.ifg.trilhadeaprendizadoapims.user.model.Administrador;
import br.edu.ifg.trilhadeaprendizadoapims.user.repository.AdministradorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceTests {

    @InjectMocks
    private AdministradorService administradorService;

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void deveBuscarAdministradorPorIdComSucesso() {
        Administrador admin = new Administrador();
        admin.setId(1L);
        admin.setNome("Ana");

        AdministradorDto dto = new AdministradorDto();
        dto.setId(1L);
        dto.setNome("Ana");

        Mockito.when(administradorRepository.findById(1L)).thenReturn(Optional.of(admin));
        Mockito.when(modelMapper.map(admin, AdministradorDto.class)).thenReturn(dto);

        AdministradorDto resultado = administradorService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Ana", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarAdministradorInexistente() {
        Mockito.when(administradorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> administradorService.buscarPorId(99L));
    }

    @Test
    void deveAtualizarAdministradorComSucesso() {
        Long id = 1L;

        AdministradorDto dtoEntrada = new AdministradorDto();
        dtoEntrada.setId(id);
        dtoEntrada.setNome("Atualizado");

        Administrador adminAntigo = new Administrador();
        adminAntigo.setId(id);
        adminAntigo.setSenhaHash("hash123");

        Administrador adminAtualizado = new Administrador();
        adminAtualizado.setId(id);
        adminAtualizado.setNome("Atualizado");
        adminAtualizado.setSenhaHash("hash123");

        AdministradorDto dtoSaida = new AdministradorDto();
        dtoSaida.setId(id);
        dtoSaida.setNome("Atualizado");

        Mockito.when(administradorRepository.findById(id)).thenReturn(Optional.of(adminAntigo));
        Mockito.when(modelMapper.map(dtoEntrada, Administrador.class)).thenReturn(adminAtualizado);
        Mockito.when(modelMapper.map(adminAtualizado, AdministradorDto.class)).thenReturn(dtoSaida);

        AdministradorDto resultado = administradorService.atualizar(id, dtoEntrada);

        assertEquals("Atualizado", resultado.getNome());
        Mockito.verify(administradorRepository).save(adminAtualizado);
    }

    @Test
    void deveRetornarListaPaginadaDeAdministradores() {
        Administrador admin = new Administrador();
        admin.setId(1L);
        admin.setNome("Lucas");

        AdministradorDto dto = new AdministradorDto();
        dto.setId(1L);
        dto.setNome("Lucas");

        Page<Administrador> pagina = new PageImpl<>(List.of(admin));
        Mockito.when(administradorRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagina);
        Mockito.when(modelMapper.map(admin, AdministradorDto.class)).thenReturn(dto);

        Page<AdministradorDto> resultado = administradorService.buscarTodos(PageRequest.of(0, 10));

        assertEquals(1, resultado.getContent().size());
        assertEquals("Lucas", resultado.getContent().get(0).getNome());
    }

    @Test
    void deveInserirAdministradorComSucesso() {
        UsuarioCreateDto dto = new UsuarioCreateDto();
        dto.setNome("Maria");
        dto.setEmail("maria@example.com");

        Administrador entidade = new Administrador();
        entidade.setId(1L);
        entidade.setNome("Maria");

        AdministradorDto saida = new AdministradorDto();
        saida.setId(1L);
        saida.setNome("Maria");

        Mockito.when(modelMapper.map(dto, Administrador.class)).thenReturn(entidade);
        Mockito.when(modelMapper.map(entidade, AdministradorDto.class)).thenReturn(saida);

        AdministradorDto resultado = administradorService.inserir(dto);

        assertEquals("Maria", resultado.getNome());
        Mockito.verify(administradorRepository).save(entidade);
    }

    @Test
    void deveExcluirAdministradorComSucesso() {
        Long id = 1L;

        Administrador admin = new Administrador();
        admin.setId(id);

        Mockito.when(administradorRepository.findById(id)).thenReturn(Optional.of(admin));
        Mockito.when(administradorRepository.count()).thenReturn(2L); // mais de 1 admin

        administradorService.excluir(id);

        Mockito.verify(administradorRepository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirUnicoAdministrador() {
        Long id = 1L;

        Administrador admin = new Administrador();
        admin.setId(id);

        Mockito.when(administradorRepository.findById(id)).thenReturn(Optional.of(admin));
        Mockito.when(administradorRepository.count()).thenReturn(1L); // único admin

        assertThrows(IllegalStateException.class, () -> administradorService.excluir(id));
        Mockito.verify(administradorRepository, Mockito.never()).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirAdministradorInexistente() {
        Long id = 99L;

        Mockito.when(administradorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> administradorService.excluir(id));
    }
}
