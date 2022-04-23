package pdp.uz.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PackageDto implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String key;

    @NotNull
    private Long value;

    @NotNull
    private double price;

    @NotNull
    private Integer validityDay;

    private boolean addResidue;

    private List<Long> tariffsId;
}
