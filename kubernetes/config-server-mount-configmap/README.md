## this is the config-server using the configuration files mounted from kubernetes configmap

Use the similar method describe in config-server to create docker image  
https://github.com/hairinwind/mySpringBoot/tree/master/kubernetes/config-server

create kubernetes deployment yaml file 
```
kubectl create deployment config-server-mount-configmap --image=config-server-mount-configmap:0.0.1-SNAPSHOT --dry-run=client -o=yaml > kube-deploy.yaml
echo --- >> kube-deploy.yaml
kubectl create service nodeport config-server-mount-configmap --tcp=8889:8889 --node-port=30882 --dry-run=client -o=yaml >> kube-deploy.yaml
```

create the configmap from the directory (**note: it would not create files in sub-directories**)
```
kubectl create configmap configserver-configmap --from-file=configmap --dry-run=client -o yaml | kubectl apply -f -
```

update the kube-deploy.yaml to mount the configmap to /etc/config
```
kind: Deployment
...
spec:
  ...
  template:
    ...
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

apply it
```kubectl appy -f kube-deploy.yaml```

login the shell to view the file
```kubectl exec --stdin --tty config-server-mount-configmap-ccf475c55-d8ghk -- /bin/bash```

verify the deployed server is working
```curl -X GET http://192.168.49.2:30882/springkub/dev```

update the properties under configmap and then update the configmap
```
kubectl create configmap configserver-configmap --from-file=configmap --dry-run=client -o yaml | kubectl apply -f -
```

