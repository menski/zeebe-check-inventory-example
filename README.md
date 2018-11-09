# Zeebe Check Inventory Example

This project contains a small Spring Boot Application which demonstrates
message correlation in Zeebe 0.13.0

![Check Inventory Workflow](src/main/resources/static/check-inventory.png?raw=true "Check Inventory Workflow")

## Start Zeebe

- Download Zeebe 0.13.0 from GitHub https://github.com/zeebe-io/zeebe/releases/tag/0.13.0
- Unpack and run the Zeebe broker, i.e. `./bin/broker`

## Deploy process

Use `zbctl` from the Zeebe distribution and deploy the process from the
resource folder.

```
zbctl deploy src/main/resources/static/check-inventory.bpmn
```


## Start Application

Use maven to start the application and go to http://localhost:8080

```
mvn spring-boot:run
```

## Play around

Click on the add item button to add more items to the list. On every change
a new workflow instance is started in Zeebe, which checks the availability
of each item. At the moment the availability is just random. The app uses
websockets to request a new check and waits for the results be pushed back by
the server.

You can see how a workflow instance is created for every request to the webapp
in the class [CheckItemsController][].

The first task of the process _Check Inventory_ is handled by the
[CheckInventoryService][], it submits for every item in the list an
asynchronous request to external service, this is simulated by submitting a
delayed runnable on an executor service.

The response from the external service is then published as message to Zeebe
and picked up by the workflow instance.

The second task _Process Messages_ is implemented in the
[ProcessMessagesService][] and handles every message of this instance in
sequence. It collects the status responses in the workflow instance payload
until all items where checked. It then sends the response back to the webapp
frontend and the instance is finished.

[CheckItemsController]:src/main/java/io/zeebe/example/inventory/app/CheckItemsController.java
[CheckInventoryService]: src/main/java/io/zeebe/example/inventory/workflow/CheckInventoryService.java
[ProcessMessagesService]: src/main/java/io/zeebe/example/inventory/workflow/ProcessMessagesService.java
