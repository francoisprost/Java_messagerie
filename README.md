# Système de messagerie instantanée

Ce projet est réalisé par PROST François dans le cadre des projets de Java 3A IT ESIREM

## Objectifs du projet
Le but de ce projet est mettre en œuvre un système de chat. Les communications devront être de deux
types : publiques (visibles par tous les utilisateurs utilisant le système de messagerie) ou privées (visibles
uniquement par deux utilisateurs concernés par les échanges).
Les fonctionnalités à implémenter sont :

1. mettre en œuvre une application côté client et côté serveur ;
2. établir la connexion entre les deux applications ;
3. tenir une liste d’utilisateurs connectés au serveur. Cette liste peut être consultée par tous les utilisateurs ;
4. garantir l’unicité du pseudo de chaque utilisateur ;
5. permettre aux utilisateurs de choisir les personnes avec qui ils veulent discuter en privé (par défaut
les messages seront visibles par tous les utilisateurs connectés) ;
6. proposer aux utilisateurs une option permettant de bannir un pseudo (temporairement ou définitivement) ;
7. proposer la sauvegarde (dans un fichier) des messages pour chaque utilisateur ;
8. permettre aux utilisateurs de se déconnecter (une notification sera envoyée aux autres utilisateurs).

## Fonctionnalités implémentées

1. mettre en œuvre une application côté client et côté serveur ;
2. établir la connexion entre les deux applications ;
6. proposer aux utilisateurs une option permettant de bannir un pseudo (définitivement seulement) ;
8. permettre aux utilisateurs de se déconnecter.

## Notice d'utilisation et remarques

Pour faire fonctionner le projet, il faut lancer le TestServeur puis le TestClient. La conversation se fait entre l'application client et serveur. Les fonctionnalités sont implémentées seulement du côté client car normalement, c'est les clients qui discutent entre eux et non le serveur avec le client.
Lors de l'execution du code, on se connecte (automatiquement) à l'utilisateur local 127.0.0.1 pour la mise à bien du test.
Pour réaliser ce projet, je me suis aidé d'Internet mais j'ai réalisé le code moi-même
