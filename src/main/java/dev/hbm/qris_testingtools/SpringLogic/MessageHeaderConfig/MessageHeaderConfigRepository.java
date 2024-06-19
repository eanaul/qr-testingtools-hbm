package dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig;

import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageHeaderConfigRepository extends JpaRepository<MessageHeaderConfig, Long> {
//    @Query("SELECT s FROM MessageHeaderConfig s WHERE s.messageType.id = :typeId and s.state = :state order by s.sequence asc")
//    Set<MessageHeaderConfig> findAllByMessageType_IdAndStateOrderBySequenceAsc(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    @Query("SELECT s FROM MessageHeaderConfig s WHERE s.messageType.id = :typeId and s.state = :state order by s.sequence asc")
    List<MessageHeaderConfig> findAllByMessageType_IdAndState(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    @Query("select s.headerConfig.fieldName from MessageHeaderConfig s WHERE s.messageType.id = :typeId and s.state = :state and s.derType = 0 order by s.sequence")
    List<String> findAllMandatoryFieldByTypeAndState(@Param("typeId") long typeId, @Param("state") MessageStateEnum state);

    List<MessageHeaderConfig> findAllByOrderByIdAsc();
    List<MessageHeaderConfig> findAllByMessageType_IdAndStateOrderBySequenceAsc(long id, MessageStateEnum state);
}
