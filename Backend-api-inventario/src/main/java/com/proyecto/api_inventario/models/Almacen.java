package com.proyecto.api_inventario.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "almacen_id")
    private Long almacen_id;

    @Column(name = "nombre_almacen")
    private String nombre_almacen;

    @ManyToOne
    @JoinColumn(name = "sede_id")  // Toma el atributo/columna con este nombre que está en la tabla con la que se relaciona.
    private Sede sede;

    /** El "cascade = CascadeType.ALL, orphanRemoval = true" es para que cuando se realice alguna operación en la tabla de almacenes, se realice la misma operación en la tabla asociada, la tabla de productos.
     * Hay que tener en cuenta que esto solo funcionará cuando se hacen peticiones con las operaciones definidas en la API que hicimos aquí en el entorno de spring boot,
     * pero cuando hacemos la misma operación en la base de datos no funciona y surge el error de restricción de integridad. */

    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "almacen", cascade = CascadeType.ALL, orphanRemoval = true)

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "almacen")
    private List<Producto> productos;

    @Transient  // Usamos esta anotación para los atributos que no se van a mapear/crear como un atributo/columna de la tabla en la base de datos.
    private Long sedeId;






    // Getter and Setter

    public Long getAlmacen_id() {
        return almacen_id;
    }

    public void setAlmacen_id(Long almacen_id) {
        this.almacen_id = almacen_id;
    }

    public String getNombre_almacen() {
        return nombre_almacen;
    }

    public void setNombre_almacen(String nombre_almacen) {
        this.nombre_almacen = nombre_almacen;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }


    // TODO poner el nullable y el length en los diferentes atributos de la tabla.


}
