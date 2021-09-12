package pl.vrbrt.spring.springBatch;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUKTY")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nazwa")
    private String name;
    @Column(name = "kod_producenta")
    private String manufacturerCode;
    @Column(name = "cena_jednostkowa")
    private BigDecimal price;
    @Column(name = "jednostka")
    private String unit;
}
