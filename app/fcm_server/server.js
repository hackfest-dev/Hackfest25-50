const express = require('express');
const admin = require('firebase-admin');
const app = express();
const port = 3000;

const serviceAccount = require('./womensafety-firebase-adminsdk.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

app.use(express.json());

app.post('/send-notification', async (req, res) => {
    try {
        const { city } = req.body;
        const message = {
            notification: {
                title: 'New Incident Report',
                body: `A report has been posted near ${city || 'your area'}. Check it out.`
            },
            topic: 'community_reports'
        };
        const response = await admin.messaging().send(message);
        console.log('Notification sent:', response);
        res.status(200).send('Notification sent successfully');
    } catch (error) {
        console.error('Error sending notification:', error);
        res.status(500).send('Failed to send notification');
    }
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});