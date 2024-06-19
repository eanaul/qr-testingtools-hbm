package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfigCondition {
    private String param;
    private String operator;
    private String value;
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigCondition)) return false;
        ConfigCondition that = (ConfigCondition) o;
        return Objects.equals(getParam(), that.getParam()) &&
                Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParam(), getPath());
    }
}
