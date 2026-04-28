package com.proyecto.api_inventario.controllers;

import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Producto;
import com.proyecto.api_inventario.services.ProductoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
//@RequestMapping("/inventario")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @ApiOperation(value = "Método para guardar los productos.")
    @PostMapping("/guardar-producto")
    public ResponseEntity<Map<String, Object>> saveProducto(@RequestBody Producto producto){

        Producto productoGuardado = productoService.saveProducto(producto);
        Map<String, Object> response = new HashMap<>();

        response.put("producto_id", productoGuardado.getProd_id());
        response.put("nombre_producto", productoGuardado.getNombre_producto());
        response.put("cantidad", productoGuardado.getCantidad());
        response.put("precio", productoGuardado.getPrecio());
        response.put("almacen_id", productoGuardado.getAlmacen().getAlmacen_id());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Método para consultar y obtener todos los productos.")
    @GetMapping("/obtener-producto-all")
    public ResponseEntity<List<Map<String, Object>>> getProductoAll(){

        List<Producto> productoLista = productoService.getProductoAll();
        List<Map<String, Object>> productoMap = new ArrayList<>();

        if (productoLista.isEmpty()) {
            throw new ModelNotFoundException("No hay productos registrados.");
        }
        for (int i = 0; i < productoLista.size(); i++) {

            Map<String, Object> response = new HashMap<>();

            response.put("producto_id", productoLista.get(i).getProd_id());
            response.put("nombre_producto", productoLista.get(i).getNombre_producto());
            response.put("cantidad", productoLista.get(i).getCantidad());
            response.put("precio", productoLista.get(i).getPrecio());
            response.put("almacen_id", productoLista.get(i).getAlmacen().getAlmacen_id());
            productoMap.add(response);
        }
        return new ResponseEntity<>(productoMap, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Método para consultar y obtener los productos por su ID.")
    @GetMapping("/obtener-producto-id/{id}")
    public ResponseEntity<Map<String, Object>> getProductoById(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Optional<Producto> producto = productoService.getProductoById(id);

        if (producto.isPresent()) {
            response.put("producto_id", producto.get().getProd_id());
            response.put("nombre_producto", producto.get().getNombre_producto());
            response.put("cantidad", producto.get().getCantidad());
            response.put("precio", producto.get().getPrecio());
            response.put("almacen_id", producto.get().getAlmacen().getAlmacen_id());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new ModelNotFoundException("El producto con ese ID no se encuentra registrado.");
    }

    @ApiOperation(value = "Método para borrar todos los productos.")
    @DeleteMapping("/borrar-producto-all")
    public String deleteProductoAll(){

        boolean productoVacio = productoService.getProductoAll().isEmpty();

        if (!productoVacio){
            productoService.deleteProductoAll();
            return "Se han borrado todos los productos.";
        }
        return "No hay productos para borrar.";
    }

    @ApiOperation(value = "Método para borrar los productos por su ID.")
    @DeleteMapping("/borrar-producto-id/{id}")
    public String deleteProductoById(@PathVariable Long id){

        List<Producto> productoLista = productoService.getProductoAll();

        for (int i = 0; i < productoLista.size(); i++) {
            if (productoLista.get(i).getProd_id().equals(id)){
                productoService.deleteProductoById(id);
                return "Producto eliminado con éxito.";
            }
        }
        return "El producto con ese ID no se encuentra registrado.";
    }
}
