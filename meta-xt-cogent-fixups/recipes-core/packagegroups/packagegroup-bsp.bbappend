
# In DomD do_rootfs fails on check_data_file_clashes task, because both
# linux-libc-headers-dev and linux-renesas-uapi provide rcar-imr.h
# Remove linux-renesas-uapi from packagegroup-bsp in meta-rcar meta-layer
# to avoid file collision.
RDEPENDS:packagegroup-bsp-utest:remove = " \
    linux-renesas-uapi \
"

# gstreamer related packages should be used only if we have wayland enabled
# in the build. Otherwise (console only build) those packages need to be removed.
RDEPENDS:packagegroup-bsp-utest:remove = " \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-base-app \
    libgstallocators-1.0 \
    libgstapp-1.0 \
"
RDEPENDS:packagegroup-bsp-utest:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'gstreamer1.0-plugins-base', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'gstreamer1.0-plugins-base-app', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'libgstallocators-1.0', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'libgstapp-1.0', '', d)} \
"
