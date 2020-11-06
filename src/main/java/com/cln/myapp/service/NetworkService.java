package com.cln.myapp.service;

import com.cln.myapp.domain.Network;
import com.cln.myapp.repository.NetworkRepository;
import com.cln.myapp.repository.search.NetworkSearchRepository;
import com.cln.myapp.service.dto.NetworkDTO;
import com.cln.myapp.service.mapper.NetworkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Network}.
 */
@Service
@Transactional
public class NetworkService {

    private final Logger log = LoggerFactory.getLogger(NetworkService.class);

    private final NetworkRepository networkRepository;

    private final NetworkMapper networkMapper;

    private final NetworkSearchRepository networkSearchRepository;

    public NetworkService(NetworkRepository networkRepository, NetworkMapper networkMapper, NetworkSearchRepository networkSearchRepository) {
        this.networkRepository = networkRepository;
        this.networkMapper = networkMapper;
        this.networkSearchRepository = networkSearchRepository;
    }

    /**
     * Save a network.
     *
     * @param networkDTO the entity to save.
     * @return the persisted entity.
     */
    public NetworkDTO save(NetworkDTO networkDTO) {
        log.debug("Request to save Network : {}", networkDTO);
        Network network = networkMapper.toEntity(networkDTO);
        network = networkRepository.save(network);
        NetworkDTO result = networkMapper.toDto(network);
        networkSearchRepository.save(network);
        return result;
    }

    /**
     * Get all the networks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NetworkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Networks");
        return networkRepository.findAll(pageable)
            .map(networkMapper::toDto);
    }


    /**
     * Get one network by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NetworkDTO> findOne(Long id) {
        log.debug("Request to get Network : {}", id);
        return networkRepository.findById(id)
            .map(networkMapper::toDto);
    }

    /**
     * Delete the network by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Network : {}", id);
        networkRepository.deleteById(id);
        networkSearchRepository.deleteById(id);
    }

    /**
     * Search for the network corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NetworkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Networks for query {}", query);
        return networkSearchRepository.search(queryStringQuery(query), pageable)
            .map(networkMapper::toDto);
    }
}
