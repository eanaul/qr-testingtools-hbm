package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import dev.hbm.qris_testingtools.Enum.DataTypeEnum;
import dev.hbm.qris_testingtools.Enum.PadAlgEnum;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class HeaderBodyConfigDictionary {
    private String fieldName;
    private String description;
    private int length;
    private String padChar;
    @Column(nullable = true)
    private Integer fieldId;
    @Comment("0 -> RIGHT \n1 -> LEFT")
    private PadAlgEnum padAlg;
    @Comment("0 -> Number \n1 -> String \n2 -> Amount \n3 -> Object \n4 -> Array_Obj \n5 -> Array_String \n6 -> Boolean")
    private DataTypeEnum dataType;
    @ManyToOne
    @JoinColumn(name = "scheme_id", referencedColumnName = "id")
    private SchemeConfig schemeConfig;
}
