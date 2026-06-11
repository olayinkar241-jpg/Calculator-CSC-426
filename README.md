# CSC426 — Web-Based Login Authentication App

A complete, production-ready login authentication application built with **HTML, CSS, JavaScript** (frontend) and **Node.js + Express** (backend option).

---

## 🔗 Links

| | URL |
|---|---|
| **GitHub Repository** | `https://github.com/YOUR_USERNAME/CSC426` |
| **Deployed App (Netlify)** | `https://csc426-login.netlify.app` |
| **Deployed App (Vercel)** | `https://csc426-login.vercel.app` |

> Replace with your actual URLs after deploying.

---

## ✨ Features

| Feature | Details |
|---|---|
| **Username field** | Live validation — 3–20 chars, alphanumeric + `.` `_` `-` |
| **Password field** | Live validation, show/hide toggle, strength meter |
| **Sign in button** | Submits form with loading spinner animation |
| **Reset button** | Clears all fields and validation states |
| **Input validation** | Client-side on blur + on submit; server-side on `/api/login` |
| **Success message** | Animated checkmark overlay with username greeting |
| **Error messages** | Per-field inline errors + toast notifications |
| **Attempt limiting** | Max 5 client-side; rate-limited server-side (10/15 min) |
| **Password strength** | 4-bar colour-coded strength indicator |
| **Responsive UI** | Mobile-first, works 320px → 4K |
| **Accessibility** | ARIA labels, live regions, keyboard navigation |
| **Security headers** | `helmet.js` (backend) — XSS, CSP, HSTS |

---

## 🗂️ Project Structure

```
csc426-login/
├── index.html          ← Standalone frontend (works without a server)
├── server.js           ← Node.js/Express backend (optional)
├── package.json
├── netlify.toml        ← Netlify deploy config
├── vercel.json         ← Vercel deploy config
├── render.yaml         ← Render deploy config
├── .gitignore
└── docs/
    ├── README.md       ← This file
    └── REPORT.md       ← Technical report
```

---

## 🚀 Running Locally

### Option A — Pure static (no server needed)
Just open `index.html` in any browser. Everything runs client-side.

```bash
# macOS / Linux
open index.html

# or with VS Code Live Server extension
```

### Option B — Node.js backend
```bash
npm install
npm start
# → http://localhost:3000
```

---

## 🌐 Deployment

### Netlify (Recommended — 1 click)

1. Push this repo to GitHub
2. Go to [netlify.com](https://netlify.com) → **Add new site** → **Import from Git**
3. Select your repo
4. Build command: *(leave blank)*  |  Publish directory: `.`
5. Click **Deploy site**

### Vercel

```bash
npm i -g vercel
vercel --prod
```

### Render (Node.js backend)

1. Connect repo at [render.com](https://render.com) → **New Web Service**
2. Build command: `npm install`
3. Start command: `npm start`
4. Deploy

---

## 🧑‍💻 Demo Credentials

| Username | Password |
|---|---|
| `admin` | `Admin@123` |
| `student` | `Student#1` |
| `csc426` | `Portal@2024` |

> These are hardcoded for demonstration. In production, use a real database with **bcrypt-hashed passwords**.

---

## 🔐 Validation Rules

### Username
- Required
- 3–20 characters
- Allowed: `a-z A-Z 0-9 . _ -`

### Password
- Required
- Minimum 8 characters
- Strength meter: Weak / Fair / Good / Strong

### Login Attempts
- Client-side: 5 attempts before lockout
- Server-side (Node.js): 10 requests per 15 minutes per IP

---

## 🎨 Design

**Palette:** GitHub-inspired dark — `#0D1117` background, `#58A6FF` accent (blue), `#3FB950` success (green), `#F85149` error (red)

**Typography:** Space Grotesk (headings) + Inter (body) — both via Google Fonts

**Signature element:** Animated dot-grid background + springy toast notification system

---

## 🛡️ Security Notes

For a real production app:

1. Store passwords as **bcrypt hashes** (never plaintext)
2. Use **HTTPS** — all hosting platforms listed provide this free
3. Use **JWT or session tokens** for post-login state
4. Add **CSRF protection** (csurf middleware for Express)
5. Never expose which field (username vs password) was wrong
6. Add **email verification** and **2FA** for sensitive apps

---

## 📄 License

MIT — free to use and modify.
