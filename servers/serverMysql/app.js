const express = require('express');
const mysql = require('mysql');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));

const base = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'biblioteca'
});

base.connect((err) => {
    if (err) {
        throw err;
    }
    console.log('Conectado a la base de datos MySQL');
});

app.get('/api/libros', (req, res) => {
    const sql = 'select * from libro';
    base.query(sql, (err, result) => {
        if (err) {
            throw err;
        }
        res.send(result);
    });
});

app.post('/api/libros', (req, res) => {
    let libro = req.body;
    console.log(libro);
    let sql = 'INSERT INTO libro (titulo, autor, paginas, fecha) VALUES (?, ?, ?, ?)';
    let valores = [libro.titulo, libro.autor, libro.paginas, libro.fecha];

    base.query(sql, valores, (err, result) => {
        if (err) {
            console.log(err.message);
            throw err;
        }
        res.sendStatus(200);
    });
});

app.put('/api/libros/:titulo', (req, res) => {
    const titulo = req.params.titulo;
    const updatedItem = {
        titulo: req.body.titulo,
        autor: req.body.autor,
        paginas: req.body.paginas,
        fecha: req.body.fecha,
    };
    const sql = `update libro set ? where titulo = "${titulo}"`;
    base.query(sql, updatedItem, (err, result) => {
        if (err) {
            throw err;
        }
        res.send('Libro actualizado');
    });
});

app.delete('/api/libros/:titulo', (req, res) => {
    const titulo = req.params.titulo;

    const sql = `delete from libro where titulo = "${titulo}"`;
    base.query(sql, (err, result) => {
        if (err) {
            throw err;
        }
        res.send('Libro eliminado');
    });
});


app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});