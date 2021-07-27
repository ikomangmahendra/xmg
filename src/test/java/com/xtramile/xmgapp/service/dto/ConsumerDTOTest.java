package com.xtramile.xmgapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.xtramile.xmgapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumerDTO.class);
        ConsumerDTO consumerDTO1 = new ConsumerDTO();
        consumerDTO1.setId(1L);
        ConsumerDTO consumerDTO2 = new ConsumerDTO();
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
        consumerDTO2.setId(consumerDTO1.getId());
        assertThat(consumerDTO1).isEqualTo(consumerDTO2);
        consumerDTO2.setId(2L);
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
        consumerDTO1.setId(null);
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
    }
}
