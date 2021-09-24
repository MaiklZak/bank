package com.app.bank.service;

import com.app.bank.dto.ClientDto;
import com.app.bank.entity.Client;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements EntityService<ClientDto> {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UUID save(ClientDto clientDto) {
        return clientRepository.save(new Client(clientDto)).getId();
    }

    @Override
    public List<ClientDto> getAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::new).collect(Collectors.toList());
    }

    @Override
    public ClientDto getById(UUID id) throws NoSuchEntityException {
        if (!clientRepository.existsById(id)) {
            throw new NoSuchEntityException("Client with specified id does not exist");
        }
        return new ClientDto(clientRepository.getById(id));
    }

    @Override
    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }

    @Override
    public void update(ClientDto clientDto) throws NoSuchEntityException {
        if (!clientRepository.existsById(clientDto.getId())) {
            throw new NoSuchEntityException("Client with specified id does not exist");
        }
        save(clientDto);
    }
}
