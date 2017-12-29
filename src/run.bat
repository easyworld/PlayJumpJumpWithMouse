@echo off

where javac>nul 2>nul
if not %errorlevel%==0 echo can't find javac && goto exit
where java>nul 2>nul
if not %errorlevel%==0 echo can't find java && goto exit

echo compile...
javac -encoding utf-8 com\company\BackgroundImage4Panel.java

echo run...
java com.company.BackgroundImage4Panel


:exit
del s.png
pause

