package br.edu.ifg.trilhadeaprendizagemapims.trilha.config;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.CategoriaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.dto.ConquistaDTO;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EConquista;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Configuracao {
    @Bean
    public ModelMapper obterModelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(CategoriaDTO.class, ECategoria.class).addMappings(m -> m.skip(ECategoria::setId));
        mapper.typeMap(ConquistaDTO.class, EConquista.class).addMappings(m -> m.skip(EConquista::setId));
        return mapper;
    }
}