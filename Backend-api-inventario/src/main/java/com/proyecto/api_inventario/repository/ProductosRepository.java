package com.proyecto.api_inventario.repository;

import com.proyecto.api_inventario.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosRepository extends JpaRepository<Producto, Long> {
}
