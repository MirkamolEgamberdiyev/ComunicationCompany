package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TariffOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tariff tariff;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value = "0";

    private boolean additional;     // true -> tarifdagi xizmat miqdori     false -> tarifdagi xizmat narxi
}
