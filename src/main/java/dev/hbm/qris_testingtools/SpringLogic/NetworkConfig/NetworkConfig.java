package dev.hbm.qris_testingtools.SpringLogic.NetworkConfig;

import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.AddtValue;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NETWORK_CONFIG")
public class NetworkConfig {
    @Id
    @SequenceGenerator(name = "network_config_seq", sequenceName = "network_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "network_config_seq")
    private Long id;
    private String localAddr;
    private int localPort;
    private String remoteAddr;
    private int remotePort;
    private NetworkStateEnum state;
    private boolean connected;
    @ManyToOne
    @JoinColumn(name = "scheme_id", referencedColumnName = "id")
    private SchemeConfig schemeConfig;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "network_config_addt_value", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private Set<AddtValue> addtValues = new HashSet<>();
}