# project to test mutual ssl 

I followed this article https://medium.com/@salarai.de/how-to-enable-mutual-tls-in-a-sprint-boot-application-77144047940f

## Generating Root Certificates
```
openssl genrsa -des3 -out rootCA.key 2048
```
pass phrase is "password"

## Request a certificate from openssl using the key generated in the previous step
```
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 3650 -out rootCA.pem
```

## Signing Server Certificate
Let’s create a private key and then a CSR for our server certificate.

```openssl genrsa -des3 -out server.key 2048```
pass phrase is "password"

Now request a CSR with the key as input key:

```openssl req -new -sha256 -key server.key -out server.csr```

It will prompt for the same kind of information that was required for the root CA. CN field needs to be fully qualified hostname on which the server will be accessible, in this case localhost

A challenge password []:pass

Let’s sign the server certificate with the given CSR.  
```
openssl x509 -req -in server.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out server.pem -days 3650 -sha256
```

## Signing Client Certificate
```openssl genrsa -des3 -out client.key 2048```
pass phrase: password

create a CSR for the client in the same way
```aidl
openssl req -new -sha256 -key client.key -out client.csr
```
A challenge password []:pass

Then we sign the client certificate also in the same way  
```
openssl x509 -req -in client.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out client.pem -days 3650 -sha256
```

## generate server keystore file 
It expects the certificate and its corresponding private key in a bundled key store in either JKS or PKCS#12 format.

```aidl
openssl pkcs12 -export -in server.pem -out keystore.p12 -name server -nodes -inkey server.key
```
Enter Export Password: changeit  
this is the password of the keystore

We can see this PKCS#12 keystore contains both server.pem (public key) and server.key (private key)

## Spring config for keystore
put the generated keystore.p12 file into src/main/resources directory  
Put the following snippet in application.yaml file and restart the application.
```androiddatabinding
server:
  ssl:
    enabled: true
    key-store: "classpath:keystore.p12"
    key-store-password: changeit
    key-store-type: PKCS12
```

The first thing to notice after restarting the application is the following log:
```androiddatabinding
Tomcat started on port(s): 8080 (https) with context path ‘’
```

change the port to 443
```server.port: 443```

## Testing with curl or openssl 
Now restart and visit https://localhost/api/hello in chrome, we can see the correct response  

or use curl 
```
curl --cacert rootCA.pem https://localhost/api/hello
```
or use openssl 
```
openssl s_client -connect localhost:443 -CAfile rootCA.pem
```
the output contains 
```
SSL handshake has read 1377 bytes and written 373 bytes
Verification: OK
```
If the right CA pem is not provided, the output is like this
```androiddatabinding
SSL handshake has read 1377 bytes and written 373 bytes
Verification error: unable to verify the first certificate
```

## enable client auth
so far, it is only 1 way ssl (only on server side)
```androiddatabinding
# Enable client authentication
server.ssl.client-auth: need
```

restart the spring boot, then visit it in browser, you will get this error "ERR_BAD_SSL_CLIENT_AUTH_CERT"  
if we do curl again, 
```androiddatabinding
curl --cacert rootCA.pem https://localhost/api/hello
we will get this error 
curl: (56) OpenSSL SSL_read: error:14094412:SSL routines:ssl3_read_bytes:sslv3 alert bad certificate, errno 0
```
use openssl command would see this error 
```androiddatabinding
2648:error:14094412:SSL routines:ssl3_read_bytes:sslv3 alert bad certificate:../openssl-1.1.1i/ssl/record/rec_layer_s3.c:1543:SSL alert number 42
```

## turn on the log of handshake in spring boot
```androiddatabinding
logging.level:
  javax.net.ssl: DEBUG
  org.apache.tomcat.util.net: DEBUG
```
Now, we can see this excption in log
```androiddatabinding
javax.net.ssl.SSLHandshakeException: Empty client certificate chain
```
it means the client does not send the client certificate to the server

## generate the truststore
Let’s create a trust store holding the root CA that signed the client certificate.
```androiddatabinding
keytool -import -file rootCA.pem -alias rootCA -keystore truststore.p12
```
spring config 
```androiddatabinding
server:
    ssl:
        trust-store: "classpath:truststore.p12"
        trust-store-password: changeit
        trust-store-type: PKCS12
```

## test with client certificate
curl command
```androiddatabinding
curl --cert client.pem --key client.key --cacert rootCA.pem https://localhost/api/hello
```
openssl command
```androiddatabinding
openssl s_client -connect localhost:443 -CAfile rootCA.pem -cert client.pem -key client.key
```
the correct output is 
```androiddatabinding
SSL handshake has read 1571 bytes and written 2495 bytes
Verification: OK
```

## othere reference
https://smallstep.com/hello-mtls/doc/client/curl  




