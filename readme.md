### Initial setups needed

**Docker setup:** 
- 1st start up (MySQL) --> `docker run -p 3307:3306 --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=links -d mysql`
- Afterwards --> `docker start mysqlcontainer`
- MailHog (for e-mail testing) --> `docker run -d -p 1025:1025 -p 8025:8025 --name mymailhog mailhog/mailhog`

**VM arguments needed:** 
- For DB password --> `-DMYSQL_password=password`
- For 'testuser' creation --> `-Dspring.profiles.active=localdev`
- For API access --> `-DVTDOC_APIKEY=<YOUR_API_KEY>`

**User handling:**
- If VM arguments are correctly set, a new user (with "testuser" & "password" credentials, and "ROLE_USER" role ) will be automatically created.
- Now you can use `'/secured'` & `'/history` endpoints, where first you are redirected automatically to `'/login'` and get authenticated with `testuser` and `password` credentials.

### Application functions

**URL shortening:**
- Sanyi

**Database indexing:**
- The `urls` database is indexed on `uuid` column to make lookups  faster
- It means when a function is reaching out to `urls` table when returning the original url (at `localhost:8080/r/uuid`) from the shortened one, the action runs faster in case of having huge no. of records in the `urls` table
- This is managed by a one-line command (`CREATE INDEX <index_name> on table (column)`) in `V3__...` migration script.

**User registration:**
- Accessible via `/register`.
- Only accessible to non-logged-in users. Logged-in users are redirected to the main page.
- It also requires e-mail verification, so the user must provide a name, password and e-mail.
- If the username already exists, an error message is displayed.
- If the given e-mail is already used by a previous user, an error message is displayed.
- After registering credentials the app stores the user as a non-verified user, so logging in is not yet available. It also generates a hashKey(token) for each user registration.
- Then a verification e-mail is sent to the user (using MailHog) with the given hashKey(token).
- MailHog is available at `http://localhost:8025/`, where you can check the inbox for verification message. After opening the link in the e-mail, the user's verification will be completed.
- If the user verification is completed, the user is able to log in.
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
- Added a `click_count` column to the `urls` table to store the number of times a URL is opened.
- Updated the `ShortenedUrl` model to include the `clickCount` field.
- Modified the `UrlController` to increment the `clickCount` each time a link is opened.
- Updated the `history.html` Thymeleaf template to display the `clickCount`.

**CI/CD pipeline**
- Workflow is in .github/workflows/CI.yml
- Triggers on any push to any branch
- Sets up JDK 17 environment
- Checks gradle dependencies
- Checks the code and runs tests
- Branch is protected, all tests must be passed in order to merge
- If you are adding new dependencies to your build.gradle file, this will handle the dependency

If you want to run tests locally:
```sh
./gradlew test
