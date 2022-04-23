package pdp.uz.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttachServiceDto {

    @NotNull
    private Long subscriberId;

    @NotNull
    private Long serviceId;
}
