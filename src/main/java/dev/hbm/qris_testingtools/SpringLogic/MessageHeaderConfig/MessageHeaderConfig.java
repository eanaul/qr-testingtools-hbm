package dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig;

import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.FunctionArgsValue;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.MessageConfigDictionary;
import dev.hbm.qris_testingtools.SpringLogic.HeaderConfig.HeaderConfig;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MESSAGE_HEADER_CONFIG")
public class MessageHeaderConfig extends MessageConfigDictionary {
    @Id
    @SequenceGenerator(name = "message_header_config_seq", sequenceName = "message_header_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "message_header_config_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    private HeaderConfig headerConfig;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MESSAGE_HEADER_CONFIG_FUNC", joinColumns = @JoinColumn(name = "id"))
    private List<FunctionArgsValue> functions = new LinkedList<>();
}
