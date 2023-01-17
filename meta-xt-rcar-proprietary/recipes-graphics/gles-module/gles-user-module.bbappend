# We need to overwrite original do_install()
# to avoid conflict with gles-module-egl-headers.
# Removed block is marked by comment 'This block is intentionally removed'
do_install() {
    # Install configuration files
    install -d ${D}${sysconfdir}/init.d
    install -m 644 ${S}/etc/powervr.ini ${D}${sysconfdir}
    install -m 755 ${S}/etc/init.d/rc.pvr ${D}${sysconfdir}/init.d/pvrinit
    install -m 755 ${S}/etc/init.d/rc.pvr ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 644 ${S}/etc/udev/rules.d/72-pvr-seat.rules ${D}${sysconfdir}/udev/rules.d/

    # Install header files
    # This block is intentionally removed

    # Install pre-builded binaries
    install -d ${D}${libdir}
    install -m 755 ${S}/usr/lib/*.so ${D}${libdir}/
    install -d ${D}${RENESAS_DATADIR}/bin
    install -m 755 ${S}/usr/local/bin/dlcsrv_REL ${D}${RENESAS_DATADIR}/bin/dlcsrv_REL
    install -d ${D}/lib/firmware
    install -m 644 ${S}/lib/firmware/* ${D}/lib/firmware/

    # Install pkgconfig
    install -d ${D}${libdir}/pkgconfig
    install -m 644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Create symbolic link
    cd ${D}${libdir}
    ln -s libEGL.so libEGL.so.1
    ln -s libGLESv2.so libGLESv2.so.2

    if [ "${USE_GLES_WAYLAND}" = "1" ]; then
        # Set the "WindowSystem" parameter for wayland
        if [ "${GLES}" = "gsx" ]; then
            sed -i -e "s/WindowSystem=libpvrDRM_WSEGL.so/WindowSystem=libpvrWAYLAND_WSEGL.so/g" \
                ${D}${sysconfdir}/powervr.ini
        fi
    fi

    # Install systemd service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)} ; then
        install -d ${D}${systemd_system_unitdir}/
        install -m 644 ${WORKDIR}/rc.pvr.service ${D}${systemd_system_unitdir}/

        install -d ${D}${exec_prefix}/bin
        install -m 755 ${S}/etc/init.d/rc.pvr ${D}${exec_prefix}/bin/pvrinit
    fi
}

