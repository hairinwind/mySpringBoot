# mutual ssl java client

## add the truststore as jvm arguments
```androiddatabinding
-Djavax.net.ssl.trustStore=/path/to/truststore.p12 -Djavax.net.ssl.trustStorePassword=changeit
```
in this example, the server certificate and client certificate are both signed by RootCA.key  
and in the server project, we already generate the truststore.p12, which import the RootCA.pem. 
so, this trustore.p12 to validate the server certificate. 

## create PKCS#12 keystore for client 
go to the server folder
```androiddatabinding
openssl pkcs12 -export -in client.pem -out client_keystore.p12 -name client -nodes -inkey client.key
```
add jvm arguments 
```androiddatabinding
-Djavax.net.ssl.trustStore=web-mutual-ssl-client/src/main/resources/truststore.p12
-Djavax.net.ssl.trustStorePassword=changeit
-Djavax.net.ssl.trustStoreType=PKCS12
-Djavax.net.debug=ssl
-Djavax.net.ssl.keyStoreType=PKCS12
-Djavax.net.ssl.keyStore=web-mutual-ssl-client/src/main/resources/client_keystore.p12
-Djavax.net.ssl.keyStorePassword=changeit
```
Note the trustStore and keyStore path is related to the working folder.

# related errors 
## the error message when missing CA certificate in trustStore
the error below happens when the trustStore does not have the CA certificate which signs the server certificate  
Or the provided certificate file () cannot be found so java falls back to cacert
```
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```
server side error is 
```
javax.net.ssl.SSLHandshakeException: Received fatal alert: certificate_unknown
```

## client does not provide certificate 
```
Caused by: javax.net.ssl.SSLHandshakeException: Received fatal alert: bad_certificate
```
server side error is 
```
javax.net.ssl.SSLHandshakeException: Empty client certificate chain
```

# consclusion
To validate the certificate from the other side, we only need a jks which contains the certificate(public key) from the remote side.  
To provide the certificate to the other side, we need a PKCS#12 keystore, which contains both public key and private key.