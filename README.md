

Project Structure
--------------------

* clc-java-sdk - module that contains all projects related to CLC Java SDK
    * sample - module that contains sample application
        * client - module contains web client of sample application
        * app - module contains backend of sample application
    * sdk - module that contains SDK library


Continuous Integration
----------------------

CI Specification:

 Parameter    | Value
 -------------|------------------
 URL          | http://66.155.4.208:8080/
 Authentication | GitHub Account Credentials
 IP Address   | 66.155.4.208
 Ssh Credentials | ci / 1qa@WS3ed
 Jdk Home     | /usr/local/jdk1.8.0_40
 Gradle Home  | /usr/local/gradle-2.1.1
 
***TODO items:***
* configure continuous installation of web client
* move from pulling changes tracking to GitHub repo webhooks


Project Documentation
---------------------
* [Architecture Specification](https://docs.google.com/document/d/1aSlv1wPeGxo4w7nY-X8u3QX-0NmITAJoPLAHIB_70Cc/edit?usp=sharing)
* [Scope Description](https://docs.google.com/document/d/16_i2pxJk9bgP5fgwwkiKveqSAm0eAdgsnWBpdtIlTLY/edit)
* [Business Scenarios](https://docs.google.com/document/d/10RTqkJ0tYmeV_S5nH0xcYcPIkXANp3mVEQOD-HGTM7E/edit?usp=sharing)
* [Schedule](https://docs.google.com/spreadsheets/d/12M36PdDvlbovWbvaJ_HULh1pqF24VgQOQrYmGIlzO38/edit?usp=sharing)