package dev.hbm.qris_testingtools.SpringLogic.IntFunction;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INT_FUNCTION")
public class IntFunction {
    @Id
    @SequenceGenerator(name = "int_function_seq", sequenceName = "int_function_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "int_function_seq")
    private Long id;
    private String expression;
    private String description;
    @Column(length = 65535)
    private String args; //Args Container
}
