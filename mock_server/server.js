// Simple Express mock server serving JSON from assets for local dev
const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
app.use(express.json());
app.get('/mock/daily_questions', (req,res)=> res.sendFile(__dirname + '/daily_questions.json'));
app.get('/mock/secrets', (req,res)=> res.sendFile(__dirname + '/secrets.json'));
app.listen(port, ()=> console.log("Mock server listening on "+port));
