package dev.hbm.qris_testingtools.SpringLogic.HeaderConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderConfigRepository extends JpaRepository<HeaderConfig, Long> {
    List<HeaderConfig> findAllBySchemeConfig_idOrderByIdAsc(long id);
}
