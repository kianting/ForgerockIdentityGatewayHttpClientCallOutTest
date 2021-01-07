#!/bin/sh
#
# Copyright 2019-2020 ForgeRock AS. All Rights Reserved
#
# Use of this code requires a commercial software license with ForgeRock AS.
# or with one of its affiliates. All use shall be exclusively subject
# to such license between the licensee and ForgeRock AS.

if [ $# -gt 1 ]; then
  echo "Expecting only the instance directory as argument"
  exit 1
elif [ $# -eq 0 ]; then
  INSTANCE_DIR="${HOME}/.openig"
  echo "No instance dir provided, using ${INSTANCE_DIR}"
else
  INSTANCE_DIR=${1}
  shift
fi

env_file="${INSTANCE_DIR}/bin/env.sh"
if [ -r "${env_file}" ]; then
  . "${env_file}"
fi

if [ -z "${JAVA_HOME}" ] ; then
  _java=`which java`
else
  _java="${JAVA_HOME}/bin/java"
fi

if [ ! -x "${_java}" ] ; then
  echo "No java executable found in the JAVA_HOME or through the PATH environment variable."
  exit 1
fi

cd $(dirname $0)/..
eval exec ${_java} -classpath "classes:lib/*:${INSTANCE_DIR}/extra/*" "${JAVA_OPTS}" org.forgerock.openig.launcher.Main ${INSTANCE_DIR} $@
