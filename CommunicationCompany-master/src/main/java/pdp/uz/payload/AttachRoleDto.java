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
public class AttachRoleDto implements Serializable {
    @NotNull
    @ApiModelProperty(example = "navruz")
    private String username;

    @NotNull
    @ApiModelProperty(example = "ROLE_MANAGER")
    private String roleName;

}
