const express = require('express');
const fs = require("fs");
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
    ocupada: Boolean
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
const esquemaMenusMeter = new Schema({
    dia: String,
    primeros: esquemaPlato,
    segundos: esquemaPlato,
    bebidas: esquemaBebida,
    cantidad : Number,
    precio: Number
}, {
    versionKey: false
});
const esquemaPedidos = new Schema({
    id: Number,
    nombre_mesa: String,
    consumiciones: Array,
    menus : [esquemaMenusMeter],
    estado: String,
    precio: Number
}, {
    versionKey: false
});

const esquemaFactura = new Schema({
    id: Number,
    nombre_mesa: String,
    consumiciones: Array,
    estado: String,
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
    atendido : String,
    creada : Date
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
        let pedidoEncontrado = await pedido.findOne({id : req.params.id});
        res.status(200).json(pedidoEncontrado);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});
//#endregion get

//#region put

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

app.put('/api/mesa:nombre_mesa', async (req, res) => {
    try {
        console.log(req.body);
        await mesa.findOneAndUpdate({ titulo: req.params.nombre_mesa }, req.body);
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message);
        res.status(500).json({ message: err.message });
    }
});

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


app.put('/api/restarBebida/:nombre', async (req, res) => {
    let nombre = req.params.nombre
    let cantidadARestar = req.body.cantidad
    console.log(nombre)
    console.log(cantidadARestar)
    try {
        await menusDia.find({ "bebidas.nombre" : nombre})
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

app.put('/api/ocuparReserva/:nombreMesa', async (req, res) => {
    let nombreMesa = req.params.nombreMesa
    
    try {
            await reserva.findOneAndUpdate(
                { $or: [
                    { "mesas.nombre_mesa": nombreMesa },
                    { "mesas.sitios.nombre": nombreMesa }
                ]},
                { $set: { atendido: true } }
        );
        res.status(200).json({ message: "Reserva actualizada correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al actualizar la reserva", error: error });
    }
});

app.put('/api/meterMenu/:id', async (req, res) => {
    let id = req.params.id
    const { menu } = req.body;
    try {
            await pedido.findOneAndUpdate(
            { id: id },
            { $push: { menus: menu } }
        );
        res.status(200).json({ message: "Menu añadido correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al añadir el menu", error: error });
    }
});

app.put('/api/meterConsumicion/:id', async (req, res) => {
    let id = req.params.id
    const { consumicion } = req.body;
    try {
            await pedido.findOneAndUpdate(
            { id: id },
            { $push: { consumiciones: consumicion } }
        );
        res.status(200).json({ message: "Consumicion Añadida Correctamente" });
    } catch (error) {
        console.log(error.message)
        res.status(500).json({ message: "Error al añadir la consumicion", error: error });
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
            precio: 0,
            estador: "creado",
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

app.delete('/api/reservas/:dni', async (req, res) => {
    try {
        await reserva.findOneAndDelete({ dni: req.params.dni });
        console.log("bien");
        res.sendStatus(200);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});

//#endregion delete

const incrementCounter = async () => {
    const numero = await contador.findOneAndUpdate(
        { $inc: { valor: 1 } }
    );
    return numero.valor;
};

app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});