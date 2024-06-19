package dev.hbm.qris_testingtools.SpringLogic.SchemeConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemeConfigRepository extends JpaRepository<SchemeConfig, Long> {
    List<SchemeConfig> findAllByOrderByIdAsc();
    Optional<SchemeConfig> findByName(String schemeConfig);
}
