
## this is the project using spring kubernetes API to get property values from configMap 
It does not use config server and it does not need mount the configmap.

## commands to run it 
create configmap on kubernetes
```
kubectl create configmap springkub-configmap --from-file=configmap --dry-run=client -o yaml | kubectl apply -f -
```
For now, all profiles shall be merged into one application.yml file. It is a known issue of spring API https://github.com/spring-cloud/spring-cloud-kubernetes/issues/640

Once it is done, check it by web UI or 
```kubectl describe configmap springkub-configmap```

build docker image 
```mvn spring-boot:build-image```

create kubernetes deployment yml
```
kubectl create deployment spring-kub-configmap-api --image=spring-kub-configmap-api:0.0.1-SNAPSHOT --dry-run=client -o=yaml > kube-deploy.yaml
echo --- >> kube-deploy.yaml
kubectl create service nodeport spring-kub-configmap-api --tcp=9092:9092 --node-port=30994 --dry-run=client -o=yaml >> kube-deploy.yaml
```
