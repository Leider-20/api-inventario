package com.proyecto.api_inventario.controllers;

import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Almacen;
import com.proyecto.api_inventario.models.Sede;
import com.proyecto.api_inventario.services.SedeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
/*@RequestMapping("/inventario") // No uso esta anotación porque quiero acceder directamente a los endpoints de cada uno de los métodos http donde hago las peticiones.*/

public class SedeController {

    @Autowired
    private SedeService sedeService;

    /**
     * El método de abajo: "@PostMapping" lo hago de tipo "return" y no "void" para que en Postman cuando haga una solicitud para guardar los datos
     * me retorne y me muestre los datos que estoy guardando.
     */

    @ApiOperation(value = "Método para guardar las sedes.")
    @PostMapping("/guardar-sede")
    public Sede saveSede(@RequestBody Sede sede) {
        return sedeService.saveSede(sede);
    }

    @ApiOperation(value = "Método para consultar y obtener todas las sedes.")
    @GetMapping("/obtener-sede-all")
    public ResponseEntity<List<Map<String, Object>>> getSedeAll() {

        List<Sede> sedeLista = sedeService.getSedeAll();    // Para guardar datos y reducir código
        List<Map<String, Object>> sedeMap = new ArrayList<>();

        if (sedeLista.isEmpty()){
            throw new ModelNotFoundException("No hay sedes registradas.");
        }
        for (int i = 0; i < sedeLista.size(); i++) {

            Map<String, Object> response = new HashMap<>();

            response.put("sede_id", sedeLista.get(i).getSede_id());
            response.put("nombre_sede", sedeLista.get(i).getNombre_sede());

            List<Almacen> almacenesAsociados = sedeLista.get(i).getAlmacenes();     // Para guardar datos y reducir código
            List<Object> datosAlmacen = new ArrayList<>();

            for (int j = 0; j < almacenesAsociados.size(); j++) {
                datosAlmacen.add("id_almacen: " + almacenesAsociados.get(j).getAlmacen_id() +
                                ", nombre_almacen: " + almacenesAsociados.get(j).getNombre_almacen());
            }
            response.put("almacenes_asociados", datosAlmacen);
            sedeMap.add(response);
        }
        return new ResponseEntity<>(sedeMap, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Método para consultar y obtener las sedes por su ID.")
    @GetMapping("/obtener-sede-id/{id}")
    public ResponseEntity<Map<String, Object>> getSedeById(@PathVariable Long id) {

        Optional<Sede> sede = sedeService.getSedeById(id);
        Map<String, Object> response = new HashMap<>();

        if (sede.isPresent()) {

            Sede obtenerSede = sede.get();     // Para guardar datos y reducir código

            response.put("sede_id", obtenerSede.getSede_id());
            response.put("nombre_sede",obtenerSede.getNombre_sede());

            List<Almacen> almacenesAsociados = obtenerSede.getAlmacenes();     // Para guardar datos y reducir código
            List<Object> datosAlmacen = new ArrayList<>();

            for (int i = 0; i < almacenesAsociados.size(); i++) {
                datosAlmacen.add("id_almacen: " + almacenesAsociados.get(i).getAlmacen_id() +
                        ", nombre_almacen: " + almacenesAsociados.get(i).getNombre_almacen());
            }
            response.put("almacenes_asociados", datosAlmacen);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new ModelNotFoundException("La sede con ese ID no se encuentra registrada.");
    }

    @ApiOperation(value = "Método para borrar todas las sedes.")
    @DeleteMapping("/borrar-sede-all")
    public String deleteSedeAll() {

        boolean sedeVacia = sedeService.getSedeAll().isEmpty();

        if (!sedeVacia){
            sedeService.deleteSedeAll();
            return "Se han borrado todas las sedes.";
        }
        return "No hay sedes para borrar.";
    }

    @ApiOperation(value = "Método para borrar las sedes por su ID.")
    @DeleteMapping("/borrar-sede-id/{id}")
    public String deleteSedeById(@PathVariable Long id) {

        List<Sede> sedeLista = sedeService.getSedeAll();

        for (int i = 0; i < sedeLista.size(); i++) {
            if (sedeLista.get(i).getSede_id().equals(id)) {
                sedeService.deleteSedeById(id);
                return "Sede eliminada con éxito.";
            }
        }
        return "La sede con ese ID no se encuentra registrada.";
    }
}
