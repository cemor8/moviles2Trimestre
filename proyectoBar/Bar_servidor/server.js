const express = require('express');
const fs = require("fs");
const axios = require('axios');
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const dbURI = 'mongodb+srv://cemor8:12q12q12@cluster0.3yldjuk.mongodb.net/bar?retryWrites=true&w=majority';
mongoose.connect(dbURI);
mongoose.connection.on('connected', () => {
    console.log('Conectado a MongoDB');
});

mongoose.connection.on('error', (err) => {
    console.log('Error al conectar a MongoDB', err);
});
const Schema = mongoose.Schema;
const modelo = mongoose.model;
const esquemaSitio = new Schema({
    nombre: String,
    ocupado: Boolean
}, {
    versionKey: false
});
const esquemaMesa = new Schema({
    nombre_mesa: String,
    ocupada: Boolean,
    ubicacion: String,
    sitios: [esquemaSitio]
}, {
    versionKey: false
});

const esquemaBebida = new Schema({
    nombre: String,
    precio: Number,
    cantidad: Number
}, {
    versionKey: false
});
const esquemaPlato = new Schema({
    nombre: String,
    precio: Number,
    cantidad: Number
}, {
    versionKey: false
});
const esquemaMenusDia = new Schema({
    dia: String,
    primeros: [esquemaPlato],
    segundos: [esquemaPlato],
    bebidas: [esquemaBebida],
    precio: Number
}, {
    versionKey: false
});
const esquemaPedidos = new Schema({
    id: Number,
    nombre_mesa: String,
    consumiciones: Array,
    menus: Array,
    estado: String,
    precio: Number
}, {
    versionKey: false
});

const esquemaFactura = new Schema({
    id: Number,
    nombre_mesa: String,
    consumiciones: Array,
    menus: Array,
    precio: Number
}, {
    versionKey: false
});
const contador = mongoose.model('contadores', new mongoose.Schema({
    _id: { type: String, required: true },
    valor: { type: Number, default: 0 }
}));

const esquemaReserva = new Schema({
    dni: String,
    mesas: [esquemaMesa],
    atendido: String,
    creada: Date
})


const mesa = modelo('mesas', esquemaMesa);
const bebida = modelo("bebidas", esquemaBebida)
const plato = modelo("platos", esquemaPlato)
const menusDia = modelo("menusdias", esquemaMenusDia)
const pedido = modelo("pedidos", esquemaPedidos)
const factura = modelo("facturas", esquemaFactura)
const reserva = modelo("reservas", esquemaReserva)


//#region get

app.get('/api/mesas', async (req, res) => {
    try {
        let listaMesas = await mesa.find();
        res.status(200).json(listaMesas);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
app.get('/api/bebidas', async (req, res) => {
    try {
        let listaBebidas = await bebida.find();
        res.status(200).json(listaBebidas);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
app.get('/api/platos', async (req, res) => {
    try {
        let listaPlatos = await plato.find();
        console.log(listaPlatos)
        res.status(200).json(listaPlatos);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
app.get('/api/menusDia', async (req, res) => {
    try {
        let listaMenus = await menusDia.find();
        res.status(200).json(listaMenus);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
app.get('/api/pedidos', async (req, res) => {
    try {
        let listaPedidos = await pedido.find();
        res.status(200).json(listaPedidos);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
app.get('/api/pedido/:id', async (req, res) => {
    try {
        let pedidoEncontrado = await pedido.findOne({ id: req.params.id });
        res.status(200).json(pedidoEncontrado);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
//#endregion get

//#region put
/*
Método que actualiza el pedido con los datos pasados
*/
app.put('/api/pedido/:id', async (req, res) => {
    try {
        console.log(req.body);
        await pedido.findOneAndUpdate({ id: req.params.id }, req.body);
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message);
        res.status(500).json({ message: err.message });
    }
});

/*
Método que se encarga de restar la cantidad de platos de la base de datos
cuyo nombre sea igual al establecido
*/
app.put('/api/restarPlatos/:nombre', async (req, res) => {
    let nombre = req.params.nombre
    let cantidadARestar = req.body.cantidad
    console.log(nombre)
    console.log(cantidadARestar)
    try {
        await menusDia.find({ $or: [{ "primeros.nombre": nombre }, { "segundos.nombre": nombre }] })
            .then(docs => {
                docs.forEach(doc => {
                    doc.primeros.forEach(plato => {
                        if (plato.nombre === nombre) {
                            plato.cantidad -= cantidadARestar;
                        }
                    });
                    doc.segundos.forEach(plato => {
                        if (plato.nombre === nombre) {
                            plato.cantidad -= cantidadARestar;
                        }
                    });
                    doc.save();
                });
            })

        await plato.find({ "nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {

                    doc.cantidad -= cantidadARestar;
                    doc.save();
                });
            });
        res.status(200).json({ message: "Cantidad actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la cantidad del plato", error: error });
    }
});

/*
Método que se encarga de restar la cantidad de platos de la base de datos
cuyo nombre sea igual al establecido
*/
app.put('/api/restarBebida/:nombre', async (req, res) => {
    let nombre = req.params.nombre
    let cantidadARestar = req.body.cantidad
    console.log(nombre)
    console.log(cantidadARestar)
    try {
        await menusDia.find({ "bebidas.nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {
                    doc.bebidas.forEach(bebida => {
                        if (bebida.nombre === nombre) {
                            bebida.cantidad -= cantidadARestar;
                        }
                    });
                    doc.save();
                });
            })

        await bebida.find({ "nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {
                    doc.cantidad -= cantidadARestar;
                    doc.save();
                });
            });
        res.status(200).json({ message: "Cantidad actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la cantidad de la bebida", error: error });
    }
});
/*
Método que se encarga de sumar la cantidad de platos de la base de datos
cuyo nombre sea igual al establecido
*/
app.put('/api/sumarPlatos/:nombre', async (req, res) => {
    let nombre = req.params.nombre
    let cantidadARestar = req.body.cantidad
    console.log(nombre)
    console.log(cantidadARestar)
    try {
        await menusDia.find({ $or: [{ "primeros.nombre": nombre }, { "segundos.nombre": nombre }] })
            .then(docs => {
                docs.forEach(doc => {
                    doc.primeros.forEach(plato => {
                        if (plato.nombre === nombre) {
                            plato.cantidad += cantidadARestar;
                        }
                    });
                    doc.segundos.forEach(plato => {
                        if (plato.nombre === nombre) {
                            plato.cantidad += cantidadARestar;
                        }
                    });
                    doc.save();
                });
            })

        await plato.find({ "nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {

                    doc.cantidad -= cantidadARestar;
                    doc.save();
                });
            });
        res.status(200).json({ message: "Cantidad actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la cantidad del plato", error: error });
    }
});

/*
Método que se encarga de sumar la cantidad de bebidas de la base de datos
cuyo nombre sea igual al establecido
*/
app.put('/api/sumarBebida/:nombre', async (req, res) => {
    let nombre = req.params.nombre
    let cantidadARestar = req.body.cantidad
    console.log(nombre)
    console.log(cantidadARestar)
    try {
        await menusDia.find({ "bebidas.nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {
                    doc.bebidas.forEach(bebida => {
                        if (bebida.nombre === nombre) {
                            bebida.cantidad += cantidadARestar;
                        }
                    });
                    doc.save();
                });
            })

        await bebida.find({ "nombre": nombre })
            .then(docs => {
                docs.forEach(doc => {
                    doc.cantidad += cantidadARestar;
                    doc.save();
                });
            });
        res.status(200).json({ message: "Cantidad actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la cantidad de la bebida", error: error });
    }
});


/*
Método que se encarga de ocupar la reserva en una base de datos
*/
app.put('/api/ocuparReserva/:nombreMesa', async (req, res) => {
    let nombreMesa = req.params.nombreMesa

    try {
        let resultado = await reserva.findOneAndUpdate(
            {
                $or: [
                    { "mesas.nombre_mesa": nombreMesa },
                    { "mesas.sitios.nombre": nombreMesa }
                ]
            },
            { $set: { atendido: true } }
        );
        
        if (!resultado) { // Si no se encuentra la reserva, resultado será null
            console.log("No se encontró la reserva especificada.");
            return res.status(500).json({ message: "No se encontró la reserva especificada." });
        }
        
        res.status(200).json({ message: "Reserva actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la reserva", error: error });
    }
});


/*
Método que se encarga de liberar una mesa despues de que el cliente acabe
*/
app.put('/api/liberar/:nombreMesa', async (req, res) => {
    let nombreMesa = req.params.nombreMesa
    try {
        let resultadoSitio = await mesa.findOneAndUpdate(
            { "sitios.nombre": nombreMesa },
            { $set: { "sitios.$.ocupado": false } }
        );

        if (resultadoSitio) {
            return res.status(200).json({ message: "Sitio liberado correctamente" });
        }
        let resultadoMesa = await mesa.findOneAndUpdate(
            { nombre_mesa: nombreMesa },
            { $set: { ocupada: false } }
        );

        if (resultadoMesa) {
            return res.status(200).json({ message: "Mesa liberada correctamente" });
        }

        
        res.status(404).json({ message: "No se encontró la mesa o sitio con el nombre proporcionado." });
    } catch (error) {
        console.log(error.message);
        res.status(500).json({ message: "Error al actualizar la reserva", error: error });
    }
});


//#endregion put

//#region post

app.post('/api/facturas', async (req, res) => {
    try {
        await new factura(req.body).save();
        res.sendStatus(201);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});

app.post('/api/pedidos', async (req, res) => {
    try {
        const nextId = await incrementCounter('contadores');
        const nuevoPedido = new pedido({
            id: nextId,
            nombre_mesa: "",
            consumiciones: [],
            menus: [],
            estado: "libre",
            precio: 0
        });
        await nuevoPedido.save();
        res.json(nuevoPedido);
    } catch (error) {
        console.error(error);
        res.status(500).send("Error en el servidor al crear un pedido");
    }
});




//#endregion post

//#region delete
/*
Método que se encarga de eliminar la reserva de la base de datos
*/
app.delete('/api/reservas/:nombreMesa', async (req, res) => {
    try {
        await reserva.findOneAndDelete(
            {
                $or: [
                    { "mesas.nombre_mesa": req.params.nombreMesa },
                    { "mesas.sitios.nombre": req.params.nombreMesa }
                ]
            },
        );
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});
/*
Método que elimina un pedido de la base de datos
*/
app.delete('/api/pedido/:id', async (req, res) => {
    try {
        console.log("eliminandoPedido")
        await pedido.findOneAndDelete({ id: req.params.id });
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});

//#endregion delete


//#region odoo

const odooUrl = 'http://192.168.1.42';
const db = 'bitnami_odoo';
const username = 'carlos@gmail.com';
const password = '12q12q12';
async function authenticate() {
    try {
        const response = await axios.post(`${odooUrl}/jsonrpc`, {
            jsonrpc: "2.0",
            method: "call",
            params: {
                service: "common",
                method: "login",
                args: [db, username, password],
            },
            id: Math.floor(Math.random() * 1000)
        });
        return response.data.result;
    } catch (error) {
        console.error('Error al autenticar:', error);
        throw error;
    }
}


app.post('/factura', async (req, res) => {

    const { productos, nombreCliente } = req.body;

    try {
        const uid = await authenticate();
        const clienteId = await buscarIdCliente(nombreCliente, uid);
        if (!clienteId) return res.status(404).send({ success: false, message: "Cliente no encontrado." });
        const lineas = [];
        for (let producto of productos) {
            console.log(producto)
            let productoInfo = await buscarIdProducto(producto.nombre, uid);
            if (!productoInfo) {
                console.error(`Producto no encontrado: ${producto.nombre}`);
                continue;
            }
            lineas.push([0, 0, {
                'product_id': productoInfo.id,
                'product_template_id': producto.nombre,
                'product_uom_qty': producto.cantidad,
                'price_unit': productoInfo.precio
            }]);
        }
        if (lineas.length > 0) {
            console.log(clienteId)
            console.log("lineas")
            console.log(lineas)
            const response = await axios.post(`${odooUrl}/jsonrpc`, {
                jsonrpc: "2.0",
                method: "call",
                params: {
                    service: "object",
                    method: "execute_kw",
                    args: [
                        db,
                        uid,
                        password,
                        "sale.order",
                        "create",
                        [{
                            'partner_id': clienteId,
                            'order_line': lineas
                        }]
                    ],
                    id: Math.floor(Math.random() * 1000)
                }
            });
            if (response.data.error) {
                console.error('Error de Odoo:', response.data.error);
                return res.status(500).send({
                    success: false,
                    message: "Error al crear la factura en Odoo.",
                    odooError: response.data.error
                });
            }
            res.send({ success: true, message: "Factura creada con éxito.", facturaId: response.data.result });
        } else {
            res.status(400).send({ success: false, message: "No se pudo crear la factura. Verifique los productos." });
        }
    } catch (error) {
        console.error('Error al eliminar el producto:', error);
        res.status(500).send({ success: false, message: "Error al eliminar el producto." });
    }
});


async function buscarIdCliente(nombreCliente, uid) {
    try {
        const response = await axios.post(`${odooUrl}/jsonrpc`, {
            jsonrpc: "2.0",
            method: "call",
            params: {
                service: "object",
                method: "execute_kw",
                args: [
                    db,
                    uid,
                    password,
                    'res.partner',
                    'search',
                    [[['name', '=', nombreCliente], ['is_company', '=', false]]],
                    { limit: 1 }
                ],
            },
            id: Math.floor(Math.random() * 1000)
        });

        const clienteIds = response.data.result
        if (clienteIds.length > 0) {
            return clienteIds[0];
        } else {
            console.log('Cliente no encontrado.');
            return null;
        }
    } catch (error) {
        console.error('Error buscando el cliente:', nombreCliente, error);
        return null;
    }
}

async function buscarIdProducto(nombreProducto, uid) {
    const response = await axios.post(`${odooUrl}/jsonrpc`, {
        jsonrpc: "2.0",
        method: "call",
        params: {
            service: "object",
            method: "execute_kw",
            args: [db, uid, password, 'product.product', 'search_read', [[['name', "ilike", nombreProducto], ['active', '=', true]]], { limit: 1 }],
        },
        id: Math.floor(Math.random() * 1000)
    });

    const products = response.data.result;
    console.log("productos")
    console.log(products)
    if (products.length > 0) {
        return { id: products[0].id, precio: products[0].list_price };
    } else {
        return null;
    }
}

//#endregion odoo


const incrementCounter = async () => {
    const numero = await contador.findOneAndUpdate(
        { $inc: { valor: 1 } }
    );
    return numero.valor;
};

app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});