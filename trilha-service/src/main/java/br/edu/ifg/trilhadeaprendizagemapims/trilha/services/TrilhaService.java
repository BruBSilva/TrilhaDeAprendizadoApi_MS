package br.edu.ifg.trilhadeaprendizagemapims.trilha.services;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDetalhadaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ModuloDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.TrilhaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EModulo;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ETrilha;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.CategoriaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.ConquistaRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.ModuloRepository;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.TrilhaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrilhaService {

    @Autowired
    TrilhaRepository trilhaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ConquistaRepository conquistaRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ModuloRepository moduloRepository;

    @Transactional
    public Page<TrilhaDTO> listarTrilhas(Pageable pageable) {
        return trilhaRepository.findAll(pageable)
                .map(entidade -> modelMapper.map(entidade, TrilhaDTO.class));
    }

    @Transactional
    public TrilhaDTO obterTrilhaPorId(Long id) {
        ETrilha entidade = trilhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));
        return modelMapper.map(entidade, TrilhaDTO.class);
    }

    public TrilhaDTO cadastrarTrilha(TrilhaDTO dto) {
        ETrilha entidade = modelMapper.map(dto, ETrilha.class);

        ECategoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        entidade.setCategoria(categoria);

        if (entidade.getModulos() != null) {
            for (EModulo modulo : entidade.getModulos()) {
                modulo.setTrilha(entidade);
                modulo.getConquista().setModulo(modulo);
            }
        }
        entidade.getConquista().setTrilha(entidade);

        trilhaRepository.save(entidade);
        return modelMapper.map(entidade, TrilhaDTO.class);
    }

    @Transactional
    public TrilhaDTO atualizarTrilha(Long id, TrilhaDTO dto) {
        ETrilha entidade = trilhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));

        ECategoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        entidade.setCategoria(categoria);

        EConquista conquista = conquistaRepository.findById(dto.getConquista().getId())
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada"));
        conquista.setNome(dto.getConquista().getNome());
        conquista.setDescricao(dto.getConquista().getDescricao());
        conquista.setXpGanho(dto.getConquista().getXpGanho());
        conquista.setTipo(TipoConquista.TRILHA);
        entidade.setConquista(conquista);

        entidade.getModulos().clear();

        for (ModuloDTO moduloDTO : dto.getModulos()) {
            EModulo modulo = new EModulo();
            modulo.setTitulo(moduloDTO.getTitulo());
            modulo.setConteudo(moduloDTO.getConteudo());
            modulo.setOrdem(moduloDTO.getOrdem());
            modulo.setTrilha(entidade);

            if (moduloDTO.getConquista() != null) {
                EConquista novaConquistaModulo = new EConquista();
                novaConquistaModulo.setNome(moduloDTO.getConquista().getNome());
                novaConquistaModulo.setDescricao(moduloDTO.getConquista().getDescricao());
                novaConquistaModulo.setXpGanho(moduloDTO.getConquista().getXpGanho());
                novaConquistaModulo.setModulo(modulo);
                modulo.setConquista(novaConquistaModulo);
            }
            
            moduloRepository.save(modulo);
            entidade.getModulos().add(modulo);
        }
        trilhaRepository.save(entidade);
        return modelMapper.map(entidade, TrilhaDTO.class);
    }

    public void deletarTrilha(Long id) {
        ETrilha entidade = trilhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));
        trilhaRepository.delete(entidade);
    }

    @Transactional
    public List<Long> getModulosIds(Long id) {
        ETrilha entidade = trilhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));
        return entidade.getModulos().stream().map(EModulo::getId).collect(Collectors.toList());
    }

    public ConquistaDetalhadaDTO obterConquistaPorTrilhaId(Long id){
        ETrilha entidade = trilhaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));
        ConquistaDetalhadaDTO dto = modelMapper.map(entidade.getConquista(), ConquistaDetalhadaDTO.class);
        dto.setTrilha_nome(entidade.getNome());

        return dto;
    }

    public ConquistaDetalhadaDTO obterConquistaPorModuloId(Long id){
        EModulo entidade = moduloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado"));
        ConquistaDetalhadaDTO dto = modelMapper.map(entidade.getConquista(), ConquistaDetalhadaDTO.class);
        dto.setModulo_nome(entidade.getTitulo());

        return dto;
    }

}
