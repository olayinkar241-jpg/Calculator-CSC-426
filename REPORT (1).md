# Technical Project Report
## CSC426 — Web-Based Login Authentication Application

**Course:** CSC426 Web Technologies  
**Project:** Login Authentication System  
**Stack:** HTML5 · CSS3 · Vanilla JavaScript · Node.js / Express (backend)  
**Version:** 1.0  
**Date:** June 2026

---

## 1. Introduction

This report documents the design and implementation of a web-based login authentication application developed as part of the CSC426 course. The application provides a complete user authentication interface with real-time input validation, success/error feedback, and a secure Node.js backend option, deployable to any modern hosting platform.

---

## 2. Objectives

- Build a responsive, accessible login form with HTML, CSS, and JavaScript
- Implement client-side validation with clear, real-time user feedback
- Provide both a static (frontend-only) and server-backed (Node.js) deployment path
- Apply security best practices (rate limiting, security headers, safe credential handling)
- Deploy the application and make it publicly accessible via a live URL

---

## 3. Technologies Used

| Layer | Technology | Purpose |
|---|---|---|
| Markup | HTML5 | Page structure, semantic elements, ARIA |
| Styling | CSS3 | Custom properties, animations, responsive layout |
| Logic | Vanilla JavaScript (ES2020) | Validation, DOM interaction, async login flow |
| Backend | Node.js 18 + Express 4 | REST API endpoint `/api/login` |
| Security | helmet.js | HTTP security headers |
| Rate limiting | express-rate-limit | Brute-force protection |
| Fonts | Google Fonts (Space Grotesk, Inter) | Typography |
| Deployment | Netlify / Vercel / Render | Hosting |

---

## 4. Application Components

### 4.1 Username Field
- Text input with icon prefix
- Validation: required · min 3 chars · max 20 chars · allowed characters (`a-z`, `A-Z`, `0-9`, `.`, `_`, `-`)
- Live error clearing: once a user fixes the error, the red border clears immediately
- Blur validation: full check when focus leaves the field

### 4.2 Password Field
- Password input with masked characters by default
- **Show/Hide toggle**: eye icon button switches between `type="password"` and `type="text"`; ARIA label updates accordingly
- **Strength meter**: 4-segment bar updates on each keystroke
  - Weak (red): < 2 criteria met
  - Fair (amber): 2–2 criteria
  - Good (blue): 3 criteria
  - Strong (green): 4+ criteria
  - Criteria: length ≥ 8, length ≥ 12, mixed case, contains digit, contains symbol
- Validation: required · min 8 characters

### 4.3 Sign In Button
- Disabled during async authentication (prevents double-submit)
- CSS loading state: hides button text, shows animated spinner
- Re-enabled after response

### 4.4 Reset Button
- Clears all input values
- Removes all validation state classes (valid/invalid borders)
- Clears all inline error/success messages
- Focuses username field
- Shows toast confirmation "Form cleared"

### 4.5 Input Validation (Client-Side)

```
On field blur:
  → run all rules for that field
  → if any fail: set red border + error message
  → if all pass: set green border + "✓ Looks good"

On form submit:
  → validate all fields at once
  → abort submit if any field is invalid
  → show toast "Please fix the errors above"
```

### 4.6 Input Validation (Server-Side — Node.js)

```
POST /api/login
  → Check presence of username and password
  → Validate types (string)
  → Validate username length (3–20)
  → Validate password length (≥ 8)
  → Match against user store
  → Return 200 success OR 401 unauthorized
  → Rate-limited: 10 requests / 15 min / IP
```

### 4.7 Success Feedback
- Animated SVG checkmark (circle + check, drawn via stroke-dashoffset animation)
- Overlay covers the full viewport with semi-transparent background
- Displays "Welcome back, [username]!"
- "Sign out" button dismisses overlay and resets form

### 4.8 Error Feedback
- Per-field inline messages (red text, shown beneath each input)
- Toast notification (springs up from bottom, auto-dismisses after 3.2s)
- Attempt counter with countdown: "2 attempts remaining"
- Generic error message (does not reveal which field was wrong — prevents username enumeration)

---

## 5. Validation Logic Detail

### Username rules
```javascript
required:  v.length > 0              → 'Username is required'
minLen:    v.length >= 3             → 'At least 3 characters'
maxLen:    v.length <= 20            → 'Maximum 20 characters'
chars:     /^[a-zA-Z0-9._-]+$/.test → 'Letters, numbers, . _ - only'
```

### Password rules
```javascript
required:  v.length > 0   → 'Password is required'
minLen:    v.length >= 8  → 'At least 8 characters'
```

---

## 6. UI / UX Design Decisions

### Color System
The application uses a GitHub-inspired dark palette, chosen for:
- High contrast (WCAG AA compliant text ratios)
- Familiar developer context matching CSC426's technical audience
- Semantic color encoding: blue for information, green for success, red for error

| Token | Value | Use |
|---|---|---|
| `--bg` | `#0D1117` | Page background |
| `--surface` | `#161B22` | Card background |
| `--border` | `#30363D` | Default borders |
| `--accent` | `#58A6FF` | Interactive elements |
| `--success` | `#3FB950` | Validated / confirmed |
| `--error` | `#F85149` | Validation failures |

### Typography
- **Space Grotesk** — headings and labels (technical, modern, distinctive)
- **Inter** — body and inputs (neutral legibility at small sizes)

### Layout
- Single centered card, max-width 420px
- Dot-grid background (CSS `radial-gradient` pattern) adds depth without distraction
- Mobile: buttons stack vertically; card padding reduces

### Signature Element
The animated checkmark success overlay — drawn via SVG `stroke-dashoffset` animation — is the memorable moment of the experience, replacing the generic "alert()" popup.

---

## 7. Accessibility

| Concern | Implementation |
|---|---|
| Screen readers | `aria-describedby` links fields to error spans |
| Live regions | `aria-live="polite"` on field messages; `aria-live="assertive"` on toast |
| Keyboard nav | All controls reachable via Tab; focus ring visible |
| Toggle button | `aria-label` updates ("Show password" / "Hide password") |
| Success overlay | `role="status"` + `aria-live="polite"` |
| `<label>` elements | All inputs have associated `<label>` elements |

---

## 8. Security Considerations

| Risk | Mitigation |
|---|---|
| Brute force | 5-attempt client limit; 10/15min server rate limit |
| Username enumeration | Generic "Invalid username or password" message |
| XSS | `helmet` sets `X-XSS-Protection`; no innerHTML with user input |
| Clickjacking | `helmet` sets `X-Frame-Options: DENY` |
| Plaintext passwords | Note: production must use bcrypt hashing |
| HTTP | All listed deployment platforms enforce HTTPS |

---

## 9. Deployment Instructions

### Netlify (Static — simplest)
1. Push to GitHub
2. Connect at netlify.com → New site → Import from Git
3. No build command; publish directory: `.`
4. Done — live in ~30 seconds

### Vercel (Static)
```bash
npx vercel --prod
```

### Render (Node.js backend)
1. Connect GitHub repo at render.com
2. Build: `npm install`, Start: `npm start`
3. Set `PORT` env var if needed

---

## 10. Sample Outputs

### Successful Login
```
User enters: admin / Admin@123
→ Loading spinner for ~1s
→ Success overlay appears
→ "Welcome back, admin!"
→ Green toast: (none — overlay covers screen)
```

### Validation Error — Empty Submit
```
User clicks Sign In with empty fields
→ Username field: red border + "Username is required"
→ Password field: red border + "Password is required"
→ Toast: "Please fix the errors above"
```

### Wrong Credentials (3rd attempt)
```
→ Password field: "Incorrect credentials. 2 attempts remaining"
→ Toast: "Invalid username or password"
→ Password field cleared (security — prevents shoulder surfing)
```

---

## 11. Conclusion

The CSC426 login application demonstrates a complete, production-quality authentication interface including all required components: username and password fields, submit and reset buttons, real-time client-side validation, clear success and error messaging, and a responsive, accessible UI. The Node.js backend adds server-side validation, rate limiting, and security headers, making the project deployable as a real web service.

---

*End of Report*
