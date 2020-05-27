package com.course.work.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "SALES")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "goods_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Goods goods;

    @Column(name = "good_count", nullable = false)
    private Integer count;

    @Column(name = "create_date", nullable = false)
    private Timestamp timestamp;
}
