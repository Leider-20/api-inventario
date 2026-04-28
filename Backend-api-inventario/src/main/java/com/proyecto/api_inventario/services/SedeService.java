package com.proyecto.api_inventario.services;

import com.proyecto.api_inventario.exception.DuplicateDataException;
import com.proyecto.api_inventario.exception.ModelNotFoundException;
import com.proyecto.api_inventario.models.Sede;
import com.proyecto.api_inventario.repository.SedeRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;  // Esta clase "JdbcTemplate" sirve para poder ejecutar comandos SQL aquí en java. La usamos para reinciar los índices de la tabla en la base de datos cuando se borren los datos de esta.

    public Sede saveSede(Sede sede) {
        List<Sede> listaSede = sedeRepository.findAll();
        for (int i = 0; i < listaSede.size(); i++) {
            if (listaSede.get(i).getNombre_sede().equals(sede.getNombre_sede())){
                throw new DuplicateDataException("Datos duplicados.");
            }
        }
        return sedeRepository.save(sede);
    }

    public List<Sede> getSedeAll() {
        return sedeRepository.findAll();
    }

    public Optional<Sede> getSedeById(Long id) {
        return sedeRepository.findById(id);
    }

    private void reseedIndex() {
        jdbcTemplate.execute("truncate table sede");

        jdbcTemplate.execute("DROP SEQUENCE sede_seq ");    // Borrar la secuencia de asignación automática de los ínidices en la tabla.

        jdbcTemplate.execute("CREATE SEQUENCE sede_seq " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOCACHE");      // Crear una nueva secuencia de asignación automática de los ínidices en la tabla.

        jdbcTemplate.execute("CREATE OR REPLACE TRIGGER SEDE_TRG " +
                "BEFORE INSERT ON SEDE " +
                "FOR EACH ROW " +
                "BEGIN " +
                "  <<COLUMN_SEQUENCES>> " +
                "  BEGIN " +
                "    IF INSERTING AND :NEW.SEDE_ID IS NULL THEN " +
                "      SELECT SEDE_SEQ.NEXTVAL INTO :NEW.SEDE_ID FROM SYS.DUAL; " +
                "    END IF; " +
                "  END COLUMN_SEQUENCES; " +
                "END;");
    }

    public void deleteSedeAll() {
        sedeRepository.deleteAll();
        reseedIndex();
    }

    public void deleteSedeById(Long id) {
        sedeRepository.deleteById(id);
    }


    /** @La diferencia entre el método "deleteSedeById" de arriba y el que está aquí abajo es que el de abajo tiene una restricción.
     * Al intentar borrar una sede, si hay almacenes en las tablas de la BD asociados a esta, entonces no se borrará la sede*/

    /*public void deleteSedeById(Long id) {
        List<Almacen> almacenes = almacenRepository.findBySedeId(id);
        if (!almacenes.isEmpty()) {
            throw new DataIntegrityViolationException("No se puede eliminar la sede porque tiene almacenes asociados.");
        }
        sedeRepository.deleteById(id);
    }*/
}
