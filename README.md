# Kafka and Sttp Proof of Concept

## Introduction
This project demonstrates the fetching of a streaming talentrequest provider (a stub implementation)
via the Sttp library:
http://sttp.readthedocs.io/en/latest/

We use the Monix plugin to be able to examine the input stream as an observable:
https://monix.io/ 
This library has support for the Task concept (similar to a Scala Future).

Within this task we can examine each element in the stream,a ByteBuffer which is we can easily convert to a String,
which we again can easily convert to a case class of TalentRequest by using Circe: https://circe.github.io/circe/

When we have a TalentRequest we want to publish it to a Kafka bus, so this project also has a Kafka
Producer implementation.


//todo make a kafkaconsumer in Scala, preferably with a frontend.

## Validating flow
As we do not have a front-end which gets the results, we examine the results by using kafkacat:
https://github.com/edenhill/kafkacat

## Prerequisites

Install docker:

On Windows:
https://docs.docker.com/docker-for-windows/install/

On linux (Debian etc.):
```
sudo apt install docker
```
On Archlinux:
```
sudo pacman -Syu docker
```

##### please read the docker instructions how to setup docker users etc for your system

## Get the kafka docker image
```
 docker pull spotify/kafka
```

Now run the docker image::
```
docker run --net=host  spotify/kafka:latest
```
(on Linux mint I had to add the --net=host flag)

## run the applications
Now we have kafka running on a docker container we can run the application

First we start the fake talentrequest provider:
Navigate to the root of this project and do:
```
 sbt "project fakeprovider" "run"      
```

Now open another terminal and navigate to the root of the project again, and do:
```

 sbt "project producer" "run"      
```
And then choose number 2, TalentRequestClient.

Finally, listen as consumer on the kafka bus:
```
kafkacat -b localhost:9092 -t test
```
Or add an offset for example with -o [offsetnumber]


