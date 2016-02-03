#!/bin/sh
pro="lining"
if [ "$1" != "" ]
then
	pro=$1
fi
mvn clean package -P $pro -Dbanben=4.9
