# UK Postcodes

https://github.com/agdturner/agdt-java-generic-data-UKPostcode/

Java for handling UK Office for National Statistics Postcode Directory (ONSPD) and UK National Statistics Postcode Lookup (NSPL) data.

This library provides a means to check if Strings are UK unit, sector, district or area postcodes as recorded in a given version of the ONSPD or NSPL. There are also convenient methods for looking up the coordinates of centroids of unit postcodes and what other spatial regions that postcode is recorded as being situated in.

Details of the ONSPD and NSPL can be found via:
https://www.ons.gov.uk/methodology/geography/geographicalproducts/postcodeproducts
1. ONSPD data contains current and terminated postcodes for the UK along with a details of what other geographical areas centroids of these poscodes are within.
2. NSPL data is similar to the ONSPD data, but it was generated using a different method for attributing which other areas a postcode is within. The NSPL is recommended for statistical reporting uses instead of the ONSPD.

As of November 2019, new versions of the data are being released around about every 3 months - a practice which has been ongoing for around a decade. The latest versions were released in 2019.

In the UK, postcodes have been used for around 50 years. Each postcode effectively represents a set of delivery point addresses. The definitions of postcodes can change over time relating to the challenges of delivering mail to these addresses. As the UK and the demands of deivering mail change over time, so do postcodes. New postcodes come into use, old postcodes are retired or terminated, and delivery addresses are added to, removed from and swapped between existing postcodes.

Given the addresses, for the most part, the UK can be classified into distict simple non-overlapping contiguous regions. Others have done this work and have released data demarking these unit, sector, district and area postcodes regions. Beware though that postcode areas can vary greatly in size and can be complex shapes owing to the vaguaries of accessing deliver addresses. Residential high rise buildings may have more than one postcode with effectively the same 2D centroid.

The ONSPD contains details of current and terminated postcodes for the UK. The details available for most postcodes include the centroid, UK human population census codes, statistical area codes, political area codes, health, education and other authority codes. There are also details about when the postcode first came into use and if and when it's use was terminated.

The NSPL contains similar detail to the ONSPD, but does not contain detail on terminated postcodes.

As the ONSPD and NSPL allows for the looking up of other area codes, and because the geometry of these as well as unit, sector, district and area postcodes are available, then this means that data attributed with postcodes can be processed and geographically mapped in a number of ways. This library facilitates such geographical data processing.

In order to geographically map and visualise the data in other ways, additional software is required. Open source versions of appropriate software are available, including some written in Java.

## Usages
1. To check if Strings are active or terminated UK unit, sector, district or area postcodes as recorded in a specific version of the ONSPD.
2. To look up coordinates of centroids of unit postcodes and what other spatial regions that postcode is recorded as being situated in (given a specific version of the ONSPD or NSPL).

## Code status and development roadmap
Actively being developed and working towards a Version 1.0.0 release.
Once this has been released periodic updates will be needed as new data becomes available.

## Platform requirements
Whilst built using Java 11, this will probably compile and run on Java 8.

## Dependencies
Please see the pom.xml for details.
1. In order for this library to be useful it hould be used in conjunction with some ONSPD or NSPL data.
1. UK Postcode Checker: https://github.com/agdturner/agdt-java-generic-UKPostcodeChecker/. A Java tool for checking Strings to see if they might be viable UK unit, sector, district or area postcodes and for returning the type of unit, sector and district postcodes.
2. Data: https://github.com/agdturner/agdt-java-generic-data/. A Java library for data processing.
3. Census: https://github.com/agdturner/agdt-java-generic-data-Census/. A Java library for loading and processing 2001 and 2011 UK human population census data.
4. Generic: https://github.com/agdturner/agdt-java-generic. A Java library with generally useful classes and methods that have been abstracted from other libraries which now depend on this.

Unit tests are written using JUnit 5. So there is a dependency on that for a fully tested build.

## Contributions
Please raise issues and submit pull requests in the usual way. Contributions will be acknowledged.

## LICENCE
Please see the standard Apache 2.0 open source LICENCE.
