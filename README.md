# esd_assignment
## Git Guide
[Git Guide](https://rogerdudler.github.io/git-guide/)
## Private Rest Service Setup
Follow these steps in order.

### Configuring the IDE
To generate a web service client in the IDE from a web service or WSDL file you need to modify the IDE configuration file (netbeans.conf) to add the following switch to netbeans_default_options.

`-J-Djavax.xml.accessExternalSchema=all`
For more about locating and modifying the netbeans.conf configuration file, see [Netbeans Conf FAQ](http://wiki.netbeans.org/FaqNetbeansConf).

### Netbeans Steps
1. Open PasswordGeneratorService Netbeans project.
2. Clean and build PasswordGeneratorService.
3. Run PasswordGeneratorService (To deploy it).
4. Close the pop up window.
5. Clean and build XYZDriverAssociation.
