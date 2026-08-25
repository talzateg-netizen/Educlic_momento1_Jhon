package com.cesde.educlic.model.entity;

import com.cesde.educlic.model.base.BaseEntity;
import com.cesde.educlic.model.embeddable.Ubicacion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil extends BaseEntity {

    @Column(name = "biografia", length = 500)
    private String biografia;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Embedded
    private Ubicacion ubicacion;

    @OneToOne(mappedBy = "perfil")
    private Usuario usuario;
}