package dev.hbm.qris_testingtools.SpringLogic.FieldConfig;

import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldConfigRepository extends JpaRepository<FieldConfig, Long> {
    List<FieldConfig> findAllByOrderByIdAsc();
    List<FieldConfig> findAllBySchemeConfig(SchemeConfig schemeConfig);
    List<FieldConfig> findAllBySchemeConfig_IdOrderByIdAsc(long id);
    Optional<FieldConfig> findByFieldNameAndSchemeConfig_Id(String fName, long schemeId);

    @Query("SELECT fc FROM FieldConfig fc WHERE fc.fieldId IN (SELECT f.fieldId FROM FieldConfig f GROUP BY f.fieldId HAVING COUNT(*) > 1)")
    List<FieldConfig> findDuplicates();

    @Query("SELECT fc FROM FieldConfig fc WHERE fc.fieldId IN (SELECT f.fieldId FROM FieldConfig f WHERE f.schemeConfig.id = :schemeId GROUP BY f.fieldId HAVING COUNT(*) > 1) AND fc.schemeConfig.id = :schemeId")
    List<FieldConfig> findDuplicatesBySchemeId(@Param("schemeId") Long schemeId);

    List<FieldConfig> findByFieldId(Integer fieldId);

    @Query("SELECT f FROM FieldConfig f WHERE f.fieldId = :fieldId AND f.schemeConfig.id = :schemeId")
    List<FieldConfig> findByFieldIdAndSchemeId(@Param("fieldId") int fieldId, @Param("schemeId") Long schemeId);
}
