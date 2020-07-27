Appium Click Test
=================

Description:

This setup is created to show one cause of the "Appium/Selenium click does not work sometimes" issue.
The Android app is a simple one, with two screens, the MainActivity and SecondActivity, each having one button, click_me and close_me.
By clicking on the click_me button the SecondActivity opens, by clicking on the close_me button, the SecondActivity finishes and the MainActivity is shown again.
There are some delays built in to simulate Async API calls and screen rendering.
In the MainActivity, after the delay, the setContentView is invoked which causes the bug to appear.

The testng.xml file will run the TestClickCounter testng test.
Switch to appium-click-test directory and execute the following command:

	mvn install -Dsuite.Xml.File=testng.xml

This does the following:

1. Install the apk from src/test/resources/app-debug-appium-click-counter.apk (for reference, the source code is in the android directory)
2. Reset the App
3. Identify "click me" button by ID
4. Click the "click me" button
5. Identify "close me" button by ID
6. Click the "close me" button
7. Repeat steps 3 through 6 a thousand times


Setup: 

- OpenJDK 11 installed
- Maven installed
- Appium Servr v1.17.1 installed and running