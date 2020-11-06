package com.cln.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cln.myapp.web.rest.TestUtil;

public class NetworkTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Network.class);
        Network network1 = new Network();
        network1.setId(1L);
        Network network2 = new Network();
        network2.setId(network1.getId());
        assertThat(network1).isEqualTo(network2);
        network2.setId(2L);
        assertThat(network1).isNotEqualTo(network2);
        network1.setId(null);
        assertThat(network1).isNotEqualTo(network2);
    }
}
