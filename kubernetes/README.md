
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
TODO: error: no node port

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
kubectl create deployment config-server --image=hairinwind/springkub-config-server --dry-run -o=yaml > deployment-config.yaml
echo --- >> deployment-config.yaml
kubectl create service nodeport config-server --tcp=8888:8888 --dry-run -o=yaml >> deployment-config.yaml
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