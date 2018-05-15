# Docker

Documentation pour récupérer un SQL Server avec docker.

## Prérequis

- Docker Engine 1.8+ 
- Minimum of 2 GB of RAM
- Docker Compose installed

## Installation

Lancer la commande suivante :

```
docker-compose up -d
```

Docker ce lance alors et télécharge (seulement au premier lancement) ses dépendances.

Pour vérifier le status du container :

```
docker ps -a
```

Si le container est est status 'Exited' vérifier ses logs avec la commande suivante :

```
docker logs [CONTAINER_ID]
```

## How to Use

La base est alors accesible sur localhost avec les identifiants suivants :

- user: `sa`
- password : `Alterplanning-2018`

## Restore From Backup

Dans le dossier `mssql_backup` ce trouve le ficher de backup de la base.