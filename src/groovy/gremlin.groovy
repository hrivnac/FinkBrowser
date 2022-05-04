import com.Lomikel.Januser.StringGremlinClient;
import org.apache.tinkerpop.gremlin.driver.Result;
StringGremlinClient client = new StringGremlinClient("134.158.74.85", 24444);
if (oformat.equals("txt")) {
  StringBuffer answer = new StringBuffer("");
  for (Result result : client.interpret(gremlin)) {
    answer.append(result.getObject().toString()).append("\n");
    }
  return answer.toString();
  }
else if (oformat.equals("json")) {
  return client.interpret2JSON(gremlin);
  }
else {
  return "Unknown or unavailable output-format " + oformat;
  }

                                  