# CatAllant 🐱

Application Android (Kotlin / Jetpack Compose) permettant de découvrir des races de chats via [The Cat API](https://thecatapi.com), de les noter, de les ajouter en favoris, et de gérer un espace personnel pour ses propres chats.

## Fonctionnalités

- **Accueil** : découverte aléatoire d'une race de chat à chaque clic
- **Liste des races** : recherche par nom, tri (alphabétique, par note, favoris d'abord)
- **Fiche détail** : description, origine, poids, traits de caractère (affection, énergie), notation par étoiles, lien Wikipedia
- **Favoris** : races marquées comme favorites, synchronisées sur tous les écrans
- **Mes chats** : ajout/édition/suppression de profils de chats personnels (nom, race catApi ou personnalisée, photo, âge, poids, notes)
- **Mode sombre** : adaptatif selon le thème système
- **Persistance locale** : favoris, notes et profils de chats stockés en SQLite, conservés après fermeture de l'application

## Stack technique

- **Langage** : Kotlin
- **UI** : Jetpack Compose (Material 3)
- **Navigation** : Navigation Compose
- **Réseau** : Retrofit + Moshi
- **Images** : Coil
- **Persistance** : SQLite (SQLiteOpenHelper)
- **API externe** : [The Cat API](https://thecatapi.com)

## Prérequis

- Android Studio (version récente, AGP 9.x)
- JDK 11+
- Un compte gratuit sur [thecatapi.com](https://thecatapi.com/signup) pour obtenir une clé API

## Installation

1. Clone le projet :
   ```bash
   git clone <url-du-repo>
   ```

2. - Sur Cat API, crée un compte et récupère ta clé API gratuite.
   - Crée un fichier `local.properties` à la racine du projet (non versionné) et ajoute ta clé API :
      ```properties
      CAT_API_KEY=ta_cle_api_ici
      ```

      Un fichier d'exemple `local.properties.example` est fourni comme modèle.

3. Ouvre le projet dans Android Studio et laisse Gradle synchroniser.

4. Lance l'application sur un émulateur ou un appareil physique (minSdk 26).

## Structure du projet

```
app/src/main/java/com/cfa/cda/catapp/
├── data/
│   ├── api/            # Configuration Retrofit + interface API
│   ├── db/              # Base de données SQLite (favoris, notes, mes chats)
│   ├── model/           # Modèles de données (Breed, Weight...)
│   └── repository/      # Repository centralisant API + base locale
├── navigation/          # Graphe de navigation et routes
└── ui/
    ├── breeds/           # Écran Liste des races
    ├── components/      # Composants réutilisables (cards, images, etc.)
    ├── detail/           # Écran Détail d'une race
    ├── favorites/        # Écran Favoris
    ├── home/             # Écran Accueil
    ├── mycats/           # Écran Mes chats (liste + formulaire)
    └── theme/            # Couleurs, typographie, thème clair/sombre
```

## Notes

- Les images de races sont chargées dynamiquement depuis le CDN de The Cat API, avec gestion de plusieurs extensions (jpg, png, gif) en cas d'échec.
- Le compteur de "vues" affiché sur la fiche détail est une valeur factice (générée de manière déterministe à partir de l'identifiant de la race), l'API ne fournissant pas cette information.