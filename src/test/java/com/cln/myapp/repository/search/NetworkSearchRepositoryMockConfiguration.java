package com.cln.myapp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link NetworkSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NetworkSearchRepositoryMockConfiguration {

    @MockBean
    private NetworkSearchRepository mockNetworkSearchRepository;

}
