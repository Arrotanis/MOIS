Autoři: František Bílek, Petr Pokorný, Jindřich Svoboda, Lukáš Váňa

----------------------
Spuštení databáze:

Spustit Docker Desktop -> Spustit CMD jako správce ->

docker run -p 3306:3306 --name mariadb1 -e MARIADB_ROOT_PASSWORD=pass -d mariadb:latest

-> v InteliJ přidat MariaDB, username root, password pass, port 3306 -> vytvořit potřebné schéma v databázi

-> prozatím 3 různé schéma ve stejné databázi | schema name = "transaction-service", "deposit-service", "authentication-service"

----------------------
Instalace frontendu:

Nainstalovat Node.js https://nodejs.org/en -> 

v CMD přejít do složky ...\MOIS\bank-spa -> npm install

----------------------
Spuštění frontendu:

v CMD přejít do složky ...\MOIS\bank-spa -> npm start

----------------------

TODO:

přepsat request param metody na request body (potřeba upravit controller, service a vytvořit dto)

dokončit základní funkcionality služeb (metody v controllerech atd.)

propojit služby mezi sebou (pomocí webClientBuilder, příklad v DepositService)

vytvořit basic front-end

implementovat pokročilé funkcionality

propojit transaction-service s paypalem (add balance)

propojit authentication-service s facebookem

rozdělení databází pro každou službu (možná ani není potřeba)

dockerizace služeb (mělo by být easy)

----------------------

Lukášův projekt na PPRO: https://github.com/vanalu1/PPRO_InternetBanking

Eureka Dashboard: http://localhost:8080/eureka/web | http://localhost:8761/

Random YT guide: https://www.youtube.com/playlist?list=PLSVW22jAG8pBnhAdq9S8BpLnZ0_jVBj0c

Front-end: http://localhost:3000/
