const express = require('express');
const fs = require("fs");
const app = express();
const port = 3000;
const axios = require('axios');
const { privateDecrypt } = require('crypto');
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
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


app.listen(port, () => {
    console.log("Servidor levantado correctamente en el puerto", port);
});