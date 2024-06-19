package dev.hbm.qris_testingtools.SpringLogic.HeaderConfig;

import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.HeaderBodyConfigDictionary;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HEADER_CONFIG")
public class HeaderConfig extends HeaderBodyConfigDictionary {
    @Id
    @SequenceGenerator(name = "header_config_seq", sequenceName = "header_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "header_config_seq")
    private Long id;
}
