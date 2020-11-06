package com.cln.myapp.repository.search;

import com.cln.myapp.domain.Network;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Network} entity.
 */
public interface NetworkSearchRepository extends ElasticsearchRepository<Network, Long> {
}
