package com.cln.myapp.web.rest;

import com.cln.myapp.ClnTestApp;
import com.cln.myapp.domain.Profil;
import com.cln.myapp.repository.ProfilRepository;
import com.cln.myapp.repository.search.ProfilSearchRepository;
import com.cln.myapp.service.ProfilService;
import com.cln.myapp.service.dto.ProfilDTO;
import com.cln.myapp.service.mapper.ProfilMapper;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProfilResource} REST controller.
 */
@SpringBootTest(classes = ClnTestApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfilResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private ProfilService profilService;

    /**
     * This repository is mocked in the com.cln.myapp.repository.search test package.
     *
     * @see com.cln.myapp.repository.search.ProfilSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfilSearchRepository mockProfilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilMockMvc;

    private Profil profil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createEntity(EntityManager em) {
        Profil profil = new Profil()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .birthDay(DEFAULT_BIRTH_DAY);
        return profil;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createUpdatedEntity(EntityManager em) {
        Profil profil = new Profil()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDay(UPDATED_BIRTH_DAY);
        return profil;
    }

    @BeforeEach
    public void initTest() {
        profil = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfil() throws Exception {
        int databaseSizeBeforeCreate = profilRepository.findAll().size();
        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);
        restProfilMockMvc.perform(post("/api/profils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isCreated());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate + 1);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testProfil.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testProfil.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfil.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProfil.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);

        // Validate the Profil in Elasticsearch
        verify(mockProfilSearchRepository, times(1)).save(testProfil);
    }

    @Test
    @Transactional
    public void createProfilWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profilRepository.findAll().size();

        // Create the Profil with an existing ID
        profil.setId(1L);
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilMockMvc.perform(post("/api/profils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate);

        // Validate the Profil in Elasticsearch
        verify(mockProfilSearchRepository, times(0)).save(profil);
    }


    @Test
    @Transactional
    public void getAllProfils() throws Exception {
        // Initialize the database
        profilRepository.saveAndFlush(profil);

        // Get all the profilList
        restProfilMockMvc.perform(get("/api/profils?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profil.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())));
    }
    
    @Test
    @Transactional
    public void getProfil() throws Exception {
        // Initialize the database
        profilRepository.saveAndFlush(profil);

        // Get the profil
        restProfilMockMvc.perform(get("/api/profils/{id}", profil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profil.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProfil() throws Exception {
        // Get the profil
        restProfilMockMvc.perform(get("/api/profils/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfil() throws Exception {
        // Initialize the database
        profilRepository.saveAndFlush(profil);

        int databaseSizeBeforeUpdate = profilRepository.findAll().size();

        // Update the profil
        Profil updatedProfil = profilRepository.findById(profil.getId()).get();
        // Disconnect from session so that the updates on updatedProfil are not directly saved in db
        em.detach(updatedProfil);
        updatedProfil
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDay(UPDATED_BIRTH_DAY);
        ProfilDTO profilDTO = profilMapper.toDto(updatedProfil);

        restProfilMockMvc.perform(put("/api/profils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isOk());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProfil.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testProfil.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfil.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProfil.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);

        // Validate the Profil in Elasticsearch
        verify(mockProfilSearchRepository, times(1)).save(testProfil);
    }

    @Test
    @Transactional
    public void updateNonExistingProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc.perform(put("/api/profils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Profil in Elasticsearch
        verify(mockProfilSearchRepository, times(0)).save(profil);
    }

    @Test
    @Transactional
    public void deleteProfil() throws Exception {
        // Initialize the database
        profilRepository.saveAndFlush(profil);

        int databaseSizeBeforeDelete = profilRepository.findAll().size();

        // Delete the profil
        restProfilMockMvc.perform(delete("/api/profils/{id}", profil.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Profil in Elasticsearch
        verify(mockProfilSearchRepository, times(1)).deleteById(profil.getId());
    }

    @Test
    @Transactional
    public void searchProfil() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        profilRepository.saveAndFlush(profil);
        when(mockProfilSearchRepository.search(queryStringQuery("id:" + profil.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(profil), PageRequest.of(0, 1), 1));

        // Search the profil
        restProfilMockMvc.perform(get("/api/_search/profils?query=id:" + profil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profil.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())));
    }
}
