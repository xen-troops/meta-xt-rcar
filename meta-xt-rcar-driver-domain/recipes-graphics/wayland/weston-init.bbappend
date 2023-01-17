do_install:append () {
    sed -e '$a\\' \
        -e '$a\echo "XDG_RUNTIME_DIR=/run/user/`id -u weston`" > /etc/default/weston' \
        -i ${D}/${sysconfdir}/profile.d/weston.sh
}
