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

