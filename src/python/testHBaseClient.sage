import sys

import jpype
import jpype.imports
from jpype import JImplements, JOverride, JImplementationFor

jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path=../dist/FinkBrowser.exe.jar", convertStrings=True)

import java.lang
import java.util

import com.Lomikel.HBaser
import com.Lomikel.Utils.DateTimeManagement

client = com.Lomikel.HBaser.HBaseClient("134.158.74.54", 2181);
client.connect("test_portal", "schema_v0");
client.setLimit(1000);
dec = client.scan(None, None, "i:ra,i:dec", None);
a = [];
for d in dec.split("\n"):
  dd = d.split(" = ");
  print(dd);
  try:
    ddd = dd[1].split(",");
    ra = ddd[0].split("=")[1];
    dec = ddd[1].split("=")[1][:-1];
    a += [(float(ra), float(dec))];
  except:
    print("");
p = list_plot(a);
#s = spline(a)
#p += plot(s, 0, maxx,  scale = 'linear', color = 'red', legend_label = 'xxx')
show(p);
#client.close();

#jpype.shutdownJVM()
