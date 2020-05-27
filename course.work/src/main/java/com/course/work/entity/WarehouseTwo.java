package com.course.work.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse2")
public class WarehouseTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Goods goods;

    @Column(name = "good_count", nullable = false)
    private Integer count;
}
