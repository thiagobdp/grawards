#Worst Movie Golden Raspberry Awards.

## Getting Started

Clone this repository in your computer: https://github.com/thiagobdp/grawards

## 💻 Running the project

Start running GRAwardsApplication.java. The CSV file "movielist.csv" which is located in the path "src\main\resources" will be loaded.

To change the data, just replace this file.

When loading, data will be splitted in 3 entities: Indicated, Producer and Studio. 

Producer and Studio will be stored with non-duplicated names.

A rest service is available for consultation in "/indicated/fastestSlowestWinnerProducer" to get the producer with the longest gap between two consecutive awards, and the one with the fastest two awards.

Rest services for Producer, Studio and Indicated are also available to list all records.

Services Paths:

/indicated

/studio

/producer

## 🔬 Running Tests

Use Manven Test to run all tests together. In Eclipse IDE, righ click "pom.xml" -> "Run as" -> "Maven test"

Tests will use "test" profile and will load the CSV file "movielist-test.csv" which is located in the path "src\main\resources".

Data in this CSV file is exclusive for tests running, so don't change it.

Tests uses a specific DB but also in H2.