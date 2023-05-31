# Move u-boot into 'firmware' folder
inherit collect_firmware

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://0001-Revert-net-ravb-Fix-stop-RAVB-module-clock-before-OS.patch \
"

###
# Add default environment

# xxd is required by u-boot if we use CONFIG_USE_DEFAULT_ENV_FILE
DEPENDS += " xxd-native"

# do_configure() from `meta-renesas` scans SRC_URI
# for '*.cfg' and appends them to u-boot's config.
# This allows us to add two self-explaining options
# through enable_env.cfg.
# Specified uEnv.txt file should be accessible
# during the compilation, so we place it to
# the sources directory `git` using `subdir` option
# of SRC_URI. Make sure that 'subdir' for uEnv.txt
# is aligned with {S} in case of any changes.
SRC_URI += "\
    file://enable_env.cfg \
    file://uEnv.txt;subdir=git \
"

# MAC-related part.
# U-boot requires MAC to be set (ethaddr) for proper work of TFTP commands.
# But we have no hardcoded info with vendor-assigned MAC.
# So, we generate a partially random MAC, with the first three bytes
# used the same as we see on the board stickers.
# This allows us to have different MACs for the various builds.
# But pay attention that two boards flashed with the same image will have
# equal MAC, and this needs to be changed manually.
python do_generate_random_mac () {
    import random

    filepath = os.path.join(d.getVar("S"), "uEnv.txt")

    f = open(filepath, "r")
    content = f.read()
    f.close()

    # if there is no 'ethaddr' provided in uEnv.txt, then generate one
    if "ethaddr=" not in content:
        # the first three bytes are the same as used on R-Car boards
        new_mac = f"ethaddr=2E:09:0A:{random.randrange(255):02X}:{random.randrange(255):02X}:{random.randrange(255):02X}"
        f = open(filepath, "a")
        f.write(new_mac)
        f.close()
}

addtask do_generate_random_mac after do_unpack before do_configure
