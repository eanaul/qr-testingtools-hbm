package dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeSecurityConfigRepository extends JpaRepository<SchemeSecurityConfig, Long> {
    List<SchemeSecurityConfig> findAllBySchemeConfig_id(Long id);
}
