package dev.hbm.qris_testingtools.SpringLogic.TagConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag_config")
public class TagConfig {
    @Id
    private Long id;
    private String name;
    private int tagId;
    private int tagSize;
    private int length;
    private Long parentId;
    private boolean hasChild;
}
