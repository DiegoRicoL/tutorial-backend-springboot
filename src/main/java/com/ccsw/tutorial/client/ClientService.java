package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

/**
 * @author ccsw
 *
 */
public interface ClientService {

    /**
     * Método para recuperar todas las
     * {@link com.ccsw.tutorial.category.model.Client}
     * 
     * @return
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar una
     * {@link com.ccsw.tutorial.category.model.Client}
     * 
     * @param dto
     * @return
     */
    void save(Long id, ClientDto dto);

    /**
     * Método para borrar una {@link com.ccsw.tutorial.category.model.Client}
     * 
     * @param id
     */
    void delete(Long id);

    /*
     * Método para buscar si ya existe un {@link
     * com.ccsw.tutorial.category.model.Client}
     * 
     * @return
     */
    boolean existsClient(String nombre);

    /*
     * Devuelve un cliente buscandolo por Id
     * 
     * @param id
     * 
     * @return
     */
    Client getById(Long id);
}