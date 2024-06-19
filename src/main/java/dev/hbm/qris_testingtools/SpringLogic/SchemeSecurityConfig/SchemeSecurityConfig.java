package dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig;

import dev.hbm.qris_testingtools.Enum.SecurityTypeEnum;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SCHEME_SECURITY_CONFIG")
public class SchemeSecurityConfig {
    @Id
    @SequenceGenerator(name = "scheme_security_seq", sequenceName = "scheme_security_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "scheme_security_seq")
    private Long id;
    private String name;
    private String value;
    private SecurityTypeEnum securityType;
    private byte[] fileData;
    @ManyToOne
    @JoinColumn(name = "scheme_id", referencedColumnName = "id")
    private SchemeConfig schemeConfig;
}
