# agones_spigot

A plugin that allows a Spigot server in an Agones Fleet to communicate with a sidecar (port is derived from the AGONES_SDK_HTTP_PORT environment variable) through the GRPC API. 
Supports the Ready, Health (5 second interval), and Shutdown methods

DoDisabling the plugin WILL cause the plugin to send a Shutdown event to the sidecar and WILL cause the controller to react accordingly.