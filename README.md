# Cascade

What is Cascade?

Cascade is a black box testing framework.  The tester would describe state transitions and states 
in step files.  Cascade can then permute these steps in order to generate journey tests.  Effectively
this models the state machine that the application is changed into in order to support automated 
testing.  Cascade can then use this model in interesting ways, such as generating tests, filtering
tests, and generating reports.


Why Cascade?

Black box tests are slow and hard to maintain, because of their journey like nature.
Cascade intelligently models the state machine of the application allowing the optimised
generation of tests to cover the complete set of functionality.  Fewer tests means less
time to run.  Intelligent modeling guarantees better coverage of functionality.  Less
code means less development effort.  Modeling the state machine allows cascade to generate
powerful reports that track the data flows through each journey.  With modifications to
the subject application, cascade can decompose journey testing into transition testing,
resulting in testing times proportional to the number of transitions in the state machine.

## Author

My name is Robin de Villiers.  I am a software contractor.  My email address is robindevilliers@me.com.

## To build

This project uses gradle.

gradle clean build functionalTest.

## How to use

www.cascadetesting.tech


