package ru.kpfu.itis.lifeTrack.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.dto.request.ClientDto;
import ru.kpfu.itis.lifeTrack.dto.request.ContractDto;
import ru.kpfu.itis.lifeTrack.dto.request.EarningDto;
import ru.kpfu.itis.lifeTrack.service.ContractService;

import java.util.List;
@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract", description = "API for managing contracts")
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "Get all contracts", description = "Retrieve a list of all contracts")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @Operation(summary = "Get contract by ID", description = "Retrieve a contract by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @ApiResponse(responseCode = "404", description = "Contract not found")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractDto> getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create a new contract", description = "Create a new contract in the system")
    @ApiResponse(responseCode = "201", description = "Contract created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractDto> createContract(@RequestBody ContractDto contractDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contractService.createContract(contractDto));
    }

    @Operation(summary = "Update an existing contract", description = "Update the details of an existing contract")
    @ApiResponse(responseCode = "200", description = "Contract updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @ApiResponse(responseCode = "404", description = "Contract not found")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractDto> updateContract(@PathVariable Long id, @RequestBody ContractDto contractDto) {
        return contractService.updateContract(id, contractDto) != null ?
                ResponseEntity.ok(contractDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a contract", description = "Delete a contract from the system")
    @ApiResponse(responseCode = "204", description = "Contract deleted successfully")
    @ApiResponse(responseCode = "404", description = "Contract not found")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get contract by name", description = "Retrieve a contract by its name")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @ApiResponse(responseCode = "404", description = "Contract not found")
    @GetMapping("/name/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractDto> getContractByName(@PathVariable String name) {
        ContractDto contract = contractService.getContractByName(name);
        return contract != null ? ResponseEntity.ok(contract) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Search contracts by description", description = "Search for contracts containing a specific keyword in their description")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractDto>> searchContractsByDescription(@RequestParam String keyword) {
        return ResponseEntity.ok(contractService.searchContractsByDescription(keyword));
    }

    @Operation(summary = "Get client by contract ID", description = "Retrieve the client associated with a specific contract ID")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class)))
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/{contractId}/client")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClientDto> getClientByContractId(@PathVariable Long contractId) {
        ClientDto client = contractService.getClientByContractId(contractId);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Get earning by contract ID", description = "Retrieve the earning associated with a specific contract ID")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EarningDto.class)))
    @ApiResponse(responseCode = "404", description = "Earning not found")
    @GetMapping("/{contractId}/earning")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EarningDto> getEarningByContractId(@PathVariable Long contractId) {
        EarningDto earning = contractService.getEarningByContractId(contractId);
        return earning != null ? ResponseEntity.ok(earning) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Get contracts by user ID", description = "Retrieve contracts associated with a specific user ID")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractDto>> getContractsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(contractService.getContractsByUserId(userId));
    }

    @Operation(summary = "Get contracts by client ID", description = "Retrieve contracts associated with a specific client ID")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class)))
    @GetMapping("/client/{clientId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractDto>> getContractsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(contractService.getContractsByClientId(clientId));
    }
}