This is the project to practise spring security with LDAP

https://spring.io/guides/gs/authenticating-ldap/

visit protected URL: http://localhost:8080/protected

login
username: ben
password: benspassword

Ben is in the LDAP group "developers" and "managers", so spring converts the group to roles.  
Ben has the role "ROLE_DEVELOPERS" and "ROLE_MANAGERS"

turn on the security log
```aidl
logging.level.root=WARN
logging.level.org.springframework.security=TRACE
```

log about retrieving authorities
```aidl
.s.s.l.u.DefaultLdapAuthoritiesPopulator : Searching for roles for user ben with DN uid=ben,ou=people,dc=springframework,dc=org and filter (uniqueMember={0}) in search base ou=groups
o.s.s.ldap.SpringSecurityLdapTemplate    : Using filter: (uniqueMember=uid=ben,ou=people,dc=springframework,dc=org)
.s.s.l.u.DefaultLdapAuthoritiesPopulator : Found roles from search [{spring.security.ldap.dn=[cn=developers,ou=groups,dc=springframework,dc=org], cn=[developers]}, {spring.security.ldap.dn=[cn=managers,ou=groups,dc=springframework,dc=org], cn=[managers]}]
.s.s.l.u.DefaultLdapAuthoritiesPopulator : Retrieved authorities for user uid=ben,ou=people,dc=springframework,dc=org
```

## spring security + facebook oAuth
https://www.youtube.com/watch?v=CWiwpvpCrro&list=PLqq-6Pq4lTTYTEooakHchTGglSvkZAjnE&index=15

