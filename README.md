Spuštení databáze:

Spustit Docker Desktop -> Spustit CMD jako správce ->

docker run -p 3306:3306 --name mariadb1 -e MARIADB_ROOT_PASSWORD=pass -d mariadb:latest

-> v InteliJ přidat MariaDB, username root, password pass, port 3306 -> vytvořit potřebné schéma v databázi

prozatím 3 různé schéma ve stejné databázi | schema name = "transaction-service", "deposit-service", "authentication-service"

TODO:

dokončit základní funkcionality služeb

propojit služby mezi sebou

vytvořit API gateway

vytvořit front-end

implementovat pokročilé funkcionality

propojit transaction-service s paypalem

propojit authentization-service s facebookem

zabezpečení

rozdělení databází pro každou službu

dockerizace
