const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

const app = express();
const port = 3001;

app.use(bodyParser.json());

let logs = [];

app.post('/webhook', (req, res) => {
    console.log('Webhook received:', req.body);
    logs.push(req.body);
    res.status(200).send('OK');
});

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.get('/logs', (req, res) => {
    res.json(logs);
});

app.listen(port, () => {
    console.log(`Log central listening at http://localhost:${port}`);
});
