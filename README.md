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

front-end (transakce, zobrazení vkladů, zobratit jméno uživatele)

možnost upravovat vklady (ukončit je předčasně, prodlužovat)

do transakcí přidat datum

integrovat sandbox paypal do transaction-service  a tím přidávat peníze na účet

rozdělení databází pro každou službu (udělat až konci aby při vývoji nemuseli běžet 3 databáze, všechno by mělo fungovat, jen je potřeba v applicaton.properties nakonfigurovat jednotlivé db)

----------------------

Lukášův projekt na PPRO: https://github.com/vanalu1/PPRO_InternetBanking

Eureka Dashboard: http://localhost:8080/eureka/web | http://localhost:8761/

Front-end: http://localhost:3000/

První prezentace: https://docs.google.com/presentation/d/1iMDNH_QD0DeWmXjKKjU5DXnwKESFE4K4JbfvWbJssZ8/edit?usp=sharing

Druhá prezentace: https://docs.google.com/presentation/d/1Gxyp5Mf9zhPDemknGq6QQtOhiFY06eZd0SSvuRwN_jw/edit?usp=sharing

Postman: https://www.postman.com/moisbank/workspace/mois/overview
