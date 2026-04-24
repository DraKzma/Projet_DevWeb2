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
- page DocumentChoice pour Creer/Trouver un document
- page Editor pour editer le document
- Creation des tables documents & hasAccessTo (permettent de gérer les droits des users pour chaque document du site, trois niveaux de droits possibles: OWNER / WRITER / VIEWER)
- Creation d'un fichier sur le serveur lors d'un envoie du formulaire DocumentChoice par "create", l'utilisateur est alors affecter en OWNER et le reste du site est affectee à VIEWER par defaut
- liste des membres ayant acces au document en cours ainsi que leur role dans Editor
- Permettre aux admins du site de pouvoir supprimer un document du site, et de pouvoir supprimer un user du site depuis la page DocumentChoice
- Permettre aux OWNER d'un document d'ajouter des WRITER, de bannir des VIEWER, et de supprimer le document du serveur

**A faire:**
- css
- systeme de chat dans Editor
- edition de document dans Editor
- Permettre aux WRITER d'un document de sauvegarder leurs modifications, et de télécharger le document sur leur machine. Les VIEWER peuvent seulement voir la page

