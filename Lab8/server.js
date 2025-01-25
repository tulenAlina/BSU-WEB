//Install express server
const express = require('express');
const path = require('path');

const app = express();

app.use(express.static(`${__dirname}`));

app.get('/*', (req, res) =>   
    res.sendFile('index.html', {root: `${__dirname}`}),
);

app.get('/data', (req, res) => {
    res.download(`${__dirname}/data.json`);
  });

app.listen(process.env.PORT || 5000);