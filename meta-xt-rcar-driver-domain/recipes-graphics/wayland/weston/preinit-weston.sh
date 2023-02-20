#!/bin/sh

FILE=/etc/default/weston

if ! [ -s ${FILE} ]; then # if the file is empty
    # injecting the XDG_RUNTIME_DIR parameter
    echo "XDG_RUNTIME_DIR=/run/user/$(id -u weston)" > ${FILE}
fi
