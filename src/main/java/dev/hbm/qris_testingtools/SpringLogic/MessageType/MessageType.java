package dev.hbm.qris_testingtools.SpringLogic.MessageType;

import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.AddtValue;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
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
@Table(name = "MESSAGE_TYPE")
public class MessageType {
    @Id
    @SequenceGenerator(name = "message_type_seq", sequenceName = "message_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "message_type_seq")
    private Long id;
    private String name;
    private String value;
    private int toTime;
    @ManyToOne
    @JoinColumn(name = "scheme_id", referencedColumnName = "id")
    private SchemeConfig schemeConfig;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_type_addt_value", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private Set<AddtValue> addtValues = new HashSet<>();
}
