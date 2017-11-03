[![Build Status](https://travis-ci.org/priyaaank/Flippers.svg?branch=master)](https://travis-ci.org/priyaaank/Flippers) 
[![Maintainability](https://api.codeclimate.com/v1/badges/e7252b5733ef6c7bca58/maintainability)](https://codeclimate.com/github/priyaaank/Flippers/maintainability)

# Implementation on SWIM Protocol

Whitepaper can be found [here](http://www.cs.cornell.edu/~asdas/research/dsn02-SWIM.pdf)


### Notes

- To test sending of UDP packets locally use `netcat` utility. Connecting to server via `nc -c localhost 8343` allows you to send UDP packets with data.

- To regenerate protobuf message objects after message template has been modified
`protoc -I=src/main/resources/messageTemplates --java_out=src/main/java src/main/resources/messageTemplates/message.proto`
