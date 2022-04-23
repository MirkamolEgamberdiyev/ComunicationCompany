package pdp.uz.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
public class PaymentDto implements Serializable {

    @NotNull
    private double amount;

    @NotNull
    private String number;

    @NotNull
    @ApiModelProperty(example = "BANK")
    private String paymentType;

    @NotNull
    private String payer;
}
