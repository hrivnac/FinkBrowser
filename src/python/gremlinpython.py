#pip install gremlinpython

from gremlin_python import statics
from gremlin_python.process.anonymous_traversal import traversal
from gremlin_python.process.graph_traversal import __
from gremlin_python.process.strategies import *
from gremlin_python.driver import client
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection
from gremlin_python.process.traversal import T
from gremlin_python.process.traversal import Order
from gremlin_python.process.traversal import Cardinality
from gremlin_python.process.traversal import Column
from gremlin_python.process.traversal import Direction
from gremlin_python.process.traversal import Operator
from gremlin_python.process.traversal import P
from gremlin_python.process.traversal import Pop
from gremlin_python.process.traversal import Scope
from gremlin_python.process.traversal import Barrier
from gremlin_python.process.traversal import Bindings
from gremlin_python.process.traversal import WithOptions

statics.load_statics(globals())

# for direct calls
g = traversal().withRemote(DriverRemoteConnection('ws://134.158.74.85:24444/gremlin','g'))

# for submitting scripts 
client = client.Client('ws://134.158.74.85:24444/gremlin', 'g')

x = g.V().has('lbl', 'alert').limit(1).valueMap().next()
print(x) 

x = g.V().has('lbl', 'candidate').has('jd', inside(2459324.90447, 2459324.90448)).in_().values('objectId').next()
print(x)

query = "g.V().has('lbl', 'alert').limit(1).valueMap()"
x = client.submit(query).next()
print(x)

query = "g.V().has('lbl', 'candidate').has('direction', geoWithin(Geoshape.circle(5.475797, -87.662704, 0.00001*6371.0087714*180/Math.PI))).valueMap()"
x = client.submit(query).one()[0].get('direction')[0].get('@value')
print(x)

