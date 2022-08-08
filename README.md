# recipes-service
The recipe-service is a java application that provides a CRUD for a recipe. 

## Objective
Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally, users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.

## Main tech used
* Java17
* Spring Boot
* MongoDB
* gradlew
* docker | docker compose
* Postman

## Documentation
The documentation can be accessible by this link: https://documenter.getpostman.com/view/2137666/VUjMoRLR
is a public postman document 

## How to Run
If you have docker and docker-compose in your computer, simple run  `docker-compose up -d` in you terminal on the project
root. This command will set up a mongodb database and build the application. After the build, the app can be accessible
by `localhost:8080` all endpoint are described in the documentation page

## How to test
If you have java 17 in your computer or in the ide, you can run `./gradlew test` to run the unitary and integration test.
Keep in mind that the integration tests need a database. You can run `docker-compose up -d` and change `spring.data.mongodb.host`
to localhost or set up other mongodb instance and change the `host` to match it.

## Decisions 
### Spring framework and your projects 
The spring framework is one of the most used frameworks for java development right now and is a solid choice for a service
like that, able not just to handle HTTP calls, but to make persistence operations. The abstractions provided by the framework
can make things easier but should be used with care because they can hide things and problems from the developer. But for
this project, this was not a problem. I also thought in use spark instead, but in spite of it being very light and doing its endpoint
in a more simple way, I couldn't find a very structured way to make a connection with the database, so I opted for the spring

### MongoDB
The main points I take into consideration to choose the database were: The structure of a recipe and the text search.
A recipe, at first glance, appears to be well-structured data, but it isn't. Recipes are instructions to do a thing, and can
be made in many ways, ingredients can be measured by many units, like pounds, unit, kg, liters, even 'a bunch' can be used.
You can have ingredients divided into different parts: a cake can have ingredients for itself and its coverage as 2 spared
list. Instructions can be text, a list, can have images, videos... Is just too many things that are either too difficult or
too abstract to be worthy represent in a relational database. For the sake of simplicity, only the different types of
ingredients are represented in this first version of the project. But think not just about having a project ready for production,
but ready to evolve makes me opt for MongoDB. The text search appears to be simpler in `mongodbTemplate`

### Postman
I always used `swagger` to document my APIs, but I decided to try something new. The `swagger` needs the application running
to be accessible, and can make a simple endpoint class a big mess of `annotations`. The Postman, on other hand, can be viewed
without the dependency of the application and lets the code clean. You can also set different types of examples and can show
how to make the exact call in many languages, with is quite handy. But this approach brings other problems though. You
need to go to another place to make a change in the doc, and this can be easily forgotten. It requires a very mature team
to not let that happen 



