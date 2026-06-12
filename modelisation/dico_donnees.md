### Table `favorites`
 
| Attribut    | Type SQLite | Type Kotlin | Cle | Nullable | Description |
|-------------|-------------|-------------|-----|----------|-------------|
| breedId     | TEXT        | String      | PK  | non      | Identifiant de la race (fourni par The Cat API, ex. "beng") |
| name        | TEXT        | String      |     | non      | Nom de la race au moment de l'enregistrement |
| imageId     | TEXT        | String?     |     | oui      | Identifiant de l'image de reference (utilise pour construire l'URL CDN) |
| rating      | REAL        | Float       |     | non      | Note attribuee par l'utilisateur (0 a 5, 0 = non note) |
| isFavorite  | INTEGER     | Boolean     |     | non      | Indique si la race est marquee en favori (0 = non, 1 = oui) |
 
### Table `my_cats`
 
| Attribut         | Type SQLite | Type Kotlin | Cle | Nullable | Description |
|------------------|-------------|-------------|-----|----------|-------------|
| id               | INTEGER     | Long        | PK  | non      | Identifiant auto-incremente du profil de chat |
| name             | TEXT        | String      |     | non      | Nom donne au chat par l'utilisateur |
| breedId          | TEXT        | String?     | FK  | oui      | Reference vers une race catApi (NULL si race personnalisee) |
| customBreedName  | TEXT        | String?     |     | oui      | Nom de race libre, utilise si `breedId` est NULL |
| photoUri         | TEXT        | String?     |     | oui      | URI locale de la photo du chat (galerie de l'appareil) |
| age              | INTEGER     | Int?        |     | oui      | Age du chat, stocke en mois (converti depuis l'unite saisie : mois ou annees) |
| weight           | REAL        | Float?      |     | oui      | Poids du chat en kilogrammes |
| notes            | TEXT        | String?     |     | oui      | Notes libres saisies par l'utilisateur |
 
### Entite externe `BREED` (The Cat API - non persistee)
 
| Attribut          | Type API (JSON)       | Type Kotlin | Description |
|-------------------|-----------------------|-------------|-------------|
| id                | string                | String      | Identifiant unique de la race |
| name              | string                | String      | Nom de la race |
| origin            | string                | String?     | Pays d'origine |
| description       | string                | String?     | Description de la race |
| wikipedia_url     | string                | String?     | Lien vers la fiche Wikipedia |
| reference_image_id| string                | String?     | Identifiant de l'image de reference (CDN) |
| affection_level   | integer (1-5)         | Int?        | Niveau d'affection |
| energy_level      | integer (1-5)         | Int?        | Niveau d'energie |
| weight.metric     | string (ex. "4 - 6")  | String?     | Plage de poids en kilogrammes |
| weight.imperial   | string                | String?     | Plage de poids en livres |

