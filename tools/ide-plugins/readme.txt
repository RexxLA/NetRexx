Language Server support for NetRexx

These plugins provide the following language support for NetRexx in Microsoft Visual Studio Code, JetBrains Ultimate IntelliJ and Eclipse
 - Diagnostics: regenerated on each file open, change or save
 - DocumentSymbol: generate outline, class and methods

Configure the onChange timeout in the NetRexx Language plugin setttings
 ~ Set --onChange=2000 to set the delay for interactive parsing to 2 secondds.
 - Set --onChange=off to disable interactive parsing

Configure classpath field to find dependent classes
Configure log checkbox to report raw LSP traffic in stderr

Install extension lsp4nrx-1.0.1.vsix on Microsoft's vscode 
Install plugin lsp4nrx-1.0.1.zip on JetBrains' Ultimate IntelliJ - prereqs LSP4IJ plugin
Install plugin lsp4nrx.1.0.0.1 from NetRexx-UpdateSite.zip  on eclipse
