package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FunctionArgsValue {
    private String name;
    @Column(length = 65535)
    private String args;
    private NetworkStateEnum networkState;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionArgsValue)) return false;
        FunctionArgsValue that = (FunctionArgsValue) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getNetworkState(), that.getNetworkState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNetworkState());
    }
}
