package com.aiken.spb_k_api.Entity;

import com.aiken.spb_k_api.Enum.TipoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TransaccionInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private TipoTransaccion tipo;

    @Column(nullable = false)
    private int Cantidad;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
