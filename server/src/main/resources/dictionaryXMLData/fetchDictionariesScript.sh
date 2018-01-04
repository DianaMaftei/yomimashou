#!/usr/bin/env bash
curl -L http://ftp.monash.edu/pub/nihongo/JMdict_e.gz | gzip -d > JMdict_e.xml
curl -L http://ftp.monash.edu/pub/nihongo/JMnedict.xml.gz | gzip -d > JMnedict.xml
curl -L http://ftp.monash.edu/pub/nihongo/kanjidic2.xml.gz | gzip -d > kanjidic2.xml