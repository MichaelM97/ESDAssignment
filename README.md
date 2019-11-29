# esd_assignment
## Git Guide
[Git Guide](https://rogerdudler.github.io/git-guide/)
## Prviate Rest Service
Enabling access on GlassFish
When deploying to GlassFish you need to enable access to external schema to generate a test client for a web service. To enable access you need to modify the configuration file of the GlassFish Server (GLASSFISH_INSTALL/glassfish/domains/domain1/config/domain.xml) and add the following JVM option element. You will need to restart the server for the change to take effect.

```
<java-config>
  ...
  <jvm-options>-Djavax.xml.accessExternalSchema=all</jvm-options>
</java-config>
```
