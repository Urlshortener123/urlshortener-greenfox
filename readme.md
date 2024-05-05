Initial set ups needed
Docker set up: 
1st start up --> docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=links -d mysql

Afterwards: docker start mysqlcontainer

VM argument needed : -DMYSQL_password=password