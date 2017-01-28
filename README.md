# This is Cyber Security Base - Course Project I

The application is intentionally insecure and implements at least following five vulnerabilities from the OWASP top ten list. (https://www.owasp.org/index.php/Top_10_2013-Top_10)

## SQL Injection (OWASP A1)
1.) Start spring boot application server (this application follows the model previous projects on cyber security base course)

2.) Open your browser and go to http://localhost:8080

3.) Fill the form:
```
Nickname: anything
Note: test + "'); DELETE FROM Note; INSERT INTO Note (nickname, note) VALUES ('only', 'note
```

4.) Click share

5.) Click "Back to the list of notes"

-> This should delete all but the added note in the sql injection statement.

How to fix: Validate and sanitize all user input, for example, with PreparedStatement to separate the data from the query.

## Broken Authentication (OWASP A2)
This doesn't need steps to reproduce. The main thing that is intentionally wrong here is the file "CustomUserDetailsService.java".
It stores all passwords in plain text to the run time database. As OWASP in this section an example case on how to exploit this vulnerability:

Scenario #3: Insider or external attacker gains access to the system’s password database. User passwords are not properly hashed, 
exposing every users’ password to the attacker.

How to fix: A proper password encrypter must be always used and provided, for example:

```
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

## Cross-Site Scripting (XSS) (OWASP A3)
1.) Start spring boot application server (this application follows the model previous projects on cyber security base course)

2.) Open your browser and go to http://localhost:8080

3.) Fill the form:

```
Nickname: anything
Note: <script>alert("this is bad");</script>
```

4.) Click share

5.) Click "Back to the list of notes"

-> A javascript alert window should be displayed with the given text.

How to fix: Validate and sanitize all user input, for example, with PreparedStatement to separate the data from the query.


## Security Misconfiguration (OWASP A5)
1.) Start spring boot application server (this application follows the model previous projects on cyber security base course)

2.) Open your browser and go to http://localhost:8080/admin

-> Due to false security configuration user can access admin site without authentication. However, delete method still requires authentication.

How to fix: In file "SecurityConfiguration.java" don't set "/admin" path to permitAll() for http.authorizeRequests().

## Cross-Site Request Forgery (CSRF) (OWASP A8)
For easier testing, remove "/admin" from SecurityCoinfiguration.java to enable logging for admin page and receiving correct sessionid which is needed for delete.

1.) Start spring boot application server (this application follows the model previous projects on cyber security base course)

2.) Open your browser and go to http://localhost:8080

3.) Fill the form:

```
Nickname: anything
Note: <img src="http://localhost:8080/delete?nickname=Oscar+Wilde" width="0" height="0" border="0">
```

4.) Click share

5.) Click "Back to the list of notes"

6.) Login

7.) Input admin:admin for authentication

8.) Look at the requests, for example, from your browser's developer console

9.) Refresh admin page

-> Oscar Wilde's note gets deleted.

How to fix: Validate and sanitize all user input, for example, with PreparedStatement to separate the data from the query.
Also CSRF tokens for every form must be enabled from the SecurityConfiguration.java to prevent CSRF.

