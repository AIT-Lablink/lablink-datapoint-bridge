Overview
========

The bridge reads one configuration and then subscribes to every datapoint listed in it.
Each entry in ``Connections`` copies a new value from ``From`` to ``To``.
Both ends must already appear in ``DataPoints``, and they must use the same datatype.

.. seealso:: See `below <#example-configuration>`_ for a complete JSON document, and `Python scripting <#python-scripting>`_ for generating that document with Jython.

Client fields
=============

.. topic:: Required fields

  :*ClientName*: name of this bridge client
  :*GroupName*: group name
  :*ScenarioName*: scenario name
  :*labLinkPropertiesUrl*: URI of the Lablink connection properties
  :*syncHostPropertiesUrl*: URI of the synchronization host properties

Datapoints
==========

``DataPoints`` is a JSON array.
Each object describes one datapoint on some other Lablink client.

.. topic:: Fields of each datapoint

  :*ID*: name used by ``Connections``. It is local to this configuration.
  :*DPGroupName*: group of the remote client
  :*DPClientName*: name of the remote client
  :*DPName*: datapoint name on that client
  :*DPIdentifier*: datapoint identifier on that client
  :*DPDataType*: one of ``long``, ``bool``, ``string``, ``double``, or ``complex``

Connections
===========

``Connections`` is a JSON array of one-way links.

.. topic:: Fields of each connection

  :*From*: ``ID`` of the source datapoint
  :*To*: ``ID`` of the destination datapoint

``From`` and ``To`` must both be present in ``DataPoints``.
Their ``DPDataType`` values must be equal.
When the source receives a new value, the bridge writes that value to each destination on its own.
A destination that is not connected, or a write that fails, is logged and skipped.
The other destinations of that source still receive the value.
The next source update is written to a destination once that destination is connected.

Environment variables
=====================

Any string in the configuration may contain one ``%NAME%`` placeholder.
If the environment variable ``NAME`` is set, that placeholder is replaced with its value.
Otherwise the string is left unchanged.
Only the first pair of ``%`` characters in a string is considered.

Comments in a JSON file
=======================

When the configuration is read as JSON, a comment is text on one line that starts and ends with ``#``.
Those comments are removed before parsing.
A ``#`` that is left over is an error.
Comment removal is not applied to JSON produced by a Python script.

Example configuration
=====================

The following document bridges one ``double`` datapoint from ``SourceClient`` to ``SinkClient``.
Stored under the identifier ``ait.demo.dpbridge.config``, it is what the bridge receives from ``http://localhost:10101/get?id=ait.demo.dpbridge.config``.

.. code-block:: json

   {
      "ClientName": "DpBridge",
      "GroupName": "Demo",
      "ScenarioName": "BridgeDemo",
      "labLinkPropertiesUrl": "http://localhost:10101/get?id=ait.demo.llproperties",
      "syncHostPropertiesUrl": "http://localhost:10101/get?id=ait.demo.sync-host.properties",
      "DataPoints": [
         {
            "ID": "src",
            "DPGroupName": "Demo",
            "DPClientName": "SourceClient",
            "DPName": "power",
            "DPIdentifier": "P",
            "DPDataType": "double"
         },
         {
            "ID": "dst",
            "DPGroupName": "Demo",
            "DPClientName": "SinkClient",
            "DPName": "power",
            "DPIdentifier": "P",
            "DPDataType": "double"
         }
      ],
      "Connections": [
         {
            "From": "src",
            "To": "dst"
         }
      ]
   }

Python scripting
================

Pass ``-t python`` to read the ``-c`` URL as a Jython script instead of as JSON.
The bridge loads the script and then runs these two statements:

.. code-block:: python

   config = DPBLablinkConfiguration()
   cfg = config.getJSONConfig()

The class must be named ``DPBLablinkConfiguration``.
The source looks up that name exactly.
``getJSONConfig`` is called with no arguments and must return a string.
That string is parsed with the JSON rules above, including ``%NAME%`` replacement.
``#`` comments are not removed from the generated string.
If the script raises, the bridge logs the error and exits with status 255.

A script can assemble the datapoint list in a loop and return the same document as the example above:

.. code-block:: python

   class DPBLablinkConfiguration:
       def getJSONConfig(self):
           ends = [("src", "SourceClient"), ("dst", "SinkClient")]
           points = []
           for point_id, client in ends:
               points.append(
                   '{"ID":"%s","DPGroupName":"Demo","DPClientName":"%s",'
                   '"DPName":"power","DPIdentifier":"P","DPDataType":"double"}'
                   % (point_id, client)
               )
           return (
               '{"ClientName":"DpBridge","GroupName":"Demo",'
               '"ScenarioName":"BridgeDemo",'
               '"labLinkPropertiesUrl":"http://localhost:10101/get?id=ait.demo.llproperties",'
               '"syncHostPropertiesUrl":"http://localhost:10101/get?id=ait.demo.sync-host.properties",'
               '"DataPoints":[%s],'
               '"Connections":[{"From":"src","To":"dst"}]}'
           ) % ",".join(points)

Jython here is Python 2. Run the script with ``-t python``, as shown in :doc:`examples`.
