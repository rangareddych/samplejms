# samplejms
This project is about the technical assignment
1. After cloning this project to your local, go to project root directory (samplejms).
2. Open command terminal or Git bash to the above location.
3. Type gradlew.bat and Enter
4. After this import this project to IDE as gradle project.
5. Right click on the project, select Gradle -> Refresh Gradle Project
6. Run com.swamy.samplejms.SamplejmsApplication as java application

Testing
--------
1. Use any REST client (I used POSTMAN) to place a request.
2. Request POST - http://localhost:8080/postsales
3. Select "Body" tab and choose "raw"
4. Copy and paste the entire JSON content in testdata/data_for_10_messages.json
5. Click Send.
6. See the output in IDE console.
7. testdata/data_for_adjustment_messages.json is having "adjustments" json data.

