import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=/home/hrivnac/work/LSST/FinkBrowser/lib/FinkBrowser.exe.jar", convertStrings=False)

import java.lang
import java.util

import com.Lomikel.HBaser

client = com.Lomikel.HBaser.HBaseClient("localhost", 2181);
client.connect("test_portal_tiny.1", "schema_v0");
client.setLimit(10);
print(client.scan("", "key:key:ZTF19", "i:candid", "10000"));
client.close();

jpype.shutdownJVM()
