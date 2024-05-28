package ru.kpfu.itis.lifeTrack.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.lifeTrack.model.ClientEntity;

@Data
@Builder
public class ClientDto {
    private Long id;
    private String name;

    public static ClientDto fromEntity(ClientEntity client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    public ClientEntity toEntity() {
        ClientEntity client = new ClientEntity();
        client.setId(this.id);
        client.setName(this.name);
        return client;
    }
}