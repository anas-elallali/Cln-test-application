package com.cln.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NetworkMapperTest {

    private NetworkMapper networkMapper;

    @BeforeEach
    public void setUp() {
        networkMapper = new NetworkMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(networkMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(networkMapper.fromId(null)).isNull();
    }
}
