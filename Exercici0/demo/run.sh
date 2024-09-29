#!/bin/bash
mvn clean package
java -cp target/demo-1.0-SNAPSHOT.jar com.example.Main