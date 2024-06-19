package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import dev.hbm.qris_testingtools.Enum.DerTypeEnum;
import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public class MessageConfigDictionary {
    private Integer sequence;
    @Comment("0 -> Request \n1 -> Response")
    private MessageStateEnum state;
    @Comment("0 -> Mandatory \n1 -> Conditional \n2 -> Optional")
    private DerTypeEnum derType;
    private String defaultValue;
    private boolean useFunction;
    @ManyToOne
    @JoinColumn(name = "message_type_id", referencedColumnName = "id")
    private MessageType messageType;
}
