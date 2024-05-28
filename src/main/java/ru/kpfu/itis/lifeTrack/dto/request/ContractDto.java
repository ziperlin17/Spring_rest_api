package ru.kpfu.itis.lifeTrack.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.lifeTrack.model.ContractEntity;

@Data
@Builder
public class ContractDto {
    private Long id;
    private String name;
    private String description;
    private String userId;
    private Long clientId;

    public static ContractDto fromEntity(ContractEntity contract) {
        return ContractDto.builder()
                .id(contract.getId())
                .name(contract.getName())
                .description(contract.getDescription())
                .userId(contract.getUser().getId())
                .clientId(contract.getClient().getId())
                .build();
    }

    public ContractEntity toEntity() {
        ContractEntity contract = new ContractEntity();
        contract.setId(this.id);
        contract.setName(this.name);
        contract.setDescription(this.description);
//        contract.setUser(this.clientId);
        return contract;
    }
}