# the project for custom parameterResolver in spring boot

I want some annotation like @PathVariable, but I need change the function a bit. 
- check if the http head has the attribute "userId", if yes, use that as userId
- if no, use the userId from path 

## test with head
```
curl -H "userId: headUserId999" -X GET http://localhost:8080/hello/pathUserId123
```
the response shall be 
```
hello headUserId999
```

## test without head
```
curl -X GET http://localhost:8080/hello/pathUserId123
```
the response shall be
```
hello pathUserId123
```