package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class AddtValue {
    /*
        TRANS_TYPE -> Message Type
        SECURITY_CONFIG -> Network Config
     */
    private String name;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddtValue)) return false;
        AddtValue that = (AddtValue) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }
}
