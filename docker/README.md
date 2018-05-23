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

Lancer la commande suivante permet de restaurer / créer la base `alterplanning` :
```
docker exec -it mssql_server /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P 'Alterplanning-2018' -Q 'RESTORE DATABASE alterplanning FROM DISK = "/var/opt/mssql/backup/alterplanning-2018-05-23.bak"'
```