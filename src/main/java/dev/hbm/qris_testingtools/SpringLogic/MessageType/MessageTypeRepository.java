package dev.hbm.qris_testingtools.SpringLogic.MessageType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageTypeRepository extends JpaRepository<MessageType, Long> {
    List<MessageType> findAllByOrderByNameAsc();
    List<MessageType> findAllBySchemeConfig_IdOrderByNameAsc(long schemeId);
    Optional<MessageType> findBySchemeConfig_IdAndValue(long schemeId, String value);
}
