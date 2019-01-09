# RSocket Example

This example consists of an echo server and a client which sends a stream of requests to the server, prints
the responses, and terminates.

## Pre-requisites

* Java 8 or later

## Building

Clone this repository, change directory into the clone, and issue the following command:

```terminal
./gradlew build
```

Then download the dependencies by issuing:
```terminal
./gradlew getDeps
```

## Running the Server

After building, issue the following command:
```terminal
java -cp "build/libs/rsocket-example.jar:dependencies/*" server.Server
```

### Running the Client

After building, issue the following command (typically in a separate terminal to the server):
```terminal
java -cp "build/libs/rsocket-example.jar:dependencies/*" client.Client
```

If the server is already running, the client should print output like this:
```terminal
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
Echo: Hello
```

### Docker

After building, copy the JAR files to the `docker/server` directory:
```terminal
docker/push.sh
```

Then build an image:
```terminal
pushd docker/server
docker build .
popd
```

Make a note of the image SHA that is printed out.

Now run the image:
```terminal
docker run -d -p 8080:8080 <image SHA>
```

If you run the client, it should print the same output as before.
