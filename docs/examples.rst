Prerequisites
=============

The configuration server described in the `configuration server manual <https://ait-lablink.readthedocs.io/projects/ait-lablink-config-server>`_ must already be running and serving two entries:

* ``ait.demo.llproperties``, the Lablink connection properties
* ``ait.demo.dpbridge.config``, the JSON document from :doc:`configuration`

An MQTT broker must be reachable at the address in those connection properties.
The clients named ``SourceClient`` and ``SinkClient`` are the ends of the bridge.
They have to be running for a value to be copied.

The walkthrough uses the runnable JAR described in :doc:`installation`.
Each step also shows the Docker image ``ait1/lablink-datapoint-bridge:latest``.

Running the bridge
==================

On Windows:

.. code-block:: winbatch

   SET DPB_JAR=target\assembly\dpbridge-<VERSION>-jar-with-dependencies.jar
   SET CONFIG_URL=http://localhost:10101/get?id=ait.demo.dpbridge.config

   java -jar %DPB_JAR% -c %CONFIG_URL%

On Linux:

.. code-block:: bash

   DPB_JAR=target/assembly/dpbridge-<VERSION>-jar-with-dependencies.jar
   CONFIG_URL='http://localhost:10101/get?id=ait.demo.dpbridge.config'

   java -jar "$DPB_JAR" -c "$CONFIG_URL"

The bridge subscribes to both datapoints.
A new value on ``SourceClient`` datapoint ``power`` / ``P`` is written to the same datapoint on ``SinkClient``.

With the Docker image on Windows:

.. code-block:: winbatch

   docker run --rm -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.config ait1/lablink-datapoint-bridge:latest

On Linux:

.. code-block:: bash

   docker run --rm --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.config ait1/lablink-datapoint-bridge:latest

``LLCONFIG`` and ``DPB_CONFIG`` are joined into the same URL passed to ``-c``.

Python scripting
================

Save the ``DPBLablinkConfiguration`` script from :doc:`configuration` as ``bridge.py``.
Run it with ``-t python``.
The URL must return the script text.

From a local file, on Windows:

.. code-block:: winbatch

   java -jar %DPB_JAR% -t python -c file:///%CD%/bridge.py

On Linux:

.. code-block:: bash

   java -jar "$DPB_JAR" -t python -c "file://$(pwd)/bridge.py"

The same script can be stored on the configuration server.
``/get`` must return the script itself, because ``-t python`` executes that response as Jython.

.. code-block:: winbatch

   java -jar %DPB_JAR% -t python -c http://localhost:10101/get?id=ait.demo.dpbridge.script

With the Docker image, arguments after the image name are forwarded to the bridge.
On Linux:

.. code-block:: bash

   docker run --rm --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.script ait1/lablink-datapoint-bridge:latest -t python

On Windows:

.. code-block:: winbatch

   docker run --rm -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.script ait1/lablink-datapoint-bridge:latest -t python

``getJSONConfig`` still returns the JSON document from :doc:`configuration`, so the bridge connects the same two datapoints.
