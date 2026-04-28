package com.proyecto.api_inventario.repository;

import com.proyecto.api_inventario.models.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
}
