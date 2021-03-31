
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

apply it to kubernetes  
update config map  
no need to trigger /actuator/refresh, it updates automatically  
refresh the web browser, you shall see the new values   

## security role
If you get error "Cannot read configmap with name: [xx] in namespace ['default']", it means the spring kubernetes API need access to read the configmap. You need run 
```kubectl apply -f roleConfig.yml```


## reference
https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/index.html  
https://github.com/spring-cloud/spring-cloud-kubernetes#starters  
https://blog.csdn.net/boling_cavalry/article/details/97529652  
https://www.cnblogs.com/larrydpk/p/13611431.html  
https://hackmd.io/@ryanjbaxter/spring-on-k8s-workshop#Create-a-Spring-Boot-App  
https://kubernetes.io/zh/docs/tasks/configure-pod-container/configure-pod-configmap/  
https://cloud.spring.io/spring-cloud-static/spring-cloud-kubernetes/1.0.0.M2/multi/multi__propertysource_reload.html  
https://github.com/spring-cloud/spring-cloud-kubernetes/blob/master/docs/src/main/asciidoc/security-service-accounts.adoc

