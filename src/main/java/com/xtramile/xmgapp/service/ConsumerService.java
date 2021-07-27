package com.xtramile.xmgapp.service;

import com.xtramile.xmgapp.domain.Consumer;
import com.xtramile.xmgapp.repository.ConsumerRepository;
import com.xtramile.xmgapp.service.dto.ConsumerDTO;
import com.xtramile.xmgapp.service.mapper.ConsumerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Consumer}.
 */
@Service
@Transactional
public class ConsumerService {

    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    private final ConsumerRepository consumerRepository;

    private final ConsumerMapper consumerMapper;

    public ConsumerService(ConsumerRepository consumerRepository, ConsumerMapper consumerMapper) {
        this.consumerRepository = consumerRepository;
        this.consumerMapper = consumerMapper;
    }

    /**
     * Save a consumer.
     *
     * @param consumerDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsumerDTO save(ConsumerDTO consumerDTO) {
        log.debug("Request to save Consumer : {}", consumerDTO);
        Consumer consumer = consumerMapper.toEntity(consumerDTO);
        consumer = consumerRepository.save(consumer);
        return consumerMapper.toDto(consumer);
    }

    /**
     * Partially update a consumer.
     *
     * @param consumerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsumerDTO> partialUpdate(ConsumerDTO consumerDTO) {
        log.debug("Request to partially update Consumer : {}", consumerDTO);

        return consumerRepository
            .findById(consumerDTO.getId())
            .map(
                existingConsumer -> {
                    consumerMapper.partialUpdate(existingConsumer, consumerDTO);

                    return existingConsumer;
                }
            )
            .map(consumerRepository::save)
            .map(consumerMapper::toDto);
    }

    /**
     * Get all the consumers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Consumers");
        return consumerRepository.findAll(pageable).map(consumerMapper::toDto);
    }

    /**
     * Get one consumer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsumerDTO> findOne(Long id) {
        log.debug("Request to get Consumer : {}", id);
        return consumerRepository.findById(id).map(consumerMapper::toDto);
    }

    /**
     * Delete the consumer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consumer : {}", id);
        consumerRepository.deleteById(id);
    }
}
