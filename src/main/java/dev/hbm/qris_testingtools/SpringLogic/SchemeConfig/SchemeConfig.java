package dev.hbm.qris_testingtools.SpringLogic.SchemeConfig;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCHEME_CONFIG")
public class SchemeConfig {
    @Id
    @SequenceGenerator(name = "scheme_config_seq", sequenceName = "scheme_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "scheme_config_seq")
    private Long id;
    private String name;
}
