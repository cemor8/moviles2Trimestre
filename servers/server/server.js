const express = require('express');
const fs = require("fs");
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const mongo_URI = "mongodb://localhost:27017/Biblioteca"
mongoose.connect(mongo_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true
});

mongoose.connection.on('connected', () => {
    console.log('Conectado a MongoDB');
});

mongoose.connection.on('error', (err) => {
    console.log('Error al conectar a MongoDB', err);
});
const { Schema, model } = mongoose;

const esquema = new Schema({
    fecha: String,
    paginas: Number,
    titulo: String
});

const libro = model('libro', esquema);


app.post('/api/libros', async (req, res) => {
    try {
        const newItem = new libro(req.body);
        const savedItem = await newItem.save();
        res.status(201).json(savedItem);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

app.get('/api/libros', async (req, res) => {
    try {
        const items = await libro.find();
        res.status(200).json(items);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

app.put('/api/libros/:id', async (req, res) => {
    try {
      const updatedItem = await libro.findByIdAndUpdate(req.params.id, req.body, { new: true });
      res.status(200).json(updatedItem);
    } catch (err) {
      res.status(500).json({ message: err.message });
    }
});

app.delete('/api/libros/:id', async (req, res) => {
    try {
      const deletedItem = await libro.findByIdAndDelete(req.params.id);
      res.status(200).json(deletedItem);
    } catch (err) {
      res.status(500).json({ message: err.message });
    }
});



app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});