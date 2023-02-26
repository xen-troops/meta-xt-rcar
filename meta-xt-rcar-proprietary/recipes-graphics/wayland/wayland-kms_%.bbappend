# HACK: This workaround circular dependency:
#
# Dependency loop #1 found:
# Task /src/meta-xt-rcar/meta-xt-rcar-proprietary/recipes-graphics/gles-module/gles-user-module_1.11.bb:do_package
#          (dependent Tasks ['initscripts_1.0.bb:do_packagedata',
#                            'update-rc.d_0.8.bb:do_packagedata',
#                            'wayland-protocols_1.20.bb:do_packagedata',
#                            'rpm_4.14.2.1.bb:do_populate_sysroot',
#                            'libgbm.bb:do_packagedata',
#                            'glibc_2.31.bb:do_packagedata',
#                            'gles-user-module_1.11.bb:do_install',
#                            'linux-renesas_5.10.bb:do_packagedata',
#                            'dwarfsrcfiles.bb:do_populate_sysroot',
#                            'pseudo_git.bb:do_populate_sysroot',
#                            'gles-module-egl-headers_1.11: bb:do_packagedata',
#                            'wayland-kms_1.6.0.bb:do_packagedata',
#                            'gcc-runtime_9.3.bb:do_packagedata'])
# Task /src/meta-xt-rcar/meta-xt-rcar-proprietary/recipes-graphics/gles-module/gles-user-module_1.11.bb:do_packagedata
#          (dependent Tasks ['gles-user-module_1.11.bb:do_package'])
# Task /yocto/meta-renesas/meta-rcar-gen3/recipes-graphics/wayland/wayland-kms_1.6.0.bb:do_package
#          (dependent Tasks ['wayland-kms_1.6.0.bb:do_install',
#                            'libdrm_2.4.101.bb:do_packagedata',
#                            'rpm_4.14.2.1.bb:do_populate_sysroot',
#                            'gles-user-module_1.11.bb:do_packagedata',
#                            'dwarfsrcfiles.bb:do_populate_sysroot',
#                            'wayland_1.18.0.bb:do_packagedata',
#                            'pseudo_git.bb:do_populate_sysroot',
#                            'gcc-runtime_9.3.bb:do_packagedata',
#                            'glibc_2.31.bb:do_packagedata'])
# Task /yocto/meta-renesas/meta-rcar-gen3/recipes-graphics/wayland/wayland-kms_1.6.0.bb:do_packagedata
#           (dependent Tasks ['wayland-kms_1.6.0.bb:do_package'])
#
DEPENDS += " gles-module-egl-headers"
DEPENDS:remove = "gles-user-module"
