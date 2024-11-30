package com.pmrodrigues.condominio.converters;

import com.pmrodrigues.condominio.dto.PerfilDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PerfilDTOConverter implements Converter<String, PerfilDTO> {

    @Override
    public PerfilDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return new PerfilDTO(null, null, source);

    }
}
