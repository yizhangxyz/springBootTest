title "spingbootTest"


set TestDir=target

%~d0
cd %~dp0\%TestDir%

java -jar daxionggames-1.0.0.RELEASE.jar
 
:eofx
pause
