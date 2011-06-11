    if name='DiagForward' then do
      '@copy DiagForward2.class' targdir '>nul'
      if rc=0 then '@erase DiagForward2.class >nul'
      '@copy DiagForward3.class' targdir '>nul'
      if rc=0 then '@erase DiagForward3.class >nul'
      '@copy DiagForward4.class' targdir '>nul'
      if rc=0 then '@erase DiagForward4.class >nul'
      if rc<>0 then exit rc
      end
    if name='DiagAbstract' then do
      '@copy DiagAbstract2.class' targdir '>nul'
      if rc=0 then '@erase DiagAbstract2.class >nul'
      end
