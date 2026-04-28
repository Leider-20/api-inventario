package com.proyecto.api_inventario.controllers;

import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Almacen;
import com.proyecto.api_inventario.models.Producto;
import com.proyecto.api_inventario.services.AlmacenService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
/*@RequestMapping("/inventario") // No uso esta anotación porque quiero acceder directamente a los endpoints de cada uno de los métodos http donde hago las peticiones.*/

public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @ApiOperation(value = "Método para guardar los almacenes.")
    @PostMapping("/guardar-almacen")
    public ResponseEntity<Map<String, Object>> saveAlmacen(@RequestBody Almacen almacen) {

        Almacen almacenGuardado = almacenService.saveAlmacen(almacen);
        Map<String, Object> response = new HashMap<>();

        response.put("almacen_id", almacenGuardado.getAlmacen_id());
        response.put("nombre_almacen", almacenGuardado.getNombre_almacen());
        response.put("sede_id", almacenGuardado.getSede().getSede_id());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*public Almacen saveAlmacen(@RequestBody Almacen almacen, @RequestParam Long sedeId) {
        return almacenService.saveAlmacen(almacen, sedeId);
        //http://localhost:8080/guardar-almacen?sedeId=1
    }*/

    @ApiOperation(value = "Método para consultar y obtener todos los almacenes.")
    @GetMapping("/obtener-almacen-all")
    public ResponseEntity<List<Map<String, Object>>> getAlmacenAll() {

        List<Almacen> almacenLista = almacenService.getAlmacenAll();
        List<Map<String, Object>> almacenMap = new ArrayList<>();

        if (almacenLista.isEmpty()) {
            throw new ModelNotFoundException("No hay almacenes registrados.");
        }
        for (int i = 0; i < almacenLista.size(); i++) {

            Map<String, Object> response = new HashMap<>();

            response.put("sede_id", almacenLista.get(i).getSede().getSede_id());
            response.put("nombre_almacen", almacenLista.get(i).getNombre_almacen());
            response.put("almacen_id", almacenLista.get(i).getAlmacen_id());

            List<Producto> productosAsociados = almacenLista.get(i).getProductos();     // Para guardar datos y reducir código
            List<Object> datosProducto = new ArrayList<>();

            for (int j = 0; j < productosAsociados.size(); j++) {
                datosProducto.add("id_producto: " + productosAsociados.get(j).getProd_id() +
                        ", nombre_producto: " + productosAsociados.get(j).getNombre_producto() +
                        ", cantidad: " + productosAsociados.get(j).getCantidad() +
                        ", precio: " + productosAsociados.get(j).getPrecio());
            }
            response.put("productos_asociados", datosProducto);
            almacenMap.add(response);
        }
        return new ResponseEntity<>(almacenMap, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Método para consultar y obtener los almacenes por su ID.")
    @GetMapping("/obtener-almacen-id/{id}")
    public ResponseEntity<Map<String, Object>> getAlmacenById(@PathVariable Long id) {

        Optional<Almacen> almacen = almacenService.getAlmacenById(id);
        Map<String, Object> response = new HashMap<>();

        if (almacen.isPresent()) {

            Almacen obtenerAlmacen = almacen.get();     // Para guardar datos y reducir código

            response.put("almacen_id", obtenerAlmacen.getAlmacen_id());
            response.put("nombre_almacen",obtenerAlmacen.getNombre_almacen());

            List<Producto> productosAsociados = obtenerAlmacen.getProductos();     // Para guardar datos y reducir código
            List<Object> datosProducto = new ArrayList<>();

            for (int i = 0; i < productosAsociados.size(); i++) {
                datosProducto.add("id_producto: " + productosAsociados.get(i).getProd_id() +
                        ", nombre_producto: " + productosAsociados.get(i).getNombre_producto() +
                        ", cantidad: " + productosAsociados.get(i).getCantidad() +
                        ", precio: " + productosAsociados.get(i).getPrecio());
            }
            response.put("productos_asociados", datosProducto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new ModelNotFoundException("El almacén con ese ID no se encuentra registrado.");
    }

    @ApiOperation(value = "Método para borrar todos los almacenes.")
    @DeleteMapping("/borrar-almacen-all")
    public String deleteAlmacenAll() {

        boolean almacenVacio = almacenService.getAlmacenAll().isEmpty();

        if (!almacenVacio) {
            almacenService.deleteAlmacenAll();
            return "Se han borrado todos los almacenes.";
        }
        return "No hay almacenes para borrar";
    }

    @ApiOperation(value = "Método para borrar los almacenes por su ID.")
    @DeleteMapping("/borrar-almacen-id/{id}")
    public String deleteAlmacenById(@PathVariable Long id) {

        List<Almacen> almacenLista = almacenService.getAlmacenAll();

        for (int i = 0; i < almacenLista.size(); i++) {
            if (almacenLista.get(i).getAlmacen_id().equals(id)) {
                almacenService.deleteAlmacenById(id);
                return "Almacén eliminado con éxito.";
            }
        }
        return "El almacén con ese ID no se encuentra registrado.";
    }
}
