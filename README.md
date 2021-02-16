# VERSUS

## Team members

| Name | Mail | Github user|
|--------|--------|------------|
|Daniel Lahera Esteban| d.lahera@alumnos.urjc.es | DaniL97 |
|Enrique Garrido Gónzalez | e.garridogon@alumnos.urjc.es | kikegg |
|Daniel Alexander Juan Moreno | da.juan@alumnos.urjc.es | dajm92 |

## FASE 0

### Description

Versus is a web app about e-sports tournaments and leagues management. You will be able to create and manage tornaments with your friends, and also watch the oficial league and tournaments with their rounds and winners.
In addition to that, you can create your own team, apply for others or simply watch the pro existing teams.

### Entities

User, Team, Tournament, Game

* **User**: They can participate in tornaments and be part of different teams.
* **Team**: Composed by several users. Can have multiple games.
* **Tournaments**: Composed by several teams or users. Associated to one game at a time. There are official tounaments created by the admin and non-official created by users.

### Users and permissions

* **Anonymous User**: read and visualization permissions over public content. 
* **Normal User**: Same anonymous user permission plus write permissions over tournaments created, write permissions over its profile data, write permissions over created team, read permissions over a team.
* **Admin User**: Registered user permissions plus read, write and remove permissions over team, user, tournament and game. 

### Images

All users have associated one image to their account same as every team. This image is default, but every player and team can modify their image by uploading one.

### Graphics

Stats graphs related with games and rounds played (wins/losses) 

### Complementary Technology

Login with third parties IDP

### Algorithm or advanced query

- Every tournament have users or teams participating in them. When the admin or the creator of the tournament proceed to start the tournament. The web aplication will automatically generate the matchups.
The web aplication will also generate new rounds with their matchups every time the admin or the creator of the tournament validates the round to advance into the next, until the tournament have a winner.
- The web application has a classification that will be updated automatically based on the official tournaments and the points achieved by the teams that participate in them.  
