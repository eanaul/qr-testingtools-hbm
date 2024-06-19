package dev.hbm.qris_testingtools.SpringLogic.TagConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagConfigRepo extends JpaRepository<TagConfig, String> {
    List<TagConfig> findByTagId(int tagId);

    List<TagConfig> findByParentId(Long parentId);
}
