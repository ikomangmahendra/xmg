package com.xtramile.xmgapp.service.mapper;

import com.xtramile.xmgapp.domain.*;
import com.xtramile.xmgapp.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {}
