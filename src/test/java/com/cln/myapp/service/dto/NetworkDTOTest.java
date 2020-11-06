package com.cln.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cln.myapp.web.rest.TestUtil;

public class NetworkDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetworkDTO.class);
        NetworkDTO networkDTO1 = new NetworkDTO();
        networkDTO1.setId(1L);
        NetworkDTO networkDTO2 = new NetworkDTO();
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
        networkDTO2.setId(networkDTO1.getId());
        assertThat(networkDTO1).isEqualTo(networkDTO2);
        networkDTO2.setId(2L);
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
        networkDTO1.setId(null);
        assertThat(networkDTO1).isNotEqualTo(networkDTO2);
    }
}
