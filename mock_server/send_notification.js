/**
 * Simple server endpoint to send OneSignal notifications using REST key from env var.
 * Usage: POST /send with JSON body { "include_external_user_ids": ["partner"], "contents": {"en":"message"}, "headings": {"en":"Title"} }
 *
 * Requires: export ONESIGNAL_REST_KEY=your_key
 */
const express = require('express');
const fetch = require('node-fetch');
const app = express();
app.use(express.json());

app.post('/send', async (req, res) => {
  const key = process.env.ONESIGNAL_REST_KEY;
  if (!key) return res.status(500).json({error: "ONESIGNAL_REST_KEY not set in env"});
  const body = {
    app_id: process.env.ONESIGNAL_APP_ID || '02d91be9-005b-4470-9c62-f552a1ea2064',
    ...req.body
  };
  try {
    const resp = await fetch('https://onesignal.com/api/v1/notifications', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Basic ${key}` },
      body: JSON.stringify(body)
    });
    const data = await resp.json();
    res.json(data);
  } catch (err) {
    res.status(500).json({error: err.message});
  }
});

const port = process.env.PORT || 3001;
app.listen(port, ()=> console.log("OneSignal proxy running on "+port));
