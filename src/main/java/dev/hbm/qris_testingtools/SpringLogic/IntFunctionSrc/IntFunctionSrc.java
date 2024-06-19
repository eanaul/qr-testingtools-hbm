package dev.hbm.qris_testingtools.SpringLogic.IntFunctionSrc;

import dev.hbm.qris_testingtools.Enum.FunctionSrcEnum;
import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INT_FUNCTION_SRC")
public class IntFunctionSrc {
    @Id
    @SequenceGenerator(name = "int_function_src_seq", sequenceName = "int_function_src_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "int_function_src_seq")
    private Long id;
    @Enumerated(EnumType.STRING)
    private FunctionSrcEnum src;
    @Enumerated(EnumType.STRING)
    private FunctionTypeEnum type;
}
