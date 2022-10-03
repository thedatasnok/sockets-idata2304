# Socket programming assignent IDATA2304

This repository how simple clients and servers can be implemented using socket programming Java for both TCP and UDP.

The application code is independent of TCP and UDP - allowing us to share business logic between them. 


The project is split into one project per problem, with some shared logic:

- [Application code][app-impl] contains the application code, including a sentence processor and the task distributor
- [Shared library][shared-library] contains all shared interfaces and utilities
- [TCP Client][tcp-client] contains a simple implementation of a TCP client
- [TCP Server][tcp-server] contains a simple implementation of a TCP server including multithreading
- [UDP Client][udp-client] contains a simple implementation of a UDP client
- [UDP Server][udp-server] contains a simple implementation of a UDP server


[app-impl]: app/src/main/java/no/ntnu/iir/idata2304/app/
[shared-library]: shared/src/main/java/no/ntnu/iir/idata2304/shared/
[tcp-client]: tcp-client/src/main/java/no/ntnu/iir/idata2304/tcp/
[tcp-server]: tcp-server/src/main/java/no/ntnu/iir/idata2304/tcp/
[udp-client]: udp-client/src/main/java/no/ntnu/iir/idata2304/udp/
[udp-server]: udp-server/src/main/java/no/ntnu/iir/idata2304/udp/