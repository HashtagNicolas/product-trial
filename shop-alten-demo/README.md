# shop-alten-demo back-end
Le projet back-end est un projet java réalisé avec springboot et une base de donnée Postgres

# swagger
http://localhost:8080/swagger-ui/index.html

# 🧪 Stratégie de tests API

Ce document décrit la stratégie de tests fonctionnels 
et techniques pour l’API (authentification, gestion des comptes, produits, panier et wishlist).

---

## 1. Authentification et gestion des utilisateurs

### POST `/api/auth/register` – Enregistrement utilisateur

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 1.1  | Enregistrer un utilisateur avec email `admin@admin.com` | ✅ OK |
| 1.2  | Enregistrer un utilisateur avec email `user@user.com` | ✅ OK |
| 1.3  | Email non conforme | ❌ KO |
| 1.4  | Sans mot de passe | ❌ KO |
| 1.5  | Email déjà existant | ❌ KO |
| 1.6  | Mot de passe < 6 caractères | ❌ KO |

### POST `/api/auth/login` – Connexion utilisateur

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 2.1  | Login admin (email + password corrects) | ✅ Token OK |
| 2.2  | Login user (email + password corrects) | ✅ Token OK |
| 2.3  | Email non enregistré | ❌ KO |
| 2.4  | Mot de passe incorrect | ❌ KO |
| 2.5  | Email vide ou format invalide | ❌ KO |

---

## 2. Gestion du compte

### GET `/api/account?email=xxx` – Consultation compte

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 3.1  | Email = subject du token | ✅ OK |
| 3.2  | Email ≠ subject du token | ❌ KO |
| 3.3  | Token expiré | ❌ 401 |
| 3.4  | Token mal formé | ❌ 401 |

---

## 3. Gestion des produits

### GET `/api/products` – Liste des produits

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 4.1  | Récupérer tous les produits | ✅ OK |
| 4.2  | Vérifier `quantity >= 0` | ✅ OK |
| 4.3  | Vérifier `price >= 0` | ✅ OK |
| 4.4  | Filtrage par catégorie ou nom | ✅ OK |

### GET `/api/products/{id}` – Détail produit

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 4.5  | Produit existant | ✅ OK |
| 4.6  | Produit inexistant | ❌ KO |
| 4.7  | Vérifier `quantity >= 0` | ✅ OK |
| 4.8  | Vérifier `price >= 0` | ✅ OK |

### POST `/api/products/admin` – Création produit (admin)

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 4.9  | `quantity > 0` et `price > 0` | ✅ 201 Created |
| 4.10 | `quantity < 0` | ❌ KO |
| 4.11 | `price < 0` | ❌ KO |
| 4.12 | Sans nom ou code | ❌ KO |
| 4.13 | Rôle ≠ admin | ❌ KO |

### PUT `/api/products/admin/{id}` – Mise à jour produit

| Test | Description | Résultat attendu |
|------|------------|-----------------|
| 4.14 | `quantity >= 0` et `price >= 0` | ✅ OK |
| 4.15 | `quantity < 0` | ❌ KO |
| 4.16 | `price < 0` | ❌ KO |
| 4.17 | Produit inexistant | ❌ KO |
| 4.18 | Rôle ≠ admin | ❌ KO |

### DELETE `/api/products/admin/{id}` – Suppression produit

| Test | Description | Résultat attendu |
|------|------------|------------------|
| 4.19 | Produit existant | ✅ OK             |
| 4.20 | Produit inexistant | ❌ KO             |
| 4.21 | Rôle ≠ admin | ❌ KO             |

---

## 4. Gestion du panier

### PUT `/api/cart/{id}` – Ajout / modification / suppression

| Test | Description | Résultat attendu |
|------|------------|------------------|
| 5.1  | Ajouter produit valide | ✅ OK             |
| 5.2  | Modifier quantité produit | ✅ OK             |
| 5.3  | Supprimer produit | ✅ OK             |
| 5.4  | Déduction stock après ajout | ✅ OK             |
| 5.5  | Produit inexistant | ❌ KO             |
| 5.6  | Quantité demandée > stock | ❌ KO             |
| 5.7  | Quantité = 0 → suppression | ✅ OK             |
| 5.8  | Produit `OUTOFSTOCK` | ❌ KO             |
| 5.9  | Ajouter plusieurs fois sans dépasser stock | ✅ OK             |
| 5.10 | Token absent / expiré | ❌ KO             |

---

## 5. Gestion de la wishlist

### PUT `/api/wishlist/{id}` – Ajout / modification / suppression

| Test | Description | Résultat attendu |
|------|------------|------------------|
| 6.1  | Ajouter produit valide | ✅ OK             |
| 6.2  | Modifier produit | ✅ OK             |
| 6.3  | Supprimer produit | ✅ OK             |
| 6.4  | Produit inexistant | ❌ KO             |
| 6.5  | Produit déjà présent → pas de doublon | ✅ OK             |
| 6.6  | Token absent / expiré | ❌ KO             |

---

## 6. Cas limites & sécurité

| Test | Description | Résultat attendu |
|------|------------|------------------|
| 7.1  | Token expiré | ❌ KO             |
| 7.2  | Accès non autorisé sur endpoint admin | ❌ KO             |
| 7.3  | Vérifier cohérence des stocks après ajouts/suppressions multiples | ✅ OK             |

---