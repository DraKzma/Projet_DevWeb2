# Projet_DevWeb2

Projet de l'UE DevWeb2 pour le semestre 6 de la L3 info.

## Pour setup le projet

- Cloner le repo dans votre workspace Eclipse
- Avec un terminal, se déplacer dans le repo clonee
- lancer mysql
- executer les fichier sql avec:
SOURCE sql/creation.sql; SOURCE sql/insertion.sql;
- verifier que tout est bon avec SHOW TABLES; DESCRIBE users; SELECT * FROM users;
- Ajouter le projet à votre serveur Tomcat (Add and remove)
- Ajouter les pilotes JDBC à votre projet (voir énoncer du TP5, première partie)

## Roadmap

**Fait:**
- Inscriptions/Connexions avec messages d'erreurs adaptees
- page DocumentChoice pour Creer/Trouver un document (Page non vierge mais ne fait rien à part rediriger sur Editor avec le bon document en parametre pour l'instant)
- page Editor vierge

**A faire:**
- css
- systeme de chat dans Editor
- edition de document dans Editor
- Creation des tables documents & hasAccessTo (permettent de gérer les droits des users pour chaque document du site, trois niveaux de droits possibles: OWNER / EDITOR / VIEWER)
*Colonnes de hasAccessTo: user\_id / document\_id / role*
- Creation d'un fichier sur le serveur lors d'un envoie du formulaire DocumentChoice par "create", l'utilisateur est alors affecter en OWNER, ainsi que les admins (permissions = 2 dans la table users), le reste du site est affectee à VIEWER par defaut
- Permettre aux OWNER d'un document d'ajouter des EDITOR, de bannir des VIEWER, et de supprimer le document du serveur. Permettre aux EDITOR de modifier le document, les VIEWER peuvent seulement voir la page.
