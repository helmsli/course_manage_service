PATH=$JAVA_HOME/bin:$PATH
CLASSPATH=$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib:$CLASSPATH
export JAVA_HOME PATH CLASSPATH
java -Duser.timezone=GMT+8 -jar xxxx.jar 1>/dev/null 2>out.error &
