call nrc -nocrossref ChatRMIServerI ChatRMIClientI
call nrc -nocrossref ChatRMIServer
rmic network.rmichat.ChatRMIServer
call nrc -nocrossref ChatClient
rmic network.rmichat.ChatRMIClient