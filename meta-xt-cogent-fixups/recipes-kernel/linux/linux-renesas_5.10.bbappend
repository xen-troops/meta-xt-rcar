FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Important note
# Patch ("...-Add-multichannel-audio-ranges-...") relates to Renesas' kernel
# and has no relation to Cogent's sources.
# So, why it is inside meta-xt-cogent-fixups?
# This patch should be applied to Renesas' kernel only if we build for h3ulcb-4x2g-kf machine.
# So we have two options:
#   a) to create separate layer meta-xt-rcar-fixups-kf just for one few-lines patch;
#   b) to ride on top of this meta-xt-cogent-fixups layer which is used only in the case of KF.
# That's why option b) was selected and "...-Add-multichannel-audio-ranges-..."
# is located inside this layer.
SRC_URI:append = " \
    file://0001-r8a7795-6-65-.dtsi-Add-multichannel-audio-ranges-tha.patch \
"
