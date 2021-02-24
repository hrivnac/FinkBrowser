#!/bin/bash

PRESENT=`grep -c com.astrolabsoftware.FinkBrowser.GremlinPlugin.FinkBrowserConnector $1`

if [[ "${PRESENT}" == "0" ]]; then
  echo "com.astrolabsoftware.FinkBrowser.GremlinPlugin.FinkBrowserConnector" >> $1
  fi
