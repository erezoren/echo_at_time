# echo_at_time

## Redis
To init redis you can either install it locally and run.
<p><b>OR</b></p> Start the redis docker image as follows:</br>
<pre>
From the command line run ./redis.sh start
</pre>
To stop it  and remove the container run
<pre>
From the command line run ./redis.sh finish
</pre>

## Server application
You can either run EchoAtTimeApplication.java from any java IDE
<p><b>OR</b></p>
Execute Maven clean package.</br>
Inthe commandline cd to where the created jar is located</br>
execute 
<pre>java -jar target/echo_at_time-0.0.1-SNAPSHOT.jar</pre>


## Connect to server
It is advised to use NetCat to connect to the server
<pre>nc 127.0.0.1 10101</pre>

## Send messages
The server excepts messages in the following format:

<pre>message:some text;time:10000</pre>
  
*The stated time is in milliseconds

## Output
The server will scan redis periodically to fetch waiting messages and will 
print them to the client. 