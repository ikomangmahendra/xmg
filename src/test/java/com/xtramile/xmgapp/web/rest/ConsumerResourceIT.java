package com.xtramile.xmgapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xtramile.xmgapp.IntegrationTest;
import com.xtramile.xmgapp.domain.Consumer;
import com.xtramile.xmgapp.repository.ConsumerRepository;
import com.xtramile.xmgapp.service.dto.ConsumerDTO;
import com.xtramile.xmgapp.service.mapper.ConsumerMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConsumerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumerResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RECORD_STATUS_ID = 1;
    private static final Integer UPDATED_RECORD_STATUS_ID = 2;

    private static final String ENTITY_API_URL = "/api/consumers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumerMockMvc;

    private Consumer consumer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .guid(DEFAULT_GUID)
            .note(DEFAULT_NOTE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .recordStatusId(DEFAULT_RECORD_STATUS_ID);
        return consumer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createUpdatedEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .guid(UPDATED_GUID)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .recordStatusId(UPDATED_RECORD_STATUS_ID);
        return consumer;
    }

    @BeforeEach
    public void initTest() {
        consumer = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumer() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();
        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);
        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isCreated());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate + 1);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testConsumer.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testConsumer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConsumer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testConsumer.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testConsumer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testConsumer.getRecordStatusId()).isEqualTo(DEFAULT_RECORD_STATUS_ID);
    }

    @Test
    @Transactional
    void createConsumerWithExistingId() throws Exception {
        // Create the Consumer with an existing ID
        consumer.setId(1L);
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        int databaseSizeBeforeCreate = consumerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerRepository.findAll().size();
        // set the field null
        consumer.setGuid(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordStatusIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerRepository.findAll().size();
        // set the field null
        consumer.setRecordStatusId(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsumers() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get all the consumerList
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordStatusId").value(hasItem(DEFAULT_RECORD_STATUS_ID)));
    }

    @Test
    @Transactional
    void getConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get the consumer
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL_ID, consumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumer.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.recordStatusId").value(DEFAULT_RECORD_STATUS_ID));
    }

    @Test
    @Transactional
    void getNonExistingConsumer() throws Exception {
        // Get the consumer
        restConsumerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer
        Consumer updatedConsumer = consumerRepository.findById(consumer.getId()).get();
        // Disconnect from session so that the updates on updatedConsumer are not directly saved in db
        em.detach(updatedConsumer);
        updatedConsumer
            .guid(UPDATED_GUID)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .recordStatusId(UPDATED_RECORD_STATUS_ID);
        ConsumerDTO consumerDTO = consumerMapper.toDto(updatedConsumer);

        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testConsumer.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testConsumer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConsumer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testConsumer.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testConsumer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testConsumer.getRecordStatusId()).isEqualTo(UPDATED_RECORD_STATUS_ID);
    }

    @Test
    @Transactional
    void putNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        partialUpdatedConsumer.guid(UPDATED_GUID).note(UPDATED_NOTE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testConsumer.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testConsumer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConsumer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testConsumer.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testConsumer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testConsumer.getRecordStatusId()).isEqualTo(DEFAULT_RECORD_STATUS_ID);
    }

    @Test
    @Transactional
    void fullUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        partialUpdatedConsumer
            .guid(UPDATED_GUID)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .recordStatusId(UPDATED_RECORD_STATUS_ID);

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testConsumer.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testConsumer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConsumer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testConsumer.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testConsumer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testConsumer.getRecordStatusId()).isEqualTo(UPDATED_RECORD_STATUS_ID);
    }

    @Test
    @Transactional
    void patchNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeDelete = consumerRepository.findAll().size();

        // Delete the consumer
        restConsumerMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
