package dev.hbm.qris_testingtools.SpringLogic.NetworkConfig;

import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkConfigRepository extends JpaRepository<NetworkConfig, Long> {
    List<NetworkConfig> findAllByStateAndSchemeConfig_Id(NetworkStateEnum state, long schemeId);
    List<NetworkConfig> findAllBySchemeConfig_IdOrderByIdAsc(long schemeId);
    List<NetworkConfig> findAllBySchemeConfig_IdAndStateOrderByIdAsc(long schemeId, NetworkStateEnum state);
    Optional<NetworkConfig> findByLocalAddrAndLocalPort(String addr, int port);
    Optional<NetworkConfig> findByRemoteAddrAndRemotePort(String addr, int port);
}
