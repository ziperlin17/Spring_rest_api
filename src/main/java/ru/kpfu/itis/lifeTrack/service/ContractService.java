package ru.kpfu.itis.lifeTrack.service;

import ru.kpfu.itis.lifeTrack.dto.request.ClientDto;
import ru.kpfu.itis.lifeTrack.dto.request.ContractDto;
import ru.kpfu.itis.lifeTrack.dto.request.EarningDto;

import java.util.List;
import java.util.Optional;

public interface ContractService {
    List<ContractDto> getAllContracts();
    Optional<ContractDto> getContractById(Long id);
    ContractDto createContract(ContractDto contract);
    ContractDto updateContract(Long id, ContractDto contract);
    void deleteContract(Long id);
    List<ContractDto> getContractsByUserId(String userId);
    List<ContractDto> getContractsByClientId(Long clientId);
    ContractDto getContractByName(String name);
    List<ContractDto> searchContractsByDescription(String keyword);
    ClientDto getClientByContractId(Long contractId);
    EarningDto getEarningByContractId(Long contractId);
}