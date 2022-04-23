package pdp.uz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto implements Serializable {
    @NotNull
    private String firstName;

    private String lastName;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Long branchId;
}
