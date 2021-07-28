# meta-xt-rcar #

This repository contains Renesas RCAR Gen3-specific Yocto layers for
Xen Troops distro. Layers in this repository are
product-independent. They provide common facilities that may be used
by any xt-based product that runs on Resas Gen3-based platforms.

Those layers *may* be added and used manually, but they were written
with [Moulin](https://moulin.readthedocs.io/en/latest/) build system,
as Moulin-based project files provide correct entries in local.conf

List of layers:

* meta-xt-rcar-dom0 - thin dom0-specific recipes. They provide means
  to run thin dom0 on Renesas hardware.

* meta-xt-rcar-domu - domu-specific recipes. They provide means
  to run generic domains on Renesas hardware.

* meta-xt-rcar-driver-domin - driver domain-specific recipes. They
  provide means to run separate DomD or thick Dom0 on Renesas
  hardware.

* meta-xt-rcar-fixups - small fixes that alter official Renesas's
  *meta-rcar* layer behavior to suit our needs. 

* meta-xt-rcar-proprietary - recipes for Renesas proprietary modules
  like GPU or multimedia. Of course we can't distribute source code,
  but we can provides recipes to build them (for those who has
  access).
