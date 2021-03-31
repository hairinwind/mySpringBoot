## the sub modules
- config-server is the spring cloud config server with application property files embedded in resources/config
- config-server-mount-configmap is the spring cloud config server using the mounted application property files from configmap.
- springkub is the spring cloud config server client
    - kube-deploy.yaml is connecting the springkub to config-server
    - kube-configserver-mount-deploy.yaml is connectiong the springkub to config-server-mount-configmap
- spring-kub-mount-config is not using cloud config server. The application files are mounted to /workspace/config from configmap
- spring-kub-configmap-api is using spring-cloud-starter-kubernetes-config to access kubernetes API and monitor the configmap changes.

## reference docs
https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/index.html  
https://github.com/spring-cloud/spring-cloud-kubernetes#starters  
https://blog.csdn.net/boling_cavalry/article/details/97529652  
https://www.cnblogs.com/larrydpk/p/13611431.html  
https://hackmd.io/@ryanjbaxter/spring-on-k8s-workshop#Create-a-Spring-Boot-App  
https://kubernetes.io/zh/docs/tasks/configure-pod-container/configure-pod-configmap/  

## commands

create configmap
```
kubectl apply -f config.yml
```

view configmap
```
kubectl describe configmap springkub-config
```

get pod
```
kubectl get pod
```

view log of the pod
```
kubectl logs springkub-6bcff4d6cf-xbb4w -f
```

minikube service
```
minikube service springkub
```

Get a shell of running pod(container)
```
kubectl exec --stdin --tty <pod_name> -- /bin/bash
```
type exit to quit

restart one deployment 
```
kubectl rollout restart deployment/springkub
```

## cheatsheet
https://kubernetes.io/docs/reference/kubectl/cheatsheet/

## config-server build docker image
```
mvn spring-boot:build-image
```
see this in log  
[INFO] Successfully built image 'docker.io/library/config-server:0.0.1-SNAPSHOT'

run the docker
```
docker run -p 8888:8888 config-server:0.0.1-SNAPSHOT
```
tag and push to dockerhub
```
docker tag config-server:0.0.1-SNAPSHOT hairinwind/springkub-config-server
docker push hairinwind/springkub-config-server
```
create kubernetes deployment yaml file
```
kubectl create deployment config-server --image=hairinwind/springkub-config-server --dry-run=client -o=yaml > deployment-config.yaml
echo --- >> deployment-config.yaml
kubectl create service nodeport config-server --tcp=8888:8888 --dry-run=client -o=yaml >> deployment-config.yaml
```
deploy to kubernetes
```
kubectl apply -f deployment-config.yaml
```
check if the service is up
```
kubectl get all
```
view the service name
```
kubectl get services
```
expose the service
```
minikube service config-server
```
It would open the browswer with the URL, append the URL with /springkub/prod
```
http://192.168.49.2:31768/springkub/prod
```
update springkub config server URL, hardcode the config server URL here. Ideally it shall be set in kubernetes configmap  
**springkub/src/main/resources/application.yml**  
```
spring.config.import: 'optional:configserver:http://192.168.49.2:31768/'
```
Run the spring boot project, it shall still be able to start and visit http://localhost:9090/message shall return the message related to the profile.

build image and run the docker
```
mvn spring-boot:build-image
docker run -p 9090:9090 -e "SPRING_PROFILES_ACTIVE=dev" --network="host" springkub:0.0.1-SNAPSHOT
```
- -e is setting the env variable of spring profile, then the service in docker would run on profile dev
- --network="host" is share the localhost network to docker, so that the service in docker can visit the config server http://192.168.49.2:31768/
  
docker push
```
docker tag springkub:0.0.1-SNAPSHOT hairinwind/springkub
docker push hairinwind/springkub
```
create kubernates deployment yaml file
```
kubectl create deployment springkub --image=hairinwind/springkub --dry-run -o=yaml > deployment-springkub.yaml
echo --- >> deployment-springkub.yaml
kubectl create service nodeport springkub --tcp=9090:9090 --dry-run -o=yaml >> deployment-springkub.yaml
```
update the yaml to put in the profile env
```
env:
          - name: SPRING_PROFILES_ACTIVE
            value: dev
```

remove and redeploy the springkub
```
kubectl delete -f deployment.yaml
kubectl apply -f deployment-springkub.yaml
minikube service springkub
```

## mount configMap as volume and let config server use the files from the mounted volume
This practice is on branch configserver-file-backend.

create configmap from directory  
It will import all the files in the directory into configmap, but it won't include the files in subdirectories.kube
```
kubectl create configmap configserver-configmap --from-file=config/springkub
```
if you need update existing configmap
```
kubectl create configmap configserver-configmap --from-file=springkub --dry-run=client -o yaml | kubectl apply -f -
```
Or, create the yaml file and then apply it
```
kubectl create configmap configserver-configmap --from-file=springkub --dry-run=client -o yaml > springkub-configmap.yml
kubectl apply -f springkub-configmap.yml
```
Once the configmap is created, add that in deployment script
```
apiVersion: apps/v1
kind: Deployment
...
spec:
  ...
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: config-server
    spec:
      containers:
      - image: hairinwind/springkub-config-server
        name: springkub-config-server
        resources: {}
        volumeMounts:
          - name: config-volume
            mountPath: /etc/config/springkub
      volumes:
        - name: config-volume
          configMap:
            name: configserver-configmap
```
The config files are mounted to /etc/config/springkub  
Update the config-server to file based backend. 
**application.properties**
```
spring.cloud.config.server.native.searchLocations=/etc/config,/etc/config/{application}
```
build docker image and push  
kubernetes apply deployment-config-with-configmap-volume.yaml  
Once it is done, you can login the shell or on web UI, click "..." of that pod and select "execute"
```
kubectl exec --stdin --tty <pod_name> -- /bin/bash
ls /etc/config/springkub
exit
```
The springkub configuration files shall be in that folder.  
You can also check the configmap by web UI or the command
```
kubectl describe configmap configserver-configmap
```
Visit the config-server kubernetes service URL, it shall display the content from the configmap. e.g. http://192.168.49.2:30257/springkub/dev

deploy the springkub service, it shall get the properties from the config server. 

I tried to update values in configMap. The config server can get the updates. But the springkub (cloud config client) can not get the updates. I did post to the /actuator/refresh and I saw the response. But the value in springkub is not updated.  
To get the new value, I have to restart the deployment. 
```
kubectl rollout restart deployment/springkub
```

## get image from local docker image
set ```imagePullPolicy: Never``` in deployment.yml  
run ```eval $(minikube docker-env)``` to use the minikube docker env.  
build docker image again  
run kubectl apply

