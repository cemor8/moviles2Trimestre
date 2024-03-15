const express = require('express');
const fs = require("fs");
const mongoose = require('mongoose');
const app = express();
const port = 3000;
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const odooUrl = 'http://tu-instancia-bitnami-odoo.com';
const db = 'bitnami_odoo';
const username = 'bn_odoo';
const password = 'vMOVICOYTE129Zy5ohRw9uijlRnSEKre2j6c70w5IFh4jkhdZ9K9f0t5fKNJyC0H';
const uid = await authenticate();
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
async function createProduct(productData) {
    const uid = await authenticate();
    try {
        const response = await axios.post(`${odooUrl}/jsonrpc`, {
            jsonrpc: "2.0",
            method: "call",
            params: {
                service: "object",
                method: "execute_kw",
                args: [db, uid, password, 'product.template', 'create', [productData]],
            },
            id: Math.floor(Math.random() * 1000)
        });

        return response.data.result; // Esto devuelve el ID del producto creado
    } catch (error) {
        console.error('Error al crear el producto:', error);
        throw error;
    }
}

app.post('/product', async (req, res) => {
    try {
        const productId = await createProduct(req.body);
        res.send({ success: true, productId: productId });
    } catch (error) {
        res.status(500).send({ success: false, error: error.message });
    }
});

app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});