
# In DomD do_rootfs fails on check_data_file_clashes task, because both
# linux-libc-headers-dev and linux-renesas-uapi provide rcar-imr.h
# Remove linux-renesas-uapi from packagegroup-bsp in meta-rcar meta-layer
# to avoid file collision.
RDEPENDS:packagegroup-bsp-utest:remove = " \
    linux-renesas-uapi \
"
