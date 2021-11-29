
## this is the project mount configmap as external application properties

It does not depend on config-server and kubernetes API. The configmap is mounted as files on the pod. 
 
create configmap
```
kubectl create configmap springkub-configmap --from-file=configmap/springkubconfig --dry-run=client -o yaml | kubectl apply -f -
```

Once it is done, check it by web UI or 
```kubectl describe configmap springkub-configmap```

build docker image 
```mvn spring-boot:build-image```

create kubernetes deployment yml
```
kubectl create deployment spring-kube-mount-configmap --image=spring-kub-mount-configmap:0.0.1-SNAPSHOT --dry-run=client -o=yaml > kube-deploy.yaml
echo --- >> kube-deploy.yaml
kubectl create service nodeport spring-kube-mount-configmap --tcp=9091:9091 --node-port=30993 --dry-run=client -o=yaml >> kube-deploy.yaml
```

update the env profile and volume mount  
refer to the content in kube-deploy.yaml
```
        env:
          - name: SPRING_PROFILES_ACTIVE
            value: dev
        volumeMounts:
          - name: config-volume
            mountPath: /workspace/config
      volumes:
        - name: config-volume
          configMap:
            name: springkub-configmap
```

apply it to kubernetes
```kubectl apply -f kube-deploy.yaml```

login the shell
```
kubectl exec --stdin --tty config-server-mount-configmap-ccf475c55-d8ghk -- /bin/bash
ls /workspace/config
exit
```
You shall see the appliation files in /workspace/config. Spring boot would scan that folder and pick up the properties. 

update the configmap

if your kubernetes does not have the ability to restart pod when configmap is changed, you have to restart the service to let the spring boot get the new application properties.

## reload when configmap is changed
To make kubernetes reload the deployment when configmap is changed.   
https://github.com/stakater/Reloader
```
kubectl apply -f https://raw.githubusercontent.com/stakater/Reloader/master/deployments/kubernetes/reloader.yaml
```
Add this in deploy yaml
```
kind: Deployment
metadata:
  annotations:
    configmap.reloader.stakater.com/reload: "springkub-configmap"
  name: ...
```
deploy it. Now if the configmap is changed, the pod is restarted. 



