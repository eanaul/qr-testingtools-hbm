package dev.hbm.qris_testingtools.SpringLogic.EmbedClass;

import dev.hbm.qris_testingtools.Enum.FunctionSrcEnum;
import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ArgsContainer {
    private String name;
    private String value;
    private FunctionTypeEnum type;
    private FunctionSrcEnum src = FunctionSrcEnum.DEFAULT;
    private final Set<ArgsContainer> child = new HashSet<>();
}
