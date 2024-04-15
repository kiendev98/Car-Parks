# Integration Tests

## Mock Server

For every integration test, there would be a mock server created that simulates the available car park
information.

You could check the [AvailableCarParMockServerConfig](./java/com/wego/interview/carpark/outbound/client/AvailableCarParkMockServerConfig.java)
to see the source code.

The mock server return a list of available car park as [json data](resources/data/mockClientResponse.json) with ids:

```text
A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12
```

>**IMPORTANT**: Any car park with id which is not in the list above would not be available.