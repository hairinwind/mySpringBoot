## this is the config-server with embedded configuration files
set to use minikube docker env
```eval $(minikube docker-env)```

run ```docker image ls -a``` and you can see a bunch of images starting with 'k8s.gcr.io', it is minikube docker env.

test before making image
```mvn spring-boot:run```  
Then visit http://localhost:8888/springkub/dev  
if you can see the correct response, it works properly.

build image 
```mvn spring-boot:build-image```

Once it is done, we can see "Successfully built image 'docker.io/library/config-server:0.0.1-SNAPSHOT'" from the console. Copy the last section

```docker image ls -a``` can find the newly built image.

create deployment file 
```
kubectl create deployment config-server --image=config-server:0.0.1-SNAPSHOT --dry-run=client -o=yaml > kube-deploy.yaml
echo --- >> kube-deploy.yaml
kubectl create service nodeport config-server --tcp=8888:8888 --node-port=30881 --dry-run=client -o=yaml >> kube-deploy.yaml
```

if it is already installed on kubernates, delete it first ```kubectl delete -f kube-deploy.yaml```

then apply it ```kubectl apply -f kube-deploy.yaml```

Once it is done

``` kubectl get pods``` to check it is running

```kubectl logs  <pod_name>``` to view the log 

```minikube service list``` to view the service list

visit http://192.168.49.2:30881/springkub/dev, you shall see the correct response.