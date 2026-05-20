package br.edu.ifg.trilhadeaprendizagemapims.trilha.services;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.CategoriaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<CategoriaDTO> listarCategorias(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(entidade -> modelMapper.map(entidade, CategoriaDTO.class));
    }

    public CategoriaDTO obterCategoriaPorId(Long id){
        return modelMapper.map(categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada")), CategoriaDTO.class);
    }

    public CategoriaDTO cadastrarCategoria(CategoriaDTO dto) {
        ECategoria entidade = modelMapper.map(dto, ECategoria.class);
        categoriaRepository.save(entidade);
        return modelMapper.map(entidade, CategoriaDTO.class);
    }

    public void deletarCategoria(Long id) {
        ECategoria entidade = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        categoriaRepository.delete(entidade);
    }

    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO dto) {
        ECategoria entidade = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        modelMapper.map(dto, entidade);
        categoriaRepository.save(entidade);
        return modelMapper.map(entidade, CategoriaDTO.class);
    }
}
