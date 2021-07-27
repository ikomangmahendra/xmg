package com.xtramile.xmgapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.xtramile.xmgapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consumer.class);
        Consumer consumer1 = new Consumer();
        consumer1.setId(1L);
        Consumer consumer2 = new Consumer();
        consumer2.setId(consumer1.getId());
        assertThat(consumer1).isEqualTo(consumer2);
        consumer2.setId(2L);
        assertThat(consumer1).isNotEqualTo(consumer2);
        consumer1.setId(null);
        assertThat(consumer1).isNotEqualTo(consumer2);
    }
}
