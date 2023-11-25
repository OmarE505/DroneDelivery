# Drone-Delivery

## Introduction:
A service via REST API that allows clients to communicate with drones (i.e. **dispatch controller**).

## Build Commands:
  * Build: `.\mvnw package`
  * Run: `.\mvnw spring-boot:run`
  * Test: `.\mvnw test`
## Documentation:
Content-type: application.json

### Drone Controller: `localhost:8080/api/drones`
   * **`GET` Get all drones: `localhost:8080/api/drones`**

   * **`GET` Get a specific drone: `localhost:8080/api/drones/{id}`**

   * **`POST` Register a drone: `localhost:8080/api/drones`**
       Request Body Example:
       ```JSON
      {
        "model" : {
          "name" : "LIGHT_WEIGHT"
          }
      }
       ```

   * **`PUT` Update a drone: `localhost:8080/api/drones/{id}`**
       Request Body Example:
       ```JSON
       
       {
         "serialNumber": {
           "id": 5,
           "value": "PEGEN-BEU3S-OSO0A-2W1ZX-YRGYK-ULCLG"
           },
         "batteryCapacity": 100,
         "model": {
           "id": 5,
           "value": 200,
           "name": "MIDDLE_WEIGHT"
           },
         "state": "IDLE",
         "medications": []
        }
      ```

   * **`DEL` Delete a drone: `localhost:8080/api/drones/{id}`**

   * **`GET` Load a drone: `localhost:8080/api/drones/load?droneId=3&medicationIds=1,2,3` accepts a specific drone id to be loaded and a list of medication ids to be loaded on that drone.**

   * **`GET` Get medications from drone: `localhost:8080/api/drones/medications/{droneId}` Checks medications that are currently loaded on a specific drone.**

   * **`GET` Get available drones: `localhost:8080/api/drones/available/{totalMedicationWeight}` Checks available drones that can load the specified medication weight.**

   * **`GET` Get drone battery level: `localhost:8080/api/drones/batterLevel/{droneId}` Checks current battery capacity of a specified drone.**

### Medication Controller: `localhost:8080/api/medications`

  * **`GET` Get all medications: `localhost:8080/api/medications`**

  * **`GET` Get a specific medication: `localhost:8080/api/medications/{id}`**

  * **`POST` Add a new medication: `localhost:8080/api/medications`**
    Request Body Example:
    ```JSON
    {
      "name" : "Med70",
      "weight" : 70
    }
    ```

  * **`PUT` Upload medication image: `localhost:8080/api/medications/{id}` Content-type: multipart/form-data. It accepts a request param of type file.**

  * **`PUT` Update medication: `localhost:8080/api/medications/{id}`**
    Request Body Example:
    ```JSON
    {
      "name" : "Med80",
      "weight" : 90
    }
    ```

  * **`DEL` Delete a medication: `localhost:8080/api/medications/{id}`**
