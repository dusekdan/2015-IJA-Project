#!/bin/bash
wget http://eva.fit.vutbr.cz/~xdusek21/images.tar.gz
mv images.tar.gz lib/
cd lib
tar xfz images.tar.gz
