### Initial setups needed

**Docker setup:** 
- 1st start up --> `docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=links -d mysql`
- Afterwards --> `docker start mysqlcontainer`

**VM arguments needed:** 
- For DB password --> `-DMYSQL_password=password`
- For 'testuser' creation --> `-Dspring.profiles.active=localdev`
- For API access --> `-DVTDOC_APIKEY=c42436737546829c8cd7d37f71685d88788bade697f392d9eb13b4df4d46d7be`

**User handling:**
- If VM arguments are correctly set, a new user (with "testuser" & "password" credentials, and "ROLE_USER" role ) will be automatically created.
- Now you can use `'/secured'` & `'/history` endpoints, where first you are redirected automatically to `'/login'` and get authenticated with `testuser` and `password` credentials.


### Application functions

**URL shortening:**
- Sanyi

**User registration:**
- Accessible via /register.
- Only accessible to non-logged-in users. Logged-in users are redirected to the main page.
- If the username already exists, an error message is displayed.
- Successful registration shows a confirmation message and redirects to the login page.

**URL history logging:**
- On `/history` endpoint a logged in User can access his/her previously shortened links with details (original url, shortened url, creation date).
- If the User is not authenticated (not logged in), this page is unavailable.

**Checking malicious URLs:**
- Using virustotal.com's API the URL sent for shortening is checked
- If the url is considered malicious (maliciousScore > 3) based on the API database, system throws an error to the User and do not proceed.
- If the url is not malicious (maliciousScore <= 3) the shortening is performed right away.
- In case of any errors (no records of the given url, API service is not available) the system logs an error and skips the malicious check and performs the shortening anyways.
- You can enter the URL in any form, so with or without`https://` or `www.` prefix, but the application handles only the `domain.com` part to communicate with the VirusTotal API.

**Counting URL opening**
- Betti