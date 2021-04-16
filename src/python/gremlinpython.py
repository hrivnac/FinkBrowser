#pip install gremlinpython

from gremlin_python import statics
from gremlin_python.process.anonymous_traversal import traversal
from gremlin_python.process.graph_traversal import __
from gremlin_python.process.strategies import *
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

g = traversal().withRemote(DriverRemoteConnection('ws://localhost:8182/gremlin','g'))

x = g.V().has('lbl', 'alert').limit(1).valueMap().next()
# Geoshape is JanusGraph extension, so it doesn't work in Gremlin-only client
#x = g.V().has('lbl', 'candidate').has('direction', geoWithin(Geoshape.circle(-26, -153, 0.0005*6371.0087714*180/Math.PI)))
print(x) 

query = "g.V().has('lbl', 'alert').limit(1).valueMap()"
x = gremlin_client.submit(query).next()
print(x)

# sometimes fails (bug ?)
query = "g.V().has('lbl', 'candidate').has('direction', geoWithin(Geoshape.circle(-26, -153, 5*6371.0087714*180/Math.PI))).valueMap()"
x = gremlin_client.submit(query).one()[0].get('direction')[0].get('@value')
print(x)