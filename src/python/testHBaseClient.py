import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

# ../dist/FinkBrowser.exe.jar
jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=" + sys.argv[1], convertStrings=False)

import java.lang
import java.util

import com.Lomikel.HBaser

true  = jpype.java.lang.Boolean(True)
false = jpype.java.lang.Boolean(False)

client = com.Lomikel.HBaser.HBaseClient("localhost", 2181)
client.connect("test_portal_tiny.3", "schema_0.7.0_0.3.6")
client.setLimit(10)

print(client.scan(None, "key:key:ZTF17", None, 100000, true, true)) 

client.close()

jpype.shutdownJVM()
