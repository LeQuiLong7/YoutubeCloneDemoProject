## Simple Youtube Clone Website using ReactJS and Springboot microservices
- Too run app:
	- Front end:
  		```
 		 npm run dev
 		```
	- Back end: Go to each module DiscoveryServer, ApiGateWay, UserSevice, VideoService respectively and run 
  		```
 		 mvn spring-boot:run
 		```
- Front end side:
  - A very simple, beginer ReactJS Application with multiple components but ugly apperance
  - React Router to specify some routes for the application
  - Axios for calling to the back end
- Back end side: a microservices application with 2 services, 1 api-gateway and 1 discovery server
  - UserService:
    - A reactive Spring Webflux Application with MongoDB
    - Endpoints: Get videos by userId, like video by videoId,...
    - Kafka to interact with VideoService: listener on video-create-topic, producer on video-delete-topic
  - VideoService:
    - A reactive Spring Webflux Application with MongoDB, AmazonS3
    - Endpoints: upload videos, find video by videoId,...
    - Kafka to interact with UserService: listener on video-delete-topic, producer on video-create-topic
    - AmazonS3 Service to interact with AWS: upload video, get video from a bucket
  - ApiGateWay:
    - Route the request to UserService and VideoService
    - Spring Security Oauth2 Google to authenticate before routing
    - Redis for storing session information
  - DiscoveryServer:
    - An Eureka Discovery Server 
- Dependencies:
  - ReactJS
  - SpringBoot, SpringWebFlux, SpringSecurityOauth2
  - MongoDB
  - Redis
  - Kafka
  - AmazonS3

