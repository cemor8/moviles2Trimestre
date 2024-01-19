const express = require('express');
const fs = require("fs");
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const direccion = "mongodb://localhost:27017/Biblioteca";

mongoose.connect(direccion);

mongoose.connection.on('connected', () => {
    console.log('Conectado a MongoDB');
});

mongoose.connection.on('error', (err) => {
    console.log('Error al conectar a MongoDB', err);
});

const Schema = mongoose.Schema;
const model = mongoose.model;

const esquema = new Schema({
    fecha: String,
    paginas: Number,
    titulo: String
});

const libro = model('libro', esquema);


app.post('/api/libros', async (req, res) => {
    try {
        let libro = new libro(req.body);
        let guardarLibro = await libro.save();
        res.status(201).json(guardarLibro);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

app.get('/api/libros', async (req, res) => {
    try {
        let listaLibros = await libro.find();
        res.status(200).json(listaLibros);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

app.put('/api/libros/:titulo', async (req, res) => {
    try {
      let libroModificar = await libro.findByIdAndUpdate(req.params.titulo, req.body, { new: true });
      res.status(200).json(libroModificar);
    } catch (err) {
      res.status(500).json({ message: err.message });
    }
});

app.delete('/api/libros/:titulo', async (req, res) => {
    try {
      let libroBorrar = await libro.findByIdAndDelete(req.params.titulo);
      res.status(200).json(libroBorrar);
    } catch (err) {
      res.status(500).json({ message: err.message });
    }
});



app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});