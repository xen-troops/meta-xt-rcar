# pypi.bbclass sets default value to PYPI_SRC_URI
# and appends it to SRC_URI so we can just override
# PYPI_SRC_URI for our purposes.
# 
# For the build of the proprietary DDK we still need
# python-clang_5.0. But it is not available online
# because the author dropped support.
# So we put archive directly into recipes' files.
PYPI_SRC_URI = "file://clang-5-5.0.post2.tar.gz"

inherit setuptools
require python-clang.inc
