package com.proyecto.api_inventario.services;

import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Almacen;
import com.proyecto.api_inventario.models.Sede;
import com.proyecto.api_inventario.repository.AlmacenRepository;
import com.proyecto.api_inventario.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private SedeRepository sedeRepository;

    public Almacen saveAlmacen(Almacen almacen) {

        Long sedeId = almacen.getSedeId();

        if (sedeId != null) {
            Optional<Sede> sede = sedeRepository.findById(sedeId);
            if (sede.isPresent()) {
                almacen.setSede(sede.get());
                return almacenRepository.save(almacen);
            }
            throw new ModelNotFoundException("La sede con ese ID no está registrada.");
        }
        throw new NullPointerException("Ingrese el ID de la sede.");
    }

    /*
    public Almacen saveAlmacen(Almacen almacen, Long sedeId) {
        Optional<Sede> sede = sedeRepository.findById(sedeId);
        if (sede.isPresent()) {
            almacen.setSede(sede.get());
            return almacenRepository.save(almacen);
        }
        throw new ModelNotFoundException("La sede con ese ID no registrada.");
    }
    */

    public List<Almacen> getAlmacenAll() {
        return almacenRepository.findAll();
    }

    public Optional<Almacen> getAlmacenById(Long id) {
        return almacenRepository.findById(id);
    }

    public void deleteAlmacenAll() {
        almacenRepository.deleteAll();
    }

    public void deleteAlmacenById(Long id) {
        almacenRepository.deleteById(id);
    }
}
