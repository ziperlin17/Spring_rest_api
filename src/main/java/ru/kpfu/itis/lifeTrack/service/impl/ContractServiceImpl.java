package ru.kpfu.itis.lifeTrack.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.dto.request.ClientDto;
import ru.kpfu.itis.lifeTrack.dto.request.ContractDto;
import ru.kpfu.itis.lifeTrack.dto.request.EarningDto;
import ru.kpfu.itis.lifeTrack.model.ClientEntity;
import ru.kpfu.itis.lifeTrack.model.ContractEntity;
import ru.kpfu.itis.lifeTrack.repository.ContractRepository;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.service.ContractService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final UserRepo userRepo;

    @Override
    public List<ContractDto> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(ContractDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContractDto> getContractById(Long id) {
        return contractRepository.findById(id).map(ContractDto::fromEntity);
    }

    @Override
    public ContractDto createContract(ContractDto contractDTO) {
        ContractEntity contract = contractDTO.toEntity();
        return ContractDto.fromEntity(contractRepository.save(contract));
    }

    @Override
    public ContractDto updateContract(Long id, ContractDto contractDTO) {
        if (contractRepository.existsById(id)) {
            ContractEntity contract = contractDTO.toEntity();
            contract.setId(id);
            return ContractDto.fromEntity(contractRepository.save(contract));
        }
        return null;
    }

    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    @Override
    public List<ContractDto> getContractsByUserId(String userId) {
        return contractRepository.findByUserId(String.valueOf(userId)).stream()
                .map(ContractDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDto> getContractsByClientId(Long clientId) {
        return contractRepository.findByClientId(clientId).stream()
                .map(ContractDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDto getContractByName(String name) {
        return ContractDto.fromEntity(contractRepository.findByName(name));
    }

    @Override
    public List<ContractDto> searchContractsByDescription(String keyword) {
        return contractRepository.findByDescriptionContaining(keyword).stream()
                .map(ContractDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getClientByContractId(Long contractId) {
        Optional<ContractEntity> contract = contractRepository.findById(contractId);
        return contract.map(value -> ClientDto.fromEntity(new ClientEntity())).orElse(null);
    }

    @Override
    public EarningDto getEarningByContractId(Long contractId) {
        Optional<ContractEntity> contract = contractRepository.findById(contractId);
        return contract.map(value -> EarningDto.fromEntity(value.getEarning())).orElse(null);
    }


}