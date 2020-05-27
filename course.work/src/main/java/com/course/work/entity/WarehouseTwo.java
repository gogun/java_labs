package com.course.work.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "warehouse2")
public class WarehouseTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false, unique = true)
    private Goods goods;

    @Column(name = "good_count", nullable = false)
    private Integer count;
}
