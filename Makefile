JCC = javac
JVM = java
JFLAGS = -g


.java.class:
	$(JCC) $(JFLAGS) $*.java


CP = src/geoexplorer:src/libs/postgis.jar:src/libs/postgresql.jar

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
		$(JVM) $(MAIN)


APP = src/geoexplorer/geo/ensimag/
TEST = $(APP)/tests
build: clean
	$(JCC) $(JFLAGS) -cp $(CP) $(TEST)/TestConnection.java
	$(JCC) $(JFLAGS) -cp $(CP) $(TEST)/TestLikeRequest.java
	$(JCC) $(JFLAGS) -cp $(CP) $(TEST)/TestGui.java
	$(JCC) $(JFLAGS) -cp $(CP) $(APP)/GrenobleMap.java


question10: build
	$(JVM) -cp $(CP) geo.ensimag.tests.TestConnection

question11: build
	$(JVM) -cp $(CP) geo.ensimag.tests.TestLikeRequest
RM = rm -f

testgui: build
	$(JVM) -cp $(CP) geo.ensimag.tests.TestGui
question12: build
	$(JVM) -cp $(CP) geo.ensimag.GrenobleMap


clean:
	$(RM) src/geoexplorer/geoexplorer/gui/*.class
	$(RM) src/geoexplorer/database/*.class
	$(RM) $(APP)/*.class
	$(RM) $(TEST)/*.class
