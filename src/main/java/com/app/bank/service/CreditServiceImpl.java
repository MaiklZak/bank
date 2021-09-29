package com.app.bank.service;

import com.app.bank.dto.CreditDto;
import com.app.bank.entity.Credit;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreditServiceImpl {

    private final CreditRepository creditRepository;

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public UUID save(CreditDto creditDto) {
        return creditRepository.save(new Credit(creditDto)).getId();
    }

    public List<CreditDto> getAll() {
        return creditRepository.findAll(Sort.by("limitOn")).stream()
                .map(CreditDto::new)
                .collect(Collectors.toList());
    }

    public CreditDto getById(UUID id) throws NoSuchEntityException {
        if (!creditRepository.existsById(id)) {
            throw new NoSuchEntityException("Credit with specified id does not exist");
        }
        return new CreditDto(creditRepository.getById(id));
    }

    public void deleteById(UUID id) {
        creditRepository.deleteById(id);
    }

    public void update(CreditDto creditDto) throws NoSuchEntityException {
        if (!creditRepository.existsById(creditDto.getId())) {
            throw new NoSuchEntityException("Credit with specified id does not exist");
        }
        save(creditDto);
    }
}
