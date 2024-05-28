package ru.kpfu.itis.lifeTrack.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.lifeTrack.model.EarningEntity;

@Data
@Builder
public class EarningDto {
    private Long id;
    private Long amount;
    private Long contractId;

    public static EarningDto fromEntity(EarningEntity earning) {
        return EarningDto.builder()
                .id(earning.getId())
                .amount(earning.getAmount())
                .contractId(earning.getContract().getId())
                .build();
    }

    public EarningEntity toEntity() {
        EarningEntity earning = new EarningEntity();
        earning.setId(this.id);
        earning.setAmount(this.amount);
        return earning;
    }
}
