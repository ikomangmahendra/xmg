package com.xtramile.xmgapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerMapperTest {

    private ConsumerMapper consumerMapper;

    @BeforeEach
    public void setUp() {
        consumerMapper = new ConsumerMapperImpl();
    }
}
