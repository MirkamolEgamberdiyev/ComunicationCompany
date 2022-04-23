package pdp.uz.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
public class ActivityDto implements Serializable {

    @NotNull
    @ApiModelProperty(example = "949291122")
    private String fromNumber;

    @ApiModelProperty(example = "939994422 or null")
    private String toNumber;

    @NotNull
    @ApiModelProperty(example = "CALL_NETWORK")
    private String serviceCategory;

    @NotNull
    private Long amount;
}
