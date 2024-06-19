package dev.hbm.qris_testingtools.SpringLogic.FieldConfig;

import dev.hbm.qris_testingtools.SpringLogic.Condition.Condition;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.HeaderBodyConfigDictionary;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FIELD_CONFIG")
public class FieldConfig extends HeaderBodyConfigDictionary {
    @Id
    @SequenceGenerator(name = "field_config_seq", sequenceName = "field_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "field_config_seq")
    private Long id;
    @OneToOne
    @JoinColumn(name = "condition_id", referencedColumnName = "id")
    private Condition condition;
}
