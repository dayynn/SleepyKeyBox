# SleepyKeyBox
A REST API for KeyBox.

## Setup
You will need to put your keybox database password that was set during keybox setup in the
file `src/main/java/DBUtil.java`. That's it!!

## Running
Just use `mvn package` to create a jar file that can be run from java: `java -jar target/keyrestbox-1.0-SNAPSHOT.jar`!

## Structure
* `api/users` -> get JSON data back from the database by passing in parameters to a GET request
  * `email`
  * `username`
  * `first_name`
  * `last_name`
* `api/newuser` -> add a new user using a POST request
  * `email`
  * `username`
  * `first_name`
  * `last_name`
  * `password`
  * `user_type`
* `api/deleteuser` -> delete a specific user (must be by unique ID)
  * `user_id`
