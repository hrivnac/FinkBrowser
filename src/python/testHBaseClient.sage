import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=../dist/FinkBrowser.exe.jar", convertStrings=True)

import java.lang
import java.util

import com.Lomikel.HBaser

true  = jpype.java.lang.Boolean(True)
false = jpype.java.lang.Boolean(False)

client = com.Lomikel.HBaser.HBaseClient("localhost", 2181)
client.connect("test_portal_tiny.3", "schema_0.7.0_0.3.6")
#client.setLimit(1000);
a17 = [];
a19 = [];
for r in client.scan("", "key:key:ZTF17", "i:ra,i:dec", 100000, false, false).values():
  a17 += [(float(r['i:ra']), float(r['i:dec']))];
for r in client.scan("", "key:key:ZTF19", "i:ra,i:dec", 100000, false, false).values():
  a19 += [(float(r['i:ra']), float(r['i:dec']))];
p = list_plot(a17, color='red') + list_plot(a19, color='lightblue');
show(p, axes_labels = ('ra', 'dec'), title='ZTF17(red) + ZTF19(lightblue)');

#client.close();

#jpype.shutdownJVM()
