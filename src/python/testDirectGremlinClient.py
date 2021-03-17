import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

# ../dist/FinkBrowser.exe.jar
jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=" + sys.argv[1], convertStrings=False)

from com.Lomikel.Januser                    import DirectGremlinClient
from com.astrolabsoftware.FinkBrowser.Utils import Init

Init.init()

client = DirectGremlinClient("134.158.74.85", 24444);

g = client.g();
print(g.V().hasLabel('alert').limit(1).valueMap().next());


client.close()

jpype.shutdownJVM()
