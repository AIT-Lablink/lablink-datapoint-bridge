Invoking the bridge from the command line
==========================================

The runnable JAR is produced as described in :doc:`installation`.
``-c`` is the URL of the configuration (see :doc:`configuration`).

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

``-t`` selects how that URL is read.
``json`` is the default and treats the body as JSON.
``python`` treats the body as a Jython script that returns the JSON.
The comparison is case-insensitive.

.. code-block:: winbatch

   java -jar %DPB_JAR% -c %CONFIG_URL% -t python

Without ``-c``, the client reads ``file:///<working directory>/resources/dpbconfig.json``.
``-h`` prints the usage text and exits.

Printing the connection diagram
===============================

``-p`` writes a PNG of the datapoints and connections into the working directory.
The drawing is produced from the PlantUML description the bridge builds while it reads the configuration.

.. code-block:: winbatch

   java -jar %DPB_JAR% -c %CONFIG_URL% -p

Running with Docker
===================

The image ``ait1/lablink-datapoint-bridge:latest`` (see :doc:`installation`) starts the bridge with ``-c`` set to ``${LLCONFIG}${DPB_CONFIG}``.
The defaults are ``LLCONFIG=http://lablink-config:10101/get?id=`` and ``DPB_CONFIG=lablink-dpbridge.config``, so the image expects a configuration server reachable as the host ``lablink-config``.
Override both variables to point at another server.
``MEMORY_FLAG`` is the JVM heap setting and defaults to ``-Xmx512m``.

On Windows, a configuration server on the host is reachable as ``host.docker.internal``:

.. code-block:: winbatch

   docker run --rm -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.config ait1/lablink-datapoint-bridge:latest

On Linux, add that hostname explicitly:

.. code-block:: bash

   docker run --rm --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.config ait1/lablink-datapoint-bridge:latest

Arguments after the image name are passed through to the bridge.
Use them for ``-t`` and ``-p``:

.. code-block:: bash

   docker run --rm --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e DPB_CONFIG=ait.demo.dpbridge.config ait1/lablink-datapoint-bridge:latest -t python
