package com.ccsw.tutorial.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

/**
 * @author ccsw
 *
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, ClientDto dto) {

        Client categoria = null;

        if (id == null)
            categoria = new Client();
        else
            categoria = this.clientRepository.findById(id).orElse(null);

        categoria.setName(dto.getName());

        this.clientRepository.save(categoria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {

        this.clientRepository.deleteById(id);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsClient(String nombre) {
        List<Client> existeCliente = this.findAll().stream().filter(cliente -> cliente.getName().equals(nombre))
                .collect(Collectors.toList());

        if (existeCliente != null && !existeCliente.isEmpty())
            return true;

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client getById(Long id) {
        return this.clientRepository.getById(id);
    }
}