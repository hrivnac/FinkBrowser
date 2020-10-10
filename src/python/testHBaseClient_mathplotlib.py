import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

import matplotlib.pyplot as plt

# ../dist/FinkBrowser.exe.jar
jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=" + sys.argv[1], convertStrings=True)

from com.Lomikel.HBaser import HBaseClient

true  = jpype.java.lang.Boolean(True)
false = jpype.java.lang.Boolean(False)

#client = HBaseClient("localhost", 2181)
client = HBaseClient("134.158.74.54", 2181);
client.connect("test_portal_tiny.3", "schema_0.7.0_0.3.6")
#client.setLimit(10)

a17_x = [];
a17_y = [];
a19_x = [];
a19_y = [];
for r in client.scan("", "key:key:ZTF17", "i:ra,i:dec", 0, false, false).values():
  a17_x += [float(r['i:ra'])]
  a17_y += [float(r['i:dec'])]
for r in client.scan("", "key:key:ZTF19", "i:ra,i:dec", 0, false, false).values():
  a19_x += [float(r['i:ra'])]
  a19_y += [float(r['i:dec'])]

plt.plot(a17_x, a17_y, 'b.')
plt.plot(a19_x, a19_y, 'r.')
plt.title('ZTF17(red) + ZTF19(lightblue)')
plt.xlabel('ra')
plt.ylabel('dec')
plt.show()

client.close()

jpype.shutdownJVM()
