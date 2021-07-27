package com.xtramile.xmgapp.service.mapper;

import com.xtramile.xmgapp.domain.*;
import com.xtramile.xmgapp.service.dto.ConsumerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consumer} and its DTO {@link ConsumerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConsumerMapper extends EntityMapper<ConsumerDTO, Consumer> {}
