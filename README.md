# Primitive HTTP proxy server

This is a computer lab done in course IK1203 as part of KTHs BSc programme in Computer Engineering.

It's a simple multithreaded server that takes a HTTP message with 3 parameters: hostname, port and an optional string. It creates a TCP-connection to the given hostname and port, and sends the string. It then returns whatever was recieved from the host to the client making the original HTTP request.

It's coded in Java. It handles data as bytes, and parses a HTTP request. Because it's functionality, it acts as both a web client and web server.