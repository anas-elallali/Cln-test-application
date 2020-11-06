package com.cln.myapp.repository.search;

import com.cln.myapp.domain.Profil;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Profil} entity.
 */
public interface ProfilSearchRepository extends ElasticsearchRepository<Profil, Long> {
}
