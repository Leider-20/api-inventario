package com.proyecto.api_inventario.services;

import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Almacen;
import com.proyecto.api_inventario.models.Producto;
import com.proyecto.api_inventario.repository.AlmacenRepository;
import com.proyecto.api_inventario.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    public Producto saveProducto (Producto producto){

        Long almacenId = producto.getAlmacenId();

        if (almacenId != null){
            Optional<Almacen> almacen = almacenRepository.findById(almacenId);
            if (almacen.isPresent()){
                producto.setAlmacen(almacen.get());
                return productosRepository.save(producto);
            }
            throw new ModelNotFoundException("El almacén con ese ID no está registrado.");
        }
        throw new NullPointerException("Ingrese el ID del almacén.");
    }

    /*
    public Producto saveProducto(Producto producto, Long almacenId) {
        Optional<Almacen> almacen = almacenRepository.findById(almacenId);
        if (almacen.isPresent()) {
            producto.setAlmacen(almacen.get());
            return productosRepository.save(producto);
        }
        throw new ModelNotFoundException("La sede con ese ID no registrada.");
    }
    */

    public List<Producto> getProductoAll(){
        return productosRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id){
        return productosRepository.findById(id);
    }

    public void deleteProductoAll(){
        productosRepository.deleteAll();
    }

    public void deleteProductoById(Long id){
        productosRepository.deleteById(id);
    }
}
