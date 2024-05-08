### Initial setups needed

**Docker setup:** 
- 1st start up --> `docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=links -d mysql`
- Afterwards --> `docker start mysqlcontainer`

VM argument needed: `-DMYSQL_password=password`

**User handling:**
- Manually register 1 user into `'users'` table -->` 1 (id), "user", "$2a$10$P9dUc03HhNXVFZSPwTXRpOTYdYOfgY57Gnbb9IhxZWlSRfGT/s7Km"`
    (this stands for "user" & "password" credentials for login)
- Set 1 role into `'roles'` table --> e.g.: `1 (id), "ROLE_USER"`
- Link these 2 in the mapping table (`'users_roles'`)
- Now you can use `'/secured'` endpoint, where first you are redirected automatically to `'/login'` and get authenticated with `user` and `password` credentials.