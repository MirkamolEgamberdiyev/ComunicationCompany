package pdp.uz.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {

    @NotNull
    @ApiModelProperty(example = "90")
    private Short code;

    @NotNull
    @ApiModelProperty(example = "3734142")
    private String number;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    @ApiModelProperty(example = "KA")
    private String passportSeries;

    @NotNull
    @ApiModelProperty(example = "4562178")
    private String passportNumber;

    @NotNull
    @ApiModelProperty(example = "PHYSICAL_PERSON")
    private String clientType;

    private Long branchId;

    private Double balance;

    private Double price;

    private Long tariffId;

    @ApiModelProperty(example = "true")
    private boolean serviceDebt;
}
