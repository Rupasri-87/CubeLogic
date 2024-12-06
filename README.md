**Suspicious Trades Detector**

**How It Works**

An order of the opposite side exists within the 30-minute window before the trade.
The order price is within 10% of the trade price:
For a BUY trade, the SELL orders must not exceed the trade price by more than 10%.
For a SELL trade, the BUY orders must not fall below the trade price by more than 10%.

===================================

**Requirements**

Prerequisites
Java 11 or higher
Maven or Gradle for dependency management
JUnit 5 for testing
Dependencies
JUnit 5: For unit testing.

Add the following to your Maven pom.xml:
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>.

=====================================

**Setup and Usage**

Clone the Repository
git clone https://github.com/your-repo/suspicious-trades-detector.git
cd suspicious-trades-detector

Build the Project
Use Maven to compile the code:
mvn clean install

Run the Tests
Execute the test suite:
mvn test
