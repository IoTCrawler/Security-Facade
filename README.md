# License

Security Facade Project source code files are made avaialable under the Apache License, Version 2.0 (Apache-2.0), located into the LICENSE file.


# Introduction

This component has been designed as an endpoint for performing both authentication and authorisation operations in a transparent way for the requester.

# Configuration network_configuration.json file

Before launching the project, it's necessary to review the blockchain.conf file:

```sh
cd projectPath / configuration_files  / config
vi network_configuration.json
```

This file contains the endpoints that Security Facade requires PEP-Proxy, XACML-PDP, IdM-Keyrock and itself.

# Configuration docker-compose.yml file

There isn't any additional required configuration in this file.

# Prerequisites

To run this project is neccessary to install the docker-compose tool.

https://docs.docker.com/compose/install/

Launch then next components:

- XACML-PDP component running. 
- IdM-Keyrock component running. 

# Installation / Execution.

After the review of network_configuration.json file and docker-compose file, we are going to obtain then Docker image. To do this, you have to build a local one, thus:

```sh
cd projectPath / security-facade
./build.sh
```

The build.sh file contains docker build -t iotcrawler/securityfacade ./ command.

Finally, to launch the connector image, we use the next command:

```sh
cd projectPath / security-facade
docker-compose up -d
```

# Monitoring.

- To test if the container is running:

```sh
docker ps -as
```

The system must return that the status of the Security Facade container is up.

- To show container logs.

```sh
docker-compose logs securityfacade
```

# Security Facade functionality.

Security Facade is waiting a POST request with an specific format data body.

```sh
curl --location --request POST 'https://<Facade-IP>:<Facade-Port>/CapabilityManagerServlet/IdemixTokenIdentity' \
	 --header 'action: <action>' \
	 --header 'resource: <resource>' \
	 --header 'device: <device>' \
	 --header 'idemixIdentity: {"name": "<IdM-user>","password": "<IdM-password>"}'
```

- idemixIdentity: IdM user credentials (IdM-user and IdM-password).
- device: endpoint of the resource’s request (protocol+IP+PORT).
- action: method of the resource’s request ("POST", "GET", "PATCH"...).
- resource: path of the resource request.