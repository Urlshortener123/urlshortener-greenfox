### Initial setups needed

**Docker setup:** 
- 1st start up --> `docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=links -d mysql`
- Afterwards --> `docker start mysqlcontainer`

VM arguments needed: 
- For DB password --> `-DMYSQL_password=password`
- For 'testuser' creation --> `-Dspring.profiles.active=localdev`

**User handling:**
- If VM arguments are correctly set, a new user (with "testuser" & "password" credentials, and "ROLE_USER" role ) will be automatically created.
- Now you can use `'/secured'` & `'/history` endpoints, where first you are redirected automatically to `'/login'` and get authenticated with `testuser` and `password` credentials.

**CI/CD pipeline**
- Workflow is in .github/workflows/CI.yml
- Triggers on any commit to any branch
- Sets up JDK 11 environment
- Checks gradle dependencies
- Checks the code and runs tests
- Branch is protected, all tests must be passed in order to merge
- If you are adding new dependencies to your build.gradle file, this will handle the dependency

If you want to run tests locally:
```sh
./gradlew test