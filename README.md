# Drone-Delivery

## Introduction:
A service via REST API that allows clients to communicate with drones (i.e. **dispatch controller**).

## Task requirements:
We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case**the load is medications**.

**A **Drone** has:**

* [x] serial number (100 characters max)
* [x] model (Lightweight, Middleweight, Cruiserweight, Heavyweight)
* [x] weight limit (500gr max)
* [x] battery capacity (percentage)
* [x] state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has: 

* [x] name (allowed only letters, numbers, ‘-‘, ‘_’)
* [x] weight
* [x] code (allowed only upper case letters, underscore and numbers)
* [x] image (picture of the medication case).

**The service should allow:**

* [x] registering a drone
* [x] loading a drone with medication items
* [x] checking loaded medication items for a given drone;
* [x] checking available drones for loading
* [x] check drone battery level for a given drone
## Build Commands:
  * Build: `.\mvnw package`
  * Run: `.\mvnw spring-boot:run`
  * Test: `.\mvnw test`

## Testing:
**you can find the JUnit testing classes under `/src/test/java/com/omarE505/DroneDelivery/`**

**to run the testing command as perviously mentioned `.\mvnw test`**

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
          "id" : 1
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
           "id": 1
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

   * **Note that when a drone gets loaded with medicine using the load method , the drone become in "LOADED" state, while in loaded state the drone loses 5% of battery every (specified schedule) time till battery reaches 25% , then state of drone changes to IDLE, while in IDLE state if the drone has less battery than 100% it will recharge 5% every (specified schedule) time till it reaches 100% back.**

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
