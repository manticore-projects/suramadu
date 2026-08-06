###STEPS TO RUN TESTS:

1. Run maven install `mvn clean install` on this project.
2. In `suramadu-directdraw-javascript` run `npm start`
3. Open url `http://localhost:9000` in browser


###Notes:

Step 1 will generate test data to file located here:

```suramadu-directdraw-javascript/src/test/webapp/generated/tests.json```

this file contains encoded protocol buffer messages of PNG encoding and Directdraw encoding for each test defined in

```suramadu-directdraw-javascript/src/test/java/org/webswing/Tests.java```
