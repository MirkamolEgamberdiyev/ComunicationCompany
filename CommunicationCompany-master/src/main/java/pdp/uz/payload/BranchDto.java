package pdp.uz.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.entity.enums.BranchType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto implements Serializable {
    @NotNull
    private String name;

    @ApiModelProperty(example = "FILIAL or empty value")
    private BranchType branchType;

    @NotNull
    @ApiModelProperty(example = "abror")
    private String managerUsername;

    public BranchDto(String name) {
        this.name = name;
    }
}
