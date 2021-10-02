package com.app.bank.service;

import com.app.bank.dto.ClientDto;
import com.app.bank.entity.Client;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public UUID save(ClientDto clientDto) {
        return clientRepository.save(new Client(clientDto)).getId();
    }

    public List<ClientDto> getAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::new).collect(Collectors.toList());
    }

    public List<ClientDto> getPageOfClient(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return clientRepository.findAll(nextPage)
                .getContent()
                .stream()
                .map(ClientDto::new)
                .collect(Collectors.toList());
    }

    public ClientDto getById(UUID id) throws NoSuchEntityException {
        logger.info("Getting client with id: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new NoSuchEntityException("Client with specified id does not exist");
        }
        return new ClientDto(clientRepository.getById(id));
    }

    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }

    public void update(ClientDto clientDto) throws NoSuchEntityException {
        if (!clientRepository.existsById(clientDto.getId())) {
            throw new NoSuchEntityException("Client with specified id does not exist");
        }
        save(clientDto);
    }
}
