
# Clavardage Entre Potes

Onnig BRULEZ | Tanguy DUCLOS-GENDREU, 4IR-SC

## Présentation du projet :
Ce projet est un application de chat en peer to peer utilisable sur un réseau local. Elle implémente les fonctionnalités suivantes : 
- Lors de la connexion, l'application voit les autres utilisateurs connectés et les ajoute.
- Lors de l'utilisation, si un utilisateur se connecte, il est ajouté à la liste de contacts.
- Lors de la deconnexion d'un contact, il est supprimé de la liste et si une conversation était ouverte avec lui, elle se ferme.
- Il est possible d'ouvrir une conversation en cliquant sur le contact associé.
- Il est possible de changer de pseudo
- Les messages sont conservés dans une BDD en local. Un poste = Un utilisateur.
- Il est possible de consulter l'historique des messages avec un utilisateur si ce dernier est connecté.

## Choix techniques :
 - Ce projet utilise maven pour permettre au projet d'être facilement importé sur différentes machine en automatisant l'importation des dépendances nécessaires. 
 - L'interface graphique a été réalisée avec JavaFX car il s'agit d'une technologie encore utilisée aujourd'hui et relativement facile à prendre en main en plus de permettre d'aboutir à un résultat visuellement satisfaisant.
- L'intégration continue est réalisée à l'aide de jenkins et automatise l'éxécution des tests unitaire lors de chaque commit. Il se trouve au lien suivant : http://jenkinspoo.obrulez.fr
## Utilisation et compilation du projet
Recupération du projet :

```git clone http://github.com/Solaumein/POO_2022```

 Le projet peut être compilé et exécuté à l'aide de la commande maven suivante (commande à éxécuter à la racine du projet, là où se trouve le pom.xml) :
 
 Commande Linux :

 ``` mvn clean && mvn compile && mvn exec:java -Dexec.mainclass="org.example.MainApp.java ```
 
 Commande cliquable depuis le readme sur intellij :
 
 ``` mvn clean compile exec:java -Dexec.mainclass="org.example.MainApp.java ```




## Utilisation de la GUI

![App Screenshot](https://cloud.obrulez.fr/index.php/apps/files_sharing/publicpreview/tC5bBkHsHiw79bZ?file=/&fileId=3384&x=1920&y=1080&a=true)

## Liens utiles
[Code](./src/main/java/org/example)

[Package Message](./src/main/java/org/example/Message)

[Rapport](./Rapport_UML_COO1.pdf) 

### UF "Conception et Programmation avancées" de 4ème année INSA 

  - [UML et patrons de conception](https://moodle.insa-toulouse.fr/course/view.php?id=1283)
  - [Programmation avancée en Java](https://moodle.insa-toulouse.fr/course/view.php?id=1228) 
  - [Conduite de projet](https://moodle.insa-toulouse.fr/course/view.php?id=1759) 
  - [PDLA](https://moodle.insa-toulouse.fr/course/view.php?id=1758)
