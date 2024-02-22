const express = require('express');
const fs = require("fs");
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const dbURI = 'mongodb+srv://cemor8:12q12q12@cluster0.3yldjuk.mongodb.net/';
mongoose.connect(dbURI);
mongoose.connection.on('connected', () => {
    console.log('Conectado a MongoDB');
});

mongoose.connection.on('error', (err) => {
    console.log('Error al conectar a MongoDB', err);
});
const Schema = mongoose.Schema;
const modelo = mongoose.model;
const esquemaSitio= new Schema({
    nombre: String,
    ocupada: Boolean
},{
    versionKey: false
});
const esquemaMesa = new Schema({
    nombre_mesa: String,
    ocupada: Boolean,
    ubicacion: String,
    sitios : [esquemaSitio]
},{
    versionKey: false
});

const esquemaBebida= new Schema({
    nombre: String,
    precio: Number
},{
    versionKey: false
});
const esquemaPlato= new Schema({
    nombre: String,
    precio: Number
},{
    versionKey: false
});
const esquemaMenusDia= new Schema({
    dia: String,
    primeros : Array,
    segundos : Array,
    bebidas : Array,
    precio: Number
},{
    versionKey: false
});
const esquemaPedidos= new Schema({
    id : Number,
    nombre_mesa: String,
    consumiciones : Array,
    estado : String,
    precio: Number
},{
    versionKey: false
});

const esquemaFactura= new Schema({
    id : Number,
    nombre_mesa: String,
    consumiciones : Array,
    estado : String,
    precio: Number
},{
    versionKey: false
});


const mesa = modelo('mesas', esquemaMesa);
const bebida = modelo("bebidas",esquemaBebida)
const plato = modelo("platos",esquemaPlato)
const menusDia = modelo("menusDia",esquemaMenusDia)
const pedido = modelo("pedidos",esquemaPedidos)
const factura = modelo("facturas",esquemaFactura)


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
//#endregion get

//#region put

app.put('/api/pedido:id', async (req, res) => {
    try {
        console.log(req.body);
        await pedido.findOneAndUpdate({ titulo: req.params.id }, req.body);
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



//#endregion put

//#region post

app.post('/api/pedidos', async (req, res) => {
    try {
        await new pedido(req.body).save();
        res.sendStatus(201);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});

app.post('/api/facturas', async (req, res) => {
    try {
        await new factura(req.body).save();
        res.sendStatus(201);
    } catch (err) {
        console.log(err.message)
        res.status(500).json({ message: err.message });
    }
});



//#endregion post

//#region delete
//#endregion delete


app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});