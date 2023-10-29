package com.example.Slearning.Backend.Java.domain.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class GenericMapper<E, D> {
    private Class<D> dtoClass;
    private Class<E> entityClass;
    @Autowired
    private ModelMapper modelMapper;

    public <E> E toEntity(Class<D> dto, Class<E> entity) {
        return modelMapper.map(dto, entity);
    }

    public <D> D toDto(Class<E> entity, Class<D> dto) {
        return modelMapper.map(entity, dto);
    }

    public Collection<E> toEntities(Collection<Class<D>> dtoCollection, Class<E> entityClass) {
        return dtoCollection.stream().map(c -> modelMapper.map(c, entityClass)).collect(Collectors.toList());
    }

    public Collection<D> toDtoCollection(Collection<E> entities) {
        return entities.stream().map(e -> modelMapper.map(e, dtoClass)).collect(Collectors.toList());
    }
}
