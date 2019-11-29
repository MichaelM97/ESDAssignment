# esd_assignment
## Git Guide
[Git Guide](https://rogerdudler.github.io/git-guide/)
## Private Rest Service Setup
### Configuring the IDE
To generate a web service client in the IDE from a web service or WSDL file you need to modify the IDE configuration file (netbeans.conf) to add the following switch to netbeans_default_options.

`-J-Djavax.xml.accessExternalSchema=all`
For more about locating and modifying the netbeans.conf configuration file, see Netbeans Conf FAQ.

### Configuring the GlassFish Server
If you are deploying to the GlassFish Server you need to modify the configuration file of the GlassFish Server (domain.xml) to enable the server to access external schemas to parse the wsdl file and generate the test client. To enable access to external schemas, open the GlassFish configuration file (GLASSFISH_INSTALL/glassfish/domains/domain1/config/domain.xml) and add the following JVM option element (in bold). You will need to restart the server for the change to take effect.

```
</java-config>
  ...
  <jvm-options>-Djavax.xml.accessExternalSchema=all</jvm-options>
</java-config>
```

### Netbeans Steps
1. Open PasswordGeneratorService Netbeans project.
2. Clean and build PasswordGeneratorService.
3. Run PasswordGeneratorService (To deploy it).
4. Close the pop up window.
5. Clean and build XYZDriverAssociation.
