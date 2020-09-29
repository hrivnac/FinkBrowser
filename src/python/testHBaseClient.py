import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

# ../dist/FinkBrowser.exe.jar
jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=" + sys.argv[1], convertStrings=False)

import java.lang
import java.util

import com.Lomikel.HBaser
print(sys.argv[1])
client = com.Lomikel.HBaser.HBaseClient("134.158.74.54", 2181);
client.connect("test_portal_tiny.3", "schema_0.7.0_0.3.6");
client.setLimit(10);
print(client.scan("", "key:key:ZTF19", "i:candid", "1000"));
client.close();

jpype.shutdownJVM()
