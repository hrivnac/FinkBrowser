import com.Lomikel.Januser.StringGremlinClient;
StringGremlinClient client = new StringGremlinClient("134.158.74.85", 24444);
print(client.interpret("importStatus()"));
System.exit(0);