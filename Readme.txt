This is to keep track of updates.

1. Formatted provided code to match the skeleton we were provided during the exercises (graddle, main and test folders/modules separate) -Cosmin;

2. created lib dependencies; added JourneyEnd creation testing;

3. added JourneyStart creation testing;

4. readded library, as it was buggy( did not show the code in the library)-fixed

5. added partially completed class diagram of existing code

6. added fully completed diagram of existing code

7. Added basic time testing for Journey. This means creating journey start and end at the same time and checking if the duration in seconds is 0; slept program for 1 second for an additional test, and checked if the duration is 1 second.

8. created a test method which creates the a JourneyStart object(start), takes a random number of milliseconds and adds start.time() to it. then it mocks a clock object which returns this value upon calling clock.currentTimeMillis(), and creates a JourneyEnd object(end) by also passing the clock as parameter; after creating a Journey object using start and end, we assert whether the random number (in seconds) is equal to journey.getSeconds(); We won't test getMinutes(), as it has to behave correctly as long as getSeconds() does.

For this purpose, we created another class, RandomVals, which uses the ThreadLocalRandom.current.nextInt() method (available since java 1.8) to generate a random value of seconds between 1 and 6000 (we hard coded this as it shouldn't have much importance). randomNum(min, max) provides the random value in range [min,max] and getRandomMilis calls randomNum to provide the number of milliseconds. 

We decided not to change the legacy code type of durationSeconds() (int), and instead put (long) in front of the method call, e.g.: 
assertThat( (long) journey.durationSeconds(), is(dSeconds));
This was due to start.time() providing a long value, which could not be stored in an int.


9. refactored JourneyEvent by Overloading its constructor with another parameter, a ClockInterface object. So, when the parameter is passed, instead of assigning System.currentTimeMillis() to time, clock.currentTimeMillis() will be assigned instead. ClockInterface is an interface, so we can mock it, and we created a SystemClock class with a currentTimeMillis(): long method which calls System.currentTimeMillis(). So the functionality of the code is not changed in any way.

10. added two testing methods for the cardscanned method in TravelTracker, to check correct behaviour of the exception throw (exception type and message), by checking with an UUID which is not in the database.

11. added a normal constructor and a constructor which takes a list<journeyevent> and set<uuid> to initialise the traveltracker fields (dependency injection) to be able to test the cardScanned method; namely, in the test method we will create 2 such objects, which we will pass to the traveltracker constructor. If we modify the 2 objects inside traveltracker (using carscanned calls), the changes will reflect to the original objects, hence being able to test it.

12. Added another constructor to traveltracker, which takes a CustomerDatabase interface object; used adaptor pattern to be able to simulate behaviour. We created an adaptor class implementing the interface, which is a singleton just as CustomerDatabase, and added it to instance variables (dependency injection). Refactored cardscanned with the adaptor and created test for all behaviours (empty list of travellers, travellers already there when one card is scanned and results in one of their removals).

13. Did the same for the PaymentsSystem, created test that assesses peak and off peak charges are accurate, so we can ensure the code keeps its functionality when we refactor it. Custom clock returns "hardcoded" miliseconds, needs refactoring.

14. Solved the controllableClock issue, now gets time accurately and properly, using LocalDateTime.atZone().toInstant().toEpochMili();

15. Renamed Adaptor interfaces with better names; Moved adaptors and their interfaces to a separate folder(Adaptors) for better code structure; clock controller into Utils.

16. Created JourneyCostCalcultor class and moved rounding and peak checking together with the part of code that calculates the total sum for the journey a customer has completed.

17. Created unit tests for aforementioned class. Used set up methods for peak and off peak situations for reuse and maintainability of tests. Created 2 mathods in JourneyCostCalculator to provide the rounded peak and offpeak values. Need to refactor the tests from traveltracker, but will keep them around now to make sure we don't break anything.

18. added customer and journeys fields to controllable payment systems to be able to test the charge method works properly;

19. For consitency, each class should only do 1 thing, so we created another class that handles the charging. This also enables us to allow future development of the class, as you might have different methods of charging, add more functionality such as login, etc. 

20. Created charger class implementation and tests using TDD; added one more test to the test class

21. refactored TravelTracker to use the new class instead, thus increasing consistency consistency

22. Created failing test for journey cost calculator with short/long journeys

23. Created more tests, created alternative method for calculating costs for a customer

24. Changed tests with to the new functionality (long and short journeys) in traveltracker, journeyCost, chargerTest, added some more tests
