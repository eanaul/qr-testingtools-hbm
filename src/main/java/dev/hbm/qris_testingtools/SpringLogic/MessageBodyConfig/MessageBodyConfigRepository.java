package dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig;

import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageBodyConfigRepository extends JpaRepository<MessageBodyConfig, Long> {
    @Query("SELECT s FROM MessageBodyConfig s LEFT JOIN FETCH s.childConfig WHERE (s.parentId IS NULL or s.parentId = 0) and s.messageType.id = :typeId and s.state = :state order by s.sequence asc")
    Set<MessageBodyConfig> findNestedChildByMessageTypeAndState(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    @Query("select s from MessageBodyConfig s WHERE s.messageType.id = :typeId and s.state = :state order by s.level")
    List<MessageBodyConfig> findAllByMessageType_IdAndState(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    @Query("select s from MessageBodyConfig s WHERE s.messageType.id = :typeId and s.parentId IS NULL and s.state = :state and s.derType = 0 order by s.sequence")
    List<MessageBodyConfig> findAllMandatoryFieldByTypeAndState(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    void deleteAllByParentId(long id);
}
