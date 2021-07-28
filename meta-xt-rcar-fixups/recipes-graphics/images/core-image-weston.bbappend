# For some reason Renesas wants libx11-locale unconditionally
# We are trying to disable x11 in DISTRO_FEATURES, so remove it from image

IMAGE_INSTALL_remove = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', 'libx11-locale', d)}"
