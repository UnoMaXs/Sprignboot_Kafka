package com.aiken.spb_k_api.Repository;

import com.aiken.spb_k_api.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository  extends JpaRepository<Producto,Long> {


}
