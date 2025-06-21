const express = require('express');
const cors = require('cors');
const fetch = require('node-fetch'); // si node < 18, sinon fetch natif
const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());

app.post('/api/auth/login', async (req, res) => {
  const { username, password } = req.body;

  try {
    const response = await fetch('https://nshop-six.vercel.app/api/users');
    const users = await response.json();

    const user = users.find(
      (u) => u.username === username && u.password === password
    );

    if (user) {
      res.json({ message: `Hello ${user.username}` });
    } else {
      res.status(401).json({ message: 'Identifiants invalides' });
    }
  } catch (error) {
    res.status(500).json({ message: 'Erreur serveur' });
  }
});

app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
