# License

Security Fachade Project source code files are made avaialable under the Apache License, Version 2.0 (Apache-2.0), located into the LICENSE file.


### Prerequisites
*It is assumed according to the Dockerfile that we have in a folder named ssh with the keys to access the odinslab repository* --> To be updated with ENV variables or other more clean methods.

# To run the Capability Managed components

#### Copy in the ssh (create if not exists) folder the keys to automate the interaction with the git repositories

```
docker build --tag capmanager .
docker run --privileged -p 8080:8080 capmanager
```

# To access the service to verify that is running

#### Capability manager service
```
http://localhost:8080/CapabilityManagerServlet/generate
```
```
POST /CapabilityManagerServlet/generate HTTP/1.1
Host: localhost:8081
action: action
resource: resource
device: device
idemix-token: acdf146a-e02c-474a-93b6-b72e63a7b8e3
```


```
http://localhost:8080/CapabilityManagerServlet/verify
```
```
POST /CapabilityManagerServlet/verify? HTTP/1.1
Host: localhost:8081
action: action
resource: resource
device: device
captoken: {"id":"7c9k9pngrtipnr3v8d3brdnkdr","ii":1585908344,"is":"capabilitymanager@odins.es","su":"testuser","de":"device","si":"MEUCIB7ku5/1yGG8gJ1HDzMlooWQP3p7FFJ2syC6Bc2GqCcwAiEAkootOpfp7P7E4HhCdkoT/NSpCAeQjJxOyBvhC4tWilA=","ar":[{"ac":"action","re":"resource"}],"nb":1585909344,"na":1585919344}
```
