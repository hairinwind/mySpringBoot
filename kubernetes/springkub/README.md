
## this is the config-server client project deployed on kubernetes
get the config-server url from ```minikube service list```

update the spring config import url in application.yml  
```spring.config.import: 'optional:configserver:http://192.168.49.2:30881/'```

```mvn spring-boot:run -Dspring-boot.run.profiles=dev``` to verify it is working

visit http://localhost:9090/message, it shall display ```from config server dev profile```

```docker image ls -a``` make sure you can see a bunch of "k8s.gcr.io/xyz" images. Otherwise, run ```eval $(minikube docker-env)```

build image ```mvn spring-boot:build-image```

Docker image springkub:0.0.1-SNAPSHOT is generated.

create deployment script
```
kubectl create deployment springkub --image=springkub:0.0.1-SNAPSHOT --dry-run=client -o=yaml > kube-deploy.yaml
echo --- >> kube-deploy.yaml
kubectl create service nodeport springkub --tcp=9090:9090 --node-port=30991 --dry-run=client -o=yaml >> kube-deploy.yaml
```

update the kube-deploy.yaml to put in the profile env
```
kind: Deployment
...
spec:
  ...
  template:
    ...
    spec:
      containers:
      - image: springkub:0.0.1-SNAPSHOT
        name: springkub
        resources: {}
        env:
          - name: SPRING_PROFILES_ACTIVE
            value: dev
```

apply the yaml, delete it first if already applied.
```kubectl apply -f kube-deploy.yaml```

visit http://192.168.49.2:30991/message, you shall see ```from config server dev profile```

Update something in the application-dev.yml on config-server, then redeploy it.

After config server is changed and redeployed, trigger refresh
```
curl -X POST 192.168.49.2:30991/actuator/refresh -d {} -H "Content-Type: application/json"
```

Then test again and you shall see the changes. 

## deploy prod profile to test config-server with mounted configmap
kube-configserver-mount-deploy.yaml is deploying the spring kub on prod profile. 
It connects to the config-server which is using the properties file mounted from configmap.  
In this test case, changing properties on config server no need to redeploy config-server.

- Change the properties or yaml in config-server-mount-configmap/configmap
- update the configmap from the file
- /actuator/refersh
- verify the message from the endpoint /message.
