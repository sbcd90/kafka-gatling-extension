kafka-gatling-extension
=======================

[`Gatling`](http://gatling.io/#/) is an open-source load testing framework.
The `Kafka Gatling extension` can be used for stress testing an existing Apache Kafka installation using `Gatling`.


## Compatibility

The extension supports `Apache Kafka 0.10 protocol` & latest released version of Gatling `2.2`.

## Installation

### Installation from source

```
mvn clean install -Ppackage-only
```

## Getting started

- Look into the file [BasicSimulation.scala](src/test/scala/io/gatling/simulation/BasicSimulation.scala). Point it to the right `Kafka Broker coordinates` & provide the correct Kafka `topic` name.
- Start the simulation using the command

```
mvn gatling:execute -Dgatling.simulationClass=io.gatling.simulation.BasicSimulation
```

- Start the [Simple Kafka Consumer](src/test/scala/io/gatling/consumer/SimpleKafkaConsumer) after pointing it to the right Kafka coordinates.

## Features

- Custom avro schemas can be passed for generating records using them. Here is an [example](src/test/scala/io/gatling/simulation/SimulationWithAvroSchema.scala)

- An in-built Random Data Generator is provided for getting started with Load tests quickly.

- Custom data generators can be added if necessary. Here is an [example](src/test/scala/io/gatling/simulation/SimulationWithCustomData.scala)

- Gatling feeders are supported & a custom csv file can be passed for loading data. Here is an [example](src/test/scala/io/gatling/simulation/FeederByteArraySimulation.scala)

