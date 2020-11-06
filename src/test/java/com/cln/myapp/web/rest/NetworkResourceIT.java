package com.cln.myapp.web.rest;

import com.cln.myapp.ClnTestApp;
import com.cln.myapp.domain.Network;
import com.cln.myapp.repository.NetworkRepository;
import com.cln.myapp.repository.search.NetworkSearchRepository;
import com.cln.myapp.service.NetworkService;
import com.cln.myapp.service.dto.NetworkDTO;
import com.cln.myapp.service.mapper.NetworkMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NetworkResource} REST controller.
 */
@SpringBootTest(classes = ClnTestApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class NetworkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private NetworkService networkService;

    /**
     * This repository is mocked in the com.cln.myapp.repository.search test package.
     *
     * @see com.cln.myapp.repository.search.NetworkSearchRepositoryMockConfiguration
     */
    @Autowired
    private NetworkSearchRepository mockNetworkSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetworkMockMvc;

    private Network network;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Network createEntity(EntityManager em) {
        Network network = new Network()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return network;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Network createUpdatedEntity(EntityManager em) {
        Network network = new Network()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        return network;
    }

    @BeforeEach
    public void initTest() {
        network = createEntity(em);
    }

    @Test
    @Transactional
    public void createNetwork() throws Exception {
        int databaseSizeBeforeCreate = networkRepository.findAll().size();
        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);
        restNetworkMockMvc.perform(post("/api/networks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(networkDTO)))
            .andExpect(status().isCreated());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeCreate + 1);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNetwork.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Network in Elasticsearch
        verify(mockNetworkSearchRepository, times(1)).save(testNetwork);
    }

    @Test
    @Transactional
    public void createNetworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = networkRepository.findAll().size();

        // Create the Network with an existing ID
        network.setId(1L);
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetworkMockMvc.perform(post("/api/networks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(networkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeCreate);

        // Validate the Network in Elasticsearch
        verify(mockNetworkSearchRepository, times(0)).save(network);
    }


    @Test
    @Transactional
    public void getAllNetworks() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList
        restNetworkMockMvc.perform(get("/api/networks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(network.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get the network
        restNetworkMockMvc.perform(get("/api/networks/{id}", network.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(network.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }
    @Test
    @Transactional
    public void getNonExistingNetwork() throws Exception {
        // Get the network
        restNetworkMockMvc.perform(get("/api/networks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeUpdate = networkRepository.findAll().size();

        // Update the network
        Network updatedNetwork = networkRepository.findById(network.getId()).get();
        // Disconnect from session so that the updates on updatedNetwork are not directly saved in db
        em.detach(updatedNetwork);
        updatedNetwork
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        NetworkDTO networkDTO = networkMapper.toDto(updatedNetwork);

        restNetworkMockMvc.perform(put("/api/networks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(networkDTO)))
            .andExpect(status().isOk());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNetwork.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Network in Elasticsearch
        verify(mockNetworkSearchRepository, times(1)).save(testNetwork);
    }

    @Test
    @Transactional
    public void updateNonExistingNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetworkMockMvc.perform(put("/api/networks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(networkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Network in Elasticsearch
        verify(mockNetworkSearchRepository, times(0)).save(network);
    }

    @Test
    @Transactional
    public void deleteNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeDelete = networkRepository.findAll().size();

        // Delete the network
        restNetworkMockMvc.perform(delete("/api/networks/{id}", network.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Network in Elasticsearch
        verify(mockNetworkSearchRepository, times(1)).deleteById(network.getId());
    }

    @Test
    @Transactional
    public void searchNetwork() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        networkRepository.saveAndFlush(network);
        when(mockNetworkSearchRepository.search(queryStringQuery("id:" + network.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(network), PageRequest.of(0, 1), 1));

        // Search the network
        restNetworkMockMvc.perform(get("/api/_search/networks?query=id:" + network.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(network.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
}
