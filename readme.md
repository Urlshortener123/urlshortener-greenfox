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

**Adding a count feature:**
- Added a `click_count` column to the `urls` table to store the number of times a URL is opened.
- Updated the `ShortenedUrl` model to include the `clickCount` field.
- Modified the `UrlController` to increment the `clickCount` each time a link is opened.
- Updated the `history.html` Thymeleaf template to display the `clickCount`.