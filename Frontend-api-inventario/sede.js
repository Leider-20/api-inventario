// Variables
const campoSedeNombre = document.querySelector("#input_sede_nombre");
const campoSedeUbicacion = document.querySelector("#input_sede_ubicacion");
const campoSedeId = document.querySelector("#input_sede_id");

const tablaSede = document.querySelector("#tabla_sede");
const tablaSedeAlmacen = document.querySelector("#tabla_sede_alm");

const botonGuardarSede = document.querySelector("#boton_Guardar_sede");
const botonBuscarAlm = document.querySelector("#boton_buscar_alm");

const indicesSedes = [];    //Se toma como referencia para poner los ID de la tabla en el front.



function agregarSedeEnTablaFront() {

    indicesSedes.push(campoSedeNombre);

    const fila = document.createElement("tr");
    const sedeId = document.createElement("td");
    sedeId.classList = "columna-id";
    const sedeNom = document.createElement("td");
    const sedeUb = document.createElement("td");

    sedeId.textContent = indicesSedes.length;
    sedeNom.textContent = campoSedeNombre.value;
    sedeUb.textContent = campoSedeUbicacion.value;

    fila.appendChild(sedeId);
    fila.appendChild(sedeNom);
    fila.appendChild(sedeUb);

    tablaSede.appendChild(fila);
}



function agregarSedeEnBaseDeDatos() {

    fetch("http://localhost:8080/guardar-sede", {

        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre_sede: campoSedeNombre.value,
            ubicacion: campoSedeUbicacion.value
        })
    })
        .then(function (resultado) {
            agregarSedeEnTablaFront();

            console.clear();
            console.log("Se registró la sede con éxito.");

            campoSedeNombre.value = "";
            campoSedeUbicacion.value = "";

            console.log(resultado);
            return resultado.json();
        })
        .then(function (datos) {
            console.log(datos);
        })
        .catch(function (error) {
            console.error("Error al guardar la sede.", error);
        });
}


function validarSedesRepetidas() {

    fetch("http://localhost:8080/obtener-sede-all", {
        method: "GET"
    })
        .then(function (resultado) {
            return resultado.json();
        })
        .then(function (sedes) {

            const sedeExistente = sedes.some(sede => sede.nombre_sede == campoSedeNombre.value);

            if (sedeExistente) {
                console.clear()
                console.log("Ya hay una sede registrada con ese nombre.");
                return;
            } else {
                agregarSedeEnBaseDeDatos();
            }
        })
}



function reiniciarTablaDeAlmacenes() {

    tablaSedeAlmacen.innerHTML = "";  // Limpia la tabla

    const filaTabla = document.createElement("thead");
    const id = document.createElement("th");
    id.classList = "columna-id";
    const nombre_almacen = document.createElement("th");

    id.textContent = "ID";
    nombre_almacen.textContent = "Nombre del almacen";

    filaTabla.appendChild(id);
    filaTabla.appendChild(nombre_almacen);

    tablaSedeAlmacen.appendChild(filaTabla);
}



function parseAlmacenString(almacenStr) {

    const parts = almacenStr.split(", ");
    const almacen = {};
    parts.forEach(function (part) {
        const [key, value] = part.split(": ");
        almacen[key.trim()] = value.trim();
    });
    return almacen;
}



/*console.log("http://localhost:8080/obtener-sede-id/" + campoSedeIdAlmacen.value);
console.log(`http://localhost:8080/obtener-sede-id/${campoSedeIdAlmacen.value}`)*/



function obtenerAlmacenesDeSedes() {

    reiniciarTablaDeAlmacenes()

    fetch(`http://localhost:8080/obtener-sede-id/${campoSedeId.value}`, {
        method: "GET"
    })
        .then(function (resultado) {
            console.clear();
            console.log(resultado);
            return resultado.json();
        })
        .then(function (sede) {
            console.log(sede);

            const arregloAlmacenes = sede.almacenes_asociados;

            arregloAlmacenes.forEach(function (almacenStr) {

                const almacen = parseAlmacenString(almacenStr);

                const filaAlm = document.createElement("tr");
                const sedeAlmId = document.createElement("td");
                sedeAlmId.classList = "columna-id";
                const sedeAlmNombre = document.createElement("td");

                sedeAlmId.textContent = almacen.id_almacen;
                sedeAlmNombre.textContent = almacen.nombre_almacen;

                filaAlm.appendChild(sedeAlmId);
                filaAlm.appendChild(sedeAlmNombre);

                tablaSedeAlmacen.appendChild(filaAlm);

            });
        });
}



function borrarTodasLasSedes() {

    fetch("http://localhost:8080/borrar-sede-all", {
        method: "DELETE"
    })
        .catch(function (error) {
            console.error('Error al borrar las sedes: ', error);
        });
}


botonGuardarSede.addEventListener("click", function () {
    if (campoSedeNombre.value.trim() === "" || campoSedeUbicacion.value.trim() === "") {
        console.clear();
        console.log("No pueden haber campos vacíos al registrar una sede.");
    } else {
        if (indicesSedes.length == 0) {
            agregarSedeEnBaseDeDatos();
        } else {
            validarSedesRepetidas();
        }

    }
})



botonBuscarAlm.addEventListener("click", function () {
    obtenerAlmacenesDeSedes();
    campoSedeIdAlmacen.value = "";

})



window.addEventListener("load", function () {
    borrarTodasLasSedes();
})
