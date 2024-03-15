const express = require('express');
const fs = require("fs");
const app = express();
const port = 3000;
const axios = require('axios');
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
const odooUrl = 'http://192.168.0.13';
const db = 'bitnami_odoo';
/**
 * 
 * const username = 'bn_odoo';
const password = 'vMOVICOYTE129Zy5ohRw9uijlRnSEKre2j6c70w5IFh4jkhdZ9K9f0t5fKNJyC0H';
 * 
 */
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
        console.log("autenticando")
        console.log(response.data);
        return response.data.result;
    } catch (error) {
        console.error('Error al autenticar:', error);
        throw error;
    }
}
async function createProduct(productData) {
    console.log("entre crear")
    const uid = await authenticate();
    try {
        console.log(db)
        console.log(uid)
        console.log(password)
        console.log(productData)
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
        console.log(response.data)
        return response.data.result; // Esto devuelve el ID del producto creado
    } catch (error) {
        console.error('Error al crear el producto:', error);
        throw error;
    }
}

app.post('/product', async (req, res) => {
    try {
        const productId = await createProduct(req.body);
        console.log("PRODUCTO CREADO CORRECTAMENTE")
        console.log(productId)
        res.send({ success: true, productId: productId });
    } catch (error) {
        res.status(500).send({ success: false, error: error.message });
    }
});

async function modifyProductByName(productName, updateData) {
    console.log("Entrando en modificar");
    const uid = await authenticate();
    try {
        // Paso 1: Buscar el ID del producto por nombre
        let searchResponse = await axios.post(`${odooUrl}/jsonrpc`, {
            jsonrpc: "2.0",
            method: "call",
            params: {
                service: "object",
                method: "execute_kw",
                args: [db, uid, password, 'product.template', 'search', [[['name', '=', productName]]]],
            },
            id: Math.floor(Math.random() * 1000)
        });
        
        const productIds = searchResponse.data.result;
        console.log("IDs encontrados:", productIds);

        if (productIds.length === 0) {
            console.log("Producto no encontrado.");
            return false; // Producto no encontrado
        }

        // Asumiendo que el nombre es único y solo obtenemos un ID
        const productId = productIds[0];

        // Paso 2: Modificar el producto usando el ID
        let updateResponse = await axios.post(`${odooUrl}/jsonrpc`, {
            jsonrpc: "2.0",
            method: "call",
            params: {
                service: "object",
                method: "execute_kw",
                args: [db, uid, password, 'product.template', 'write', [productId, updateData]],
            },
            id: Math.floor(Math.random() * 1000 + 1)
        });
        
        console.log(updateResponse.data);
        return updateResponse.data.result; // True si la operación fue exitosa
    } catch (error) {
        console.error('Error al modificar el producto:', error);
        throw error;
    }
}

app.post('/modify-product', async (req, res) => {
    const { productName, updateData } = req.body;

    if (!productName || !updateData) {
        return res.status(400).send({ success: false, message: "Falta el nombre del producto o los datos de actualización." });
    }

    try {
        const result = await modifyProductByName(productName, updateData);
        if (result) {
            res.send({ success: true, message: "Producto modificado con éxito." });
        } else {
            res.status(404).send({ success: false, message: "Producto no encontrado." });
        }
    } catch (error) {
        console.error('Error al modificar el producto:', error);
        res.status(500).send({ success: false, message: "Error interno del servidor." });
    }
});

app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});