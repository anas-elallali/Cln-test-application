package com.cln.myapp.web.rest;

import com.cln.myapp.service.NetworkService;
import com.cln.myapp.web.rest.errors.BadRequestAlertException;
import com.cln.myapp.service.dto.NetworkDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cln.myapp.domain.Network}.
 */
@RestController
@RequestMapping("/api")
public class NetworkResource {

    private final Logger log = LoggerFactory.getLogger(NetworkResource.class);

    private static final String ENTITY_NAME = "network";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NetworkService networkService;

    public NetworkResource(NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * {@code POST  /networks} : Create a new network.
     *
     * @param networkDTO the networkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new networkDTO, or with status {@code 400 (Bad Request)} if the network has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/networks")
    public ResponseEntity<NetworkDTO> createNetwork(@RequestBody NetworkDTO networkDTO) throws URISyntaxException {
        log.debug("REST request to save Network : {}", networkDTO);
        if (networkDTO.getId() != null) {
            throw new BadRequestAlertException("A new network cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NetworkDTO result = networkService.save(networkDTO);
        return ResponseEntity.created(new URI("/api/networks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /networks} : Updates an existing network.
     *
     * @param networkDTO the networkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated networkDTO,
     * or with status {@code 400 (Bad Request)} if the networkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the networkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/networks")
    public ResponseEntity<NetworkDTO> updateNetwork(@RequestBody NetworkDTO networkDTO) throws URISyntaxException {
        log.debug("REST request to update Network : {}", networkDTO);
        if (networkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NetworkDTO result = networkService.save(networkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, networkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /networks} : get all the networks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of networks in body.
     */
    @GetMapping("/networks")
    public ResponseEntity<List<NetworkDTO>> getAllNetworks(Pageable pageable) {
        log.debug("REST request to get a page of Networks");
        Page<NetworkDTO> page = networkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /networks/:id} : get the "id" network.
     *
     * @param id the id of the networkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the networkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/networks/{id}")
    public ResponseEntity<NetworkDTO> getNetwork(@PathVariable Long id) {
        log.debug("REST request to get Network : {}", id);
        Optional<NetworkDTO> networkDTO = networkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(networkDTO);
    }

    /**
     * {@code DELETE  /networks/:id} : delete the "id" network.
     *
     * @param id the id of the networkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/networks/{id}")
    public ResponseEntity<Void> deleteNetwork(@PathVariable Long id) {
        log.debug("REST request to delete Network : {}", id);
        networkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/networks?query=:query} : search for the network corresponding
     * to the query.
     *
     * @param query the query of the network search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/networks")
    public ResponseEntity<List<NetworkDTO>> searchNetworks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Networks for query {}", query);
        Page<NetworkDTO> page = networkService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
