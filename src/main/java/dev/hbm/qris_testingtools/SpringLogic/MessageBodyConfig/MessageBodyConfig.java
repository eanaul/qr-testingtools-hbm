package dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig;

import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.ConfigCondition;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.FunctionArgsValue;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.MessageConfigDictionary;
import dev.hbm.qris_testingtools.SpringLogic.FieldConfig.FieldConfig;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MESSAGE_BODY_CONFIG")
public class MessageBodyConfig extends MessageConfigDictionary {
    @Id
    @SequenceGenerator(name = "message_body_config_seq", sequenceName = "message_body_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "message_body_config_seq")
    private Long id;
    private Long parentId;
    private Integer level;
    @Column(length = 65535)
    private String path;
    @ManyToOne
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    private FieldConfig fieldConfig;
    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER)
    @OrderBy("sequence asc")
    private Set<MessageBodyConfig> childConfig = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MESSAGE_BODY_CONFIG_CONDITION", joinColumns = @JoinColumn(name = "id"))
    private Set<ConfigCondition> conditions = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MESSAGE_BODY_CONFIG_FUNC", joinColumns = @JoinColumn(name = "id"))
    private Set<FunctionArgsValue> functions = new HashSet<>();
    @Transient
    private MessageBodyConfig parentConfig;
}
